# RouteMe-API
[![Build Status](https://travis-ci.org/heshamMassoud/RouteMe-API.svg?branch=master)](https://travis-ci.org/heshamMassoud/RouteMe-API) [![Codacy Badge](https://api.codacy.com/project/badge/Grade/1b82f6d1ce9e4af2b6ff983495eba77d)](https://www.codacy.com/app/heshamhamdymassoud/RouteMe-API?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=heshamMassoud/RouteMe-API&amp;utm_campaign=Badge_Grade)

A JSON-Based RESTful API for the RouteME collaborative multi-modal route recommender app. This API is build as a backend support for the prototype iOS app RouteMe which is developed as a prototype to support the research of my Master thesis topic which is "Collaborative and Social Multi-modal Route Planning"

## Run the API application locally
Start the MongoDB instance first and then you're good to start the API server and start using it.
```
mvn clean install
mvn clean package
java -jar 'path to created jar'
```
## Data persistence
The data store used is MongoDB
###### Start MongoDB locally
`mongod`
###### The API server by default now points to the database
`test`
###### To connect to MongoDB through CLI
`mongo db`


## PredictionIO setup
### Installation 
https://docs.prediction.io/install/
### Usage
Interaction with PredictionIO is done through the CLI. It follows the format of:

`pio <command> [options] <args>...`

You can run pio help to see a list of all available commands and pio help <command> to see details of the command.

PredictionIO commands can be separated into the following three categories.

##### General Commands

`pio help` Display usage summary. pio help <command> to read about a specific subcommand.

`pio version` Displays the version of the installed PredictionIO.

`pio status` Displays install path and running status of PredictionIO system and its dependencies.

##### Event Server Commands

`pio eventserver` Launch the Event Server.

`pio app` Manage apps that are used by the Event Server.

`pio app data-delete <name>` deletes all data associated with the app.

`pio app delete <name>` deletes the app and its data.

`--ip <value>` IP to bind to. Default to localhost.

`--port <value>` Port to bind to. Default to 7070.

`pio accesskey` Manage app access keys.

##### Engine Commands

Engine commands need to be run from the directory that contains the engine project. `--debug` and `--verbose` flags will provide debug and third-party informational messages.

###### Running a Recommender engine for the first time
Create a repository for the recommendation engine using the template 'template-scala-parallel-universal-recommendation'
After you have downloaded an Engine Template, you can deploy it with these steps:

- Run `pio app new **your-app-name-here**` and specify the appName used in the template's engine.json file (you can set it there to your preference).
- Run `pio build` to build an engine in the current directory.
- Run `pio train` to train a predictive model with training data of an engine in the current directory.
- Run `pio deploy` to deploy the engine in the current directory as a service.
- Guide here: https://docs.prediction.io/deploy/

##### Programmatic Use in RouteMe-API
The PredictionIO Java SDK https://github.com/PredictionIO/PredictionIO-Java-SDK is used.

### Engine Template Used in RouteMe-API
https://github.com/PredictionIO/template-scala-parallel-universal-recommendation

This template uses ElasticSearch and Hbase for storage.
##### Common errors
Running `pio status` most of the time throws an exception. Due to some instance of Hbase not being closed correctly. To fix it, do the following:
* Run `pio-stop-all`
* if it gets stuck in hbase, you may need manually kill it by running `jps`
* if you see "HMaster" processes, for example: `69687 HMaster` then manually kill the process by `kill -9 69687`
* Run `pio-stop-all` again
* Run `jps` again  to make sure no more HMaster process.
* manually kill them if there is any.
* run `pio-start-all`
* run `pio status` (most probably the error is still there)

#### Steps for development
* Start pio data stores 
```
PredictionIO-0.9.7-aml/bin/pio-start-all
```
* Check if everything is started using
```
jps -l
```
* The output should look something like this:
```
  15344 org.apache.hadoop.hbase.master.HMaster
  15409 io.prediction.tools.console.Console
  15256 org.elasticsearch.bootstrap.Elasticsearch
  15469 sun.tools.jps.Jps
```
* Check the status of the dependencies
```
./pio status
```
* If you get the Hbase not initialised correctly error follow the section above
* Start event server 
```
./pio eventserver
```
* Start mongodb server 
```
mongod
```
* Build, package and run the application server
```
mvn clean install
mvn clean package
java -jar *name of packaged jar file*
```
### Developer Forums
https://groups.google.com/forum/#!forum/predictionio-dev

