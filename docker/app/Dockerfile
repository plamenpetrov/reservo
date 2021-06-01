FROM openjdk:11-jdk-slim as mvnimage
MAINTAINER "plamen.milchev.petrov@gmail.com"

ARG MAVEN_VERSION=3.8.1
ARG USER_HOME_DIR="/root"
ARG SHA=0ec48eb515d93f8515d4abe465570dfded6fa13a3ceb9aab8031428442d9912ec20f066b2afbf56964ffe1ceb56f80321b50db73cf77a0e2445ad0211fb8e38d
ARG BASE_URL=https://apache.osuosl.org/maven/maven-3/${MAVEN_VERSION}/binaries

RUN apt-get update && \
    apt-get install -y \
      curl procps \
  && rm -rf /var/lib/apt/lists/*

RUN mkdir -p /usr/share/maven /usr/share/maven/ref \
  && curl -fsSL -o /tmp/apache-maven.tar.gz ${BASE_URL}/apache-maven-${MAVEN_VERSION}-bin.tar.gz \
  && echo "${SHA}  /tmp/apache-maven.tar.gz" | sha512sum -c - \
  && tar -xzf /tmp/apache-maven.tar.gz -C /usr/share/maven --strip-components=1 \
  && rm -f /tmp/apache-maven.tar.gz \
  && ln -s /usr/share/maven/bin/mvn /usr/bin/mvn

ENV MAVEN_HOME /usr/share/maven
ENV MAVEN_CONFIG "$USER_HOME_DIR/.m2"

COPY docker/app/mvn-entrypoint.sh /usr/local/bin/mvn-entrypoint.sh
COPY docker/app/settings-docker.xml /usr/share/maven/ref/

ENTRYPOINT ["/usr/local/bin/mvn-entrypoint.sh"]
CMD ["mvn"]

RUN mkdir -p /usr/src/reservo
WORKDIR /usr/src/reservo

ARG APP_NAME
ARG APP_VERSION
ARG APP_USERNAME

#RUN apt-get update -y && apt-get install -y xmlstarlet

COPY pom.xml /usr/src/reservo

COPY src /usr/src/reservo/src

RUN mvn clean package

##JDK 11 Build
FROM openjdk:11-jdk-slim
EXPOSE 8082

ARG APP_NAME
ARG APP_VERSION
ARG APP_USERNAME

#COPY docker/app/etc/supervisor/conf.d/* /etc/supervisor/conf.d
#RUN for file in /etc/supervisor/conf.d/*.conf; do sed -i "s/{APP_NAME}/${APP_NAME}/g" ${file}; done
#RUN for file in /etc/supervisor/conf.d/*.conf; do sed -i "s/{APP_USERNAME}/${APP_USERNAME}/g" ${file}; done

RUN adduser --system --no-create-home --group --uid 87432 ${APP_USERNAME}

RUN mkdir -p /opt/reservo/${APP_NAME}
WORKDIR /opt/reservo/${APP_NAME}

COPY docker/app/bin/bootstrap.sh /usr/local/bin/bootstrap
#RUN chmod a+x /usr/local/bin/bootstrap

COPY --from=mvnimage /usr/src/reservo/target/${APP_NAME}-${APP_VERSION}.jar reservo.jar
RUN chown ${APP_USERNAME}:${APP_USERNAME} reservo.jar
RUN chmod +x reservo.jar

ENTRYPOINT ["java", "-jar", "reservo.jar"]
