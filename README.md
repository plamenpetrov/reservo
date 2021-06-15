# Java Spring Boot RestFul API designed as microservice by following DDD and EDA

## Getting Started
This project was designed following Domain-driven principles ((Domain-driven design). 
Furthermore, in order to support high load and process thousands of reservations per second
this project can consume and produce events for updating, deleting, or creating an entity.
More about Event-driven architectures (EDA) can be found  
[here](https://aws.amazon.com/event-driven-architecture/)
 
This is basic implementation of RestFul API based on Spring Boot Framework. 
So, no frontend implemented.

Clone the repository using git command:

```
git clone https://github.com/plamenpetrov/reservo.git
```

### Installing

Before you run application you need to have installed Java 11 and some IDE like IntelliJ, Eclipse, Netbeans or what you wish.
Also, [maven](https://maven.apache.org/what-is-maven.html) is required.

Create .env file using env.example:
```
cp .env.example .env
```

### Dependencies

* Web
* Spring Secutiry Web
* Spring Boot Starter Data REST
* JPA
* MySQL
* Lombok
* Kafka
* Model Mapper

### IDE Configuration

Ensure plugins Maven and Lombok are installed from File/Settings/Plugins. Add a new configuration of type Application.
For main class choose `com.pp.reservo.ReservoApplication`
Copy the entire content of .env file and paste it directly into the Environment Variables
Enable Maven and run `mvn clean`, `mvn package` and `mvn install`


### Running the Project
There are two ways to run the project:
- using IDE - To run application via the IDE using the `▶` button. Application is running on http://localhost:8080

- using Docker
To run a project via Docker run the following command
`docker-compose up --build` 


# Main functionalities
This project is Spring boot based, and the main goal was to create simple 
reservation system with the following functionalities:

* Basic authentication with username and password
* List all Clients, Appointments, Employee and Reservations
* Search for entities by different criteria such as name, period, ect.
* Create, remove, update and get information about each client, employee, appointment and reservation
* Advanced search and provide information for reservations by Client or Employee
* Show details for reservation
* Easily integrate with any Calendar or any other FE framework

### What was implemented in this project?

Some implementations include:
* Custom validation
* Global error handling with custom messages
* Search specification for filtering
* Pagination for endpoints which return list of entities
* Command pattern implementation for consuming Kafka messages
* Docker integration for easy CI/CD deployment
* Full application documentation with Openapi and Swagger integration
* Kafka integration to consume messages
* Kafka integration to produce messages
* All functionalities covered with Tests (Unit, Feature and Integration)

## Built With

* [Spring Boot Framework](https://spring.io/)
* [Java SE 11](https://www.oracle.com/java/technologies/javase-jdk11-downloads.html)
* [Maven](https://maven.apache.org/download.cgi) - Tool that can be used for building and managing any Java-based project


## Author

* **Plamen Petrov** - [PlamenPetrov](https://github.com/plamenpetrov)

## License

This project is licensed under the MIT License.
