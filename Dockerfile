FROM openjdk:11-jdk

WORKDIR /server

COPY ./build/libs/dnd10_backend-0.0.1-SNAPSHOT.jar server.jar

ENTRYPOINT ["java","-jar","server.jar"]