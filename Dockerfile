FROM openjdk:18-alpine AS build

WORKDIR /app

COPY pom.xml ./
RUN mvn -f pom.xml package

FROM openjdk:18-alpine

WORKDIR /app

COPY target/*.jar ./

EXPOSE 8080

CMD ["java", "-jar", "/*.jar"]

