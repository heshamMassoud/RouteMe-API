# RouteMe-API
[![Build Status](https://travis-ci.org/heshamMassoud/RouteMe-API.svg?branch=master)](https://travis-ci.org/heshamMassoud/RouteMe-API)

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
### Start MongoDB locally
`mongod`
### The API server by default now points to the database
`test`
### To connect to MongoDB through CLI
`mongo db`


## PredictionIO setup
#### CLI
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
- Run `pio build` to update the engine
- Run `pio train` to train a predictive model with training data
- Run `pio deploy` to deploy the engine as a service
- Guide here: https://docs.prediction.io/deploy/



