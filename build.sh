#!/bin/zsh

./gradlew build \
&& nohup sudo -E java -jar ./build/libs/myapp_api-0.0.1-SNAPSHOT.jar --server.port=$SPRING_PORT