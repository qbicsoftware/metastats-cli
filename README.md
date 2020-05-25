# MetaStats-CLI

[![Build Status](https://travis-ci.com/qbicsoftware/metastats-cli.svg?branch=development)](https://travis-ci.com/qbicsoftware/metastats-cli)[![Code Coverage]( https://codecov.io/gh/qbicsoftware/metastats-cli/branch/development/graph/badge.svg)](https://codecov.io/gh/qbicsoftware/metastats-cli)

MetaStats-CLI, version 1.0.0-SNAPSHOT - Command-line tool to create a metadata table for an openBIS project

## Author
Created by Jennifer Bödker (jennifer.boedker@student.uni-tuebingen.de).

## Description

## How to Install
Execute 
```mvn clean package```
in the terminal to create the jar.

## How to use
MetaStats can be started with the command:

``java -jar metastats-application/target/metastats-1.0.0-SNAPSHOT-jar-with-dependencies.jar -c path-to-conf -p projectCode``

**-c** defines the path to the config file (see next section for more details)
**-p** specifies the project code of the desired project for which the description should be loaded

### Examplary Configuration File
```
{
   "url" : "https://example.url.de",
   "user": "name",
   "password": "pw"
}
```
