FROM openjdk:16-jdk-alpine

WORKDIR /src
COPY . /src

RUN apk --update add bash
RUN apk --no-cache add dos2unix wget curl
RUN dos2unix gradlew

RUN bash gradlew fatJar

WORKDIR /run

COPY ./build/libs/*.jar /run/server.jar

EXPOSE 8080
EXPOSE 5432

CMD java -jar /run/server.jar