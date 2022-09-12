![Sprint 2 - Infrastructure](docs/banner.png)

This is Java code for a simple API that displays a greeting. 

## Table of Contents
- [Table of Contents](#table-of-contents)
- [Pre-requisites](#pre-requisites)
- [Tasks](#tasks)
- [Setup](#setup)
- [Building & Running.](#building--running)
- [Accessing the API](#accessing-the-api)

---

## Pre-requisites
- [ ] [Maven](https://maven.apache.org/index.html)
- [ ] [OpenJDK 18](https://openjdk.org/)
- [ ] IDE ([IntelliJ IDEA](https://www.jetbrains.com/idea/) recommended)

## Tasks
While working with this code, you'll perform several tasks when working with this code:

- [ ] Clone the code locally using Git via the command line
- [ ] Import the code to the Java IDE
- [ ] Build the code via the Java IDE
- [ ] Identify the deployment artifact (i.e., WAR file)
- [ ] Deploy the WAR to your web server

It is recommended to do all of the above from scratch with no assistance. However, documentation has been provided below on how to complete a number of the key steps.

## Setup
1. Check into the `api/` directory.
```shell
cd cr-sprint2-sabertooth/api
```

1. Install pre-requisites. (_Note: All pre-requisites can be setup inside of IntelliJ and do not require these commands_)
```
brew install maven
brew install openjdk
```

3. Congrats! You are ready to start developing!

## Building & Running.
This following are instructions are aimed at building and running the API from the command line. For doing this in IntelliJ or another IDE, please refer to your IDE's respective documentation.

1. To build the API, you can use the following command. Here, we are building the API with Maven and packaging it into a single `.war` file.
```shell
mvn package
```

2. Now, we can run the API!
```
java -jar target/api-0.0.1-SNAPSHOT.war
```

## Accessing the API
After you have built and started to run the this code locally, use the following URLs to access the API.

* `http://localhost:8080/greeting`
* `http://localhost:8080/greeting?name=Kesha`
