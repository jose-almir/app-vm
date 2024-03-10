FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app
COPY . /app/
RUN mvn clean package

FROM eclipse-temurin:21-jdk
COPY --from=build /app/target/*.jar /app.jar
RUN mkdir -p uploads uploads/events uploads/users
EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]