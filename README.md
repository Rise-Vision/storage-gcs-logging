# Google Cloud Storage Log Importing

## Introduction

This [Google App Engine](https://cloud.google.com/appengine/docs) (GAE) [module](https://cloud.google.com/appengine/docs/java/modules/) will import [Google Cloud Storage](https://cloud.google.com/storage/) (GCS) logs into [Google BigQuery](https://cloud.google.com/bigquery/).  There are two types of logs that are generated from GCS.  **Usage logs** record per-request information against bucket objects while **storage logs** record total stored data in a bucket.

This module is used by [Rise Vision](http://www.risevision.com).

## Built With

- Maven (3.2.1 or greater)
- Java (1.7.0_65 SDK or greater)
- GAE (Google App Engine)

## Development 

### Local Development Environment Setup and Installation

* Clone the repo using Git
```bash
git clone https://github.com/Rise-Vision/storage-gcs-logging.git
```

* Change working directory to the repo directory
```bash
cd storage-gcs-logging
```

* Run unit tests
``` bash
mvn test
```

* Run integration tests
``` bash
mvn verify
```
*Note that integration tests require p12 key files mentioned below.  The tests also expect certain files to be available in a GCS bucket.  See [src/test/resources/integration-test-files.properties](https://github.com/Rise-Vision/storage-gcs-logging/blob/feature/log-load/src/test/resources/integration-test-files.properties)*

### External Registrations and Requirements
* Private p12 key files should go into src/private-keys.  These allow server to server authentication for google cloud services.
* To register your own p12 for google app engine please refer to Google's help page on this topic: 
https://developers.google.com/storage/docs/authentication

## Submitting Issues 

Issues should be reported in the github issue list at https://github.com/Rise-Vision/storage-gcs-logging/issues  

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

All contributions greatly appreciated and welcome! If you would first like to discuss your contribution ideas please post your thoughts to our community at http://community.risevision.com, otherwise submit a pull request and we will do our best to incorporate it.

## Help

If you have any questions or problems please don't hesitate to join our lively and responsive community at http://community.risevision.com.

If you are looking for user documentation on Rise Vision please visit http://help.risevision.com/#/user

If you would like more information on developing applications for Rise Vision please visit http://help.risevision.com/#/developer

## Facilitator
[Tyler Johnson](https://github.com/tejohnso "Tyler Johnson")
