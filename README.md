# MetaStats-CLI

[![Build Status](https://travis-ci.com/qbicsoftware/metastats-cli.svg?branch=development)](https://travis-ci.com/qbicsoftware/metastats-cli)[![Code Coverage]( https://codecov.io/gh/qbicsoftware/metastats-cli/branch/development/graph/badge.svg)](https://codecov.io/gh/qbicsoftware/metastats-cli)

MetaStats-CLI, version 1.0.0-SNAPSHOT - Command-line tool to create a metadata table for an openBIS project

## Author
Created by Jennifer Bödker (jennifer.boedker@student.uni-tuebingen.de).

## Description

MetaStats is a tool to collect the metadata from an existing openBis project. The metadata is then transformed into a tsv-file which is downloaded
to the users download folder. This file can then be used for statistical analysis of the project.

## How to Install
Execute ```mvn clean package``` in the terminal to create the jar.

## How to run
MetaStats-CLI is run with the command:

``java -jar metastats-application/target/metastats-1.0.0-SNAPSHOT-jar-with-dependencies.jar -c path-to-conf -p projectCode``

| Command line option | Description                                                  |
| ------------------- | ------------------------------------------------------------ |
| `-c`                | defines the path to the [*configuration file*](#exemplary-configuration-file) |
| `-p`                | specifies the project code for which the description should be loaded |
(note: information can only be loaded for projects the user has access to)

### Exemplary Configuration File
```
{
   "server_url" : "https://example.url.de",
   "user": "name",
   "password": "pw"
}
```
