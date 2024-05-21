FROM openjdk:17-slim
COPY target/*.jar app.jar
EXPOSE 8090
CMD ["java", "-jar", "app.jar"]

