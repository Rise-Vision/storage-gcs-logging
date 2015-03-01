# Google Cloud Storage Log Importing

## Introduction

This module will import Google Cloud Storage logs.  There are two types of logs that are generated from GCS.  Usage logs record per-request information against bucket objects while storage logs record total amount of data stored in a bucket.

These logs will be loaded into Google Big Query tables.

This module is used by [Rise Vision](http://help.risevision.com).
Rise Vision runs on Google App Engine and as such requires GAE to operate. It also uses Google Cloud Storage as a datastore.

Chrome is the only supported browser to use to view the Storage Server API Explorer.

## Built With

- Maven (3.2.1 or greater)
- Java (1.7.0_65 SDK or greater)
- GAE (Google App Engine)

## Development 

### Local Development Environment Setup and Installation

#### Linux

* Maven 3 is required so you need to do some things to make sure your apt-get doesn't install an older version of maven.

* clone the repo using Git to your local:
```bash
git clone https://github.com/Rise-Vision/storage-server.git
```

* cd into the repo directory
```bash
cd storage-server
```


* Run this command to start the server locally
``` bash
mvn clean install
mvn appengine:devserver
```

### External Registrations and Requirements
* Private p12 and client_secret files should go into src/private-keys.  These allow server to server authentication for google cloud storage.
* P12 file in the src/private-keys directory also allows you to run the api-tests by running ./run-tests.sh --password=(use your google account's password registered with risevision here)
* To register your own p12 for google app engine please refer to Google's help page on this topic: 
https://developers.google.com/storage/docs/authentication

### Build and Deployment Process

#### Linux

## Submitting Issues 

Issues should be reported in the github issue list at https://github.com/Rise-Vision/storage-server/issues  

Issues should be reported with the template format as follows:

**Reproduction Steps**
(list of steps)
1. step 1
2. step 2

**Expected Results**
(what you expected the steps to produce)

**Actual Results**
(what actually was produced by the app)

Screenshots are always helpful with issues. 

## Contributing

All contributions greatly appreciated and welcome! If you would first like to sound your contribution ideas please post your thoughts to our community (http://community.risevision.com), otherwise submit a pull request and we will do our best to incorporate it

### Languages

In order to support languages i18n needs to be added to this repository.  Please refer to our Suggested Contributions.

### Suggested Contributions

* i18n Language Support

## Resources

Source code for the jar files can be found at the following two urls:
 * http://risevision.googlecode.com/svn/!svn/bc/890/trunk/coreAPIjava/src/com/risevision/core/api/
 * https://github.com/Rise-Vision/core/tree/master/core/src/com/risevision/directory

If you have any questions or problems please don't hesitate to join our lively and responsive community at http://community.risevision.com.

If you are looking for user documentation on Rise Vision please see http://www.risevision.com/help/users/

If you would like more information on developing applications for Rise Vision please visit http://www.risevision.com/help/developers/. 

## Facilitator
[Tyler Johnson](https://github.com/tejohnso "Tyler Johnson")
