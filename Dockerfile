FROM eclipse-tenurin:17-jdk-alpine
VOLUME /tmp
COPY target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "/*.jar"]
EXPOSE 8080
