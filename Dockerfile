# Sử dụng image maven để build ứng dụng
FROM maven:3.8.4-openjdk-17 AS build

# Thiết lập thư mục làm việc
WORKDIR /usr/src/app

# Sao chép tệp pom.xml và cài đặt các phụ thuộc
COPY pom.xml ./
RUN mvn dependency:go-offline

# Sao chép mã nguồn ứng dụng
COPY src ./src

# Build ứng dụng
RUN mvn package -DskipTests

# Sử dụng image OpenJDK để chạy ứng dụng
FROM openjdk:17-jdk-slim

# Thiết lập thư mục làm việc
WORKDIR /app

# Sao chép file jar từ giai đoạn build
COPY --from=build /usr/src/app/target/*.jar app.jar

# Expose port (thường là 8080 cho ứng dụng Spring Boot)
EXPOSE 8080

# Chạy ứng dụng
ENTRYPOINT ["java", "-jar", "app.jar"]
