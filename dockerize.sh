#!/usr/bin/env bash

# build jars

./gradlew build

# build docker images

echo "Starting build docker images..."
cd rest-api
docker build -t tasks-rest-api .
cd ..
cd rest-client
docker build -t tasks-rest-client .
cd ..
cd eureka-server
docker build -t eureka-server .
cd ..
cd trello-service
docker build -t tasks-trello-service .
cd ..

echo "Build is finished"

# start docker compose

echo "Starting docker-compose...press ctrl+C to cancel"


 if docker-compose up --build; then
 echo "Work is finished"
 else docker-compose down
 echo "There were errors"
 fi
