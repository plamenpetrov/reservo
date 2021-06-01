FROM mysql:8.0.25 as mysql-instance

COPY data/*.sql /tmp/data/
RUN cat /tmp/data/*.sql > /tmp/data.sql

RUN ls -la /tmp
CMD ["mysqld", "--sql-mode=", "--init-file=/tmp/data.sql"]