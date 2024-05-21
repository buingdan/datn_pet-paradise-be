# Sử dụng image maven với JDK 17 để build ứng dụng
FROM maven:3.8.4-openjdk-17 AS build

# Cài đặt JDK 21
RUN apt-get update && \
    apt-get install -y wget tar && \
    wget https://download.java.net/java/GA/jdk21/35/GPL/openjdk-21_linux-x64_bin.tar.gz && \
    tar -xvf openjdk-21_linux-x64_bin.tar.gz && \
    mv jdk-21 /usr/local/ && \
    update-alternatives --install /usr/bin/java java /usr/local/jdk-21/bin/java 1 && \
    update-alternatives --set java /usr/local/jdk-21/bin/java && \
    update-alternatives --install /usr/bin/javac javac /usr/local/jdk-21/bin/javac 1 && \
    update-alternatives --set javac /usr/local/jdk-21/bin/javac && \
    rm openjdk-21_linux-x64_bin.tar.gz

# Kiểm tra phiên bản Java để đảm bảo rằng JDK 21 đã được cài đặt thành công
RUN java -version

# Thiết lập thư mục làm việc
WORKDIR /usr/src/app

# Sao chép tệp pom.xml và cài đặt các phụ thuộc
COPY pom.xml ./
RUN mvn dependency:go-offline

# Sao chép mã nguồn ứng dụng
COPY src ./src

# Build ứng dụng
RUN mvn package -DskipTests

# Sử dụng image OpenJDK 21 để chạy ứng dụng
FROM openjdk:21-jdk-slim

# Thiết lập thư mục làm việc
WORKDIR /app

# Sao chép file jar từ giai đoạn build
COPY --from=build /usr/src/app/target/*.jar app.jar

# Expose port (thường là 8080 cho ứng dụng Spring Boot)
EXPOSE 8080

# Chạy ứng dụng
ENTRYPOINT ["java", "-jar", "app.jar"]
