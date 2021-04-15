# Prerequisites

- Java 11
- Maven

# Architecture decisions

- Clean architecture aka Port and Adapters.
- A modular monolith with modularization on package level. That provides high cohesion, low coupling and great options
  for the journey to microservices.
- The code mostly follow OOP priciples. 
- Implementation stops on application level

# Domain modeling
![alt text](https://github.com/zielichowski/layer-demo//blob/master/events.png?raw=true)
- I used event storming technique to better understand the domain.
- After the session I came up with multiple conclusions:
  - There is a core bounded context related to publishing articles.
  - There is a generic subdomain related to authorization/authentication. I decided to skip it completly in the implementation.
  - I focused on the article subdomain.
  - Another possible subdomain which is not included in implementation is topic.
- Furthermore I took some assumptions:
  - We assign copywriter to the draft article when creating it.
  - Article suggestions are impemented as the Entity in the Article Aggregate. I can imagine that I could be a separate aggragate but from the task description I didn't found enough reasons to do it.
  
# Skipped parts
- Persistence layer is not something you should pay attention to. I impemented very simple in memory mechanism.
- There is no GUI or REST controllers.
- The query part from CQRS is skipped.
- Identity and access domain

# Test

- Tests are written in the best test framework for JVM applications - spock
- I focused on Unit tests
- Integration tests are skipped since there is no real external dependencies like DB, messaging system, etc.
- Architecture test are supported by ArchUnit.
- The main purpose is to test high cohesion and low coupling between modules

# Package structure
- When using DDD approach there are multiple ways to organise the code. I prefere to keep related things together and minimize the socope of each class. In pratice when I use Java I keep most of classes `package-private`. 
Since Kotlin miss this nice feature we have to use `internal` keyword which is slighly different but good enough.
- I have made some simplifications regarding this rule e.g: `ArticleSuggestions`. 
- Package `api` represents public classes

# Covered user stories
- Domain model covers mostly the happy path of all user stories
- In the `ArticleApplicationService` I implemented the following user stories:
  - 4. As a journalist, I respond to suggestions by making the suggested changes
  - 5. As a copywriter, I resolve suggestions that the journalist applied
  - 6. As a journalist, I can publish the article after all suggestions are resolved
