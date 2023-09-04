# Explore with me

### Overview

The application allows users to share information about interesting events and helps to find a company to participate in them. It is implemented in the form of two microservices with separate databases - one for saving and receiving statistics, the second with the main business logic.


### Getting Started

```
git clone https://github.com/igushkin/java-explore-with-me.git

mvn clean
mvn package
docker-compose build
docker-compose up -d
```

#### The application includes the following services 
- The main service — contains everything necessary for the product to work.
    - Viewing events without authorization;
    - Ability to create and manage categories;
    - Events and working with them - creation, moderation;
    - User requests to participate in the event - request, confirmation, rejection;
    -Creating and managing collections;
- Statistics service — stores the number of views and allows you to make various selections to analyze the operation of the application.

### Description of services
#### The main service works on port 8080
The API of the main service is divided into three parts. The first one is public, accessible without registration to any network user. The second one is closed, available only to authorized users. The third is administrative, for service administrators.

- **Public** (available to all users)
  - API for working with events
  - API for working with categories
  - API for working with collections of events
- **Private** (available only for registered users)
  - API for working with events
  - API for working with requests from the current user to participate in events
- **Administrative** (available only for the project administrator)
  - API for working with events
  - API for working with categories
  - API for working with users
  - API for working with collections of events
#### The statistics service runs on port 9090:
Collects information. Firstly, about the number of user requests to the event lists and, secondly, about the number of requests for detailed information about the event. Based on this information, statistics about the operation of the application are generated.
- **Administrative** (available only for the project administrator)
  - API for working with visit statistics

### Swagger REST API Specification
- [Main service](https://raw.githubusercontent.com/KotTret/java-explore-with-me/main/ewm-main-service-spec.json)
- [Statistics service](https://raw.githubusercontent.com/KotTret/java-explore-with-me/main/ewm-stats-service-spec.json)
