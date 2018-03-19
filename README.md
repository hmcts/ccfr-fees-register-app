# Fees Register for HMCTS online services

This project provides REST based web services for exposing fees related information for hearing fee and award fees.

## Getting Started

This is SpringBoot+maven based java application. Please see the Jenkinsfile in root folder to see the build and deployment pipeline.

### Prerequisites

You will need jdk and maven installed on your machine or use mvnw to install the prerequisites

### Installing
1. Clone the repo to your machine using git clone git@git.reform.hmcts.net:fees-register/fees-register-app.git
2. Run $ ./gradlew build

## Running the tests

You can run the tests using './gradlew test'


## Deployment

See Jenkinsfile for the deployment details

## Run the application
To run the application at local developer machine use following command

$ ./gradlew bootRun

Once application server is started use swagger ui to find the endpoints and test these. 
http://localhost:8080/swagger-ui.html

or in dev/test environment you can use this link
https://dev-proxy.fees-register.reform.hmcts.net/swagger-ui.html
or https://test-proxy.fees-register.reform.hmcts.net/swagger-ui.html

## Service Endpoints
Some of the end points are as below. These might be out of date. Please look at the swagger-ui to be sure. 

- GET /fees-register/cmc
- GET /fees-register/cmc/categories
- GET /fees-register/cmc/categories/{id}/ranges/{amount}/fees
- GET /fees-register/cmc/flat
- GET /fees-register/cmc/flat/{id}

## Built With

* [Maven](https://maven.apache.org/) - Dependency Management

## Service Versioning

We use [SemVer](http://semver.org/) for versioning.

