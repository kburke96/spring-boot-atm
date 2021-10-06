# ATM Simulator API

This is a Java API using Spring Boot which simulates some actions of an ATM.

## Features
* Spring Boot 2.5.5
* Java 11
* Maven 3.8.2
* HSQL In-memory DB
* Spring Data JPA
* Lombok

[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=kburke96_spring-boot-atm&metric=coverage)](https://sonarcloud.io/dashboard?id=kburke96_spring-boot-atm)

## Pre-requisites
This project has been tested using:
* Linux Mint 20
* openjdk 11.0.9 2020-10-20
* Docker version 20.10.8, build 3967b7d

## Installation

To run this project you can either:
1. Clone from GitHub and run as a Spring Boot project with Maven:

```bash
git clone https://github.com/kburke96/spring-boot-atm.git
./mvnw spring-boot:run
```
or

2. Clone from GitHub, build a Docker image and run:
```bash
git clone https://github.com/kburke96/spring-boot-atm.git
docker build -t atm-api .
docker run -p 8080:8080 atm-api
```

> **NOTE:**  This application runs on port 8080. The above Docker run command maps port 8080 inside the container to port 8080 on the local network.




## Usage

The ATM application initialises with two accounts, as per the requirements:

Account #1  

   * Account Number: 123456789
   * PIN: 1234
   * Opening Balance: 800
   * Overdraft: 200

Account #2

   * Account Number: 987654321
   * PIN: 4321
   * Opening Balance: 1230
   * Overdraft: 150

### Calling the API

To request the balance for a specific account, send a GET request to the "/balance" endpoint with the following request parameters:
* acct: The account number
* pin: The account PIN

#### Example
```bash
localhost:8080/balance?acct=123456789&pin=1234
```

![GetRequestSuccess](./img/GetRequest_success.png?raw=true "Sample GET request")

To request a withdrawal, send a POST request to the "/withdraw" endpoint with the following request parameters:
* acct: The account number
* pin: The account PIN
* amount: The amount to withdraw

#### Example
```bash
localhost:8080/withdraw?acct=123456789&pin=1234&amount=10
```

![PostRequestSuccess](./img/PostRequest_success.png?raw=true "Sample POST request")


## Known Issues and Limitations
* **Security:** This API exposes sensitive account information and credentials in the URL of each request. This implementation should never be run in a production environment. 
At the very least, implement Basic Authentication using Spring Security to keep credentials in the header. Even then, extra measures should be taken to ensure security.

## Future Enhancements
* With Spring Boot, this project can easily be extended to use a persistent database. Because Spring Data JPA is used, there will be minimal code changes required if the storage mechanism was changed.
* Custom exceptions should be added to give the user more information about errors.
* Logging should be implemented using a standard logging framework (slf4j, log4j etc.).
* Extra functionality could be provided to allow for:
   * User account creation
   * Deposit funds
   * PIN change service
* A frontend UI application could be developed which would consume this API and display data to a user.

## Contributing
Pull requests are welcome. For major changes, please open an issue first to discuss what you would like to change.

Please make sure to update tests as appropriate.
