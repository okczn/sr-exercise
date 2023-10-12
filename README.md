# Scoreboard Exercise

## Overview

A simple library implementing a Football World Cup score
board. It supports the following operations:

- start a match with home and away team names
- update the match score
- finish a match
- get a summary of matches in progress

By default, the library uses in-memory storage for
registered matches. See [Usage](#usage) for more details
and customization instructions.

## Installation

The library is build with Java 17 and Maven. You can 
build it using your
Maven installation or with the Maven wrapper `mvnw`
(recommended).

```
./mvnw install
```

As a project dependency:

Maven
```xml
<dependency>
    <groupId>okczn</groupId>
    <artifactId>scoreboard</artifactId>
    <version>1.0.0-SNAPSHOT</version>
</dependency>
```

Gradle
```kotlin
implementation("okczn:scoreboard:1.0.0-SNAPSHOT")
```

## Usage

Basic usage:

```java
class MyApp {
    public static void main(String[] args) {
        var scoreboard = new Scoreboard();

        var id = scoreboard.startMatch("Netherlands", "England");
        scoreboard.updateScore(id, 1, 1);
        scoreboard.finishMatch(id);

        var scores = scoreboard.matchSummary();
    }
}
```

The default `Scoreboard` constructor uses
`InMemoryMatchRepository` to store match information. It
can be replaced with any other implementation of
[MatchRepository](src/main/java/okczn/scoreboard/domain/MatchRepository.java)
to support different storage, such as a database or a 
shared data grid:

```java
var scoreboard = new Scoreboard(new CustomMatchRepository());
```

## Implementation Notes

I decided to use a simplified DDD approach and tiered 
architecture 
including application, domain and infrastructure tiers. 
Considering the library is simple and has only 4 use 
cases, there is one application service, brought to the 
main package for simplicity. Domain classes are not 
exposed by the application service, a simple DTO is used.
In this case it does not add great value, however it can 
be helpful if the library grows and more sophisticated 
needs emerge regarding returned details, especially if 
the data should come from multiple aggregates.
