# Fees Register for HMCTS online services
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=uk.gov.hmcts.reform.fees-register%3Afees-register-app&metric=alert_status)](https://sonarcloud.io/dashboard?id=uk.gov.hmcts.reform.fees-register%3Afees-register-app)
[![Build Status](https://travis-ci.org/hmcts/ccfr-fees-register-app.svg?branch=master)](https://travis-ci.org/hmcts/ccfr-fees-register-app)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/0cb10a161dc24d0092470cda7c304c87)](https://app.codacy.com/app/HMCTS/ccfr-fees-register-app)
[![codecov](https://codecov.io/gh/hmcts/ccfr-fees-register-app/branch/master/graph/badge.svg)](https://codecov.io/gh/hmcts/ccfr-fees-register-app)

This project provides REST based web services for exposing fees related information for hearing fee and award fees.

## Getting Started

This is SpringBoot based java application. Please see the Jenkinsfile in root folder to see the build and deployment pipeline.

### Prerequisites

You will need jdk installed on your machine.

### Installing
1. Clone the repo to your machine using git clone git@github.com:hmcts/ccfr-fees-register-app.git
2. Run $ ./gradlew build

## Running the tests

You can run the tests using './gradlew test'


## Deployment

See Jenkinsfile for the deployment details

## DB migration task

To run the DB migration task locally, run

```./gradlew --no-daemon '-Pdburl=localhost:5432/fees_register' -Pflyway.user=fees_register -Pflyway.password=fees_register migratePostgresDatabase```

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

## Service Versioning

We use [SemVer](http://semver.org/) for versioning.

