# Prerequisites

- Java 11
- Maven

# Architecture decisions

- Clean architecture aka Port and Adapters.
- A modular monolith with modularization on package level. That provides high cohesion, low coupling, and great options
  for the journey to microservices.
- The code mostly follows OOP principles. 
- Implementation stops on the application level

# Domain modeling
![alt text](https://github.com/zielichowski/layer-demo//blob/master/events.png?raw=true)
- I used the event storming technique to better understand the domain.
- After the session, I came up with multiple conclusions:
  - There is a core bounded context related to publishing articles.
  - There is a generic subdomain related to authorization/authentication. I decided to skip it completely in the implementation.
  - I focused on the article subdomain.
  - Another possible subdomain which is not included in implementation is the topic.
- Furthermore, I took some assumptions:
  - We assign a copywriter to the draft article when creating it.
  - Article suggestions are implemented as the Entity in the Article Aggregate. I can imagine that I could be a separate aggregate but from the task description, I didn't found enough reasons to do it.
  
# Skipped parts
- Persistence layer is not something you should pay attention to. I implemented a very simple in-memory mechanism.
- There are no GUI or REST controllers.
- The query part from CQRS is skipped.
- Identity and access domain

# Test

- Tests are written in the best test framework for JVM applications - spock
- I focused on Unit tests
- Integration tests are skipped since there are no real external dependencies like DB, messaging system, etc.
- Architecture tests are supported by ArchUnit.
- The main purpose is to test high cohesion and low coupling between modules

# Package structure
- When using the DDD approach there are multiple ways to organize the code. I prefer to keep related things together and minimize the scope of each class. In practice when I use Java I keep most of the classes `package-private`. 
Since Kotlin miss this nice feature we have to use the `internal` keyword which is slightly different but good enough.
- I have made some simplifications regarding this rule e.g: `ArticleSuggestions`. 
- Package `api` represents public classes

# Covered user stories
- Domain model covers mostly the happy path of all user stories
- In the `ArticleApplicationService` I implemented the following user stories:
  - 4. As a journalist, I respond to suggestions by making the suggested changes
  - 5. As a copywriter, I resolve suggestions that the journalist applied
  - 6. As a journalist, I can publish the article after all suggestions are resolved
