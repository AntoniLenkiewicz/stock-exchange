#!/bin/sh

mvn clean

mvn package

java -jar target/exchange-0.0.1-SNAPSHOT.jar
