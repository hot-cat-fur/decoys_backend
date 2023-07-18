#
# Build stage
#
FROM maven:3.8.2-jdk-11 AS build
COPY . .
RUN mvn clean package -DskipTests

#
# Package stage
#
FROM openjdk:11-jdk-slim
COPY --from=build /target/orgasms-0.0.1-SNAPSHOT.jar orgasms.jar
# ENV PORT=8050
EXPOSE 8050
ENTRYPOINT ["java","-jar","orgasms.jar"]