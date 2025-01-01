FROM maven:3-eclipse-temurin-21 AS build
WORKDIR /app

COPY pom.xml /app
#RUN mvn dependency:resolve
COPY . /app
RUN mvn clean
RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-alpine

WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

EXPOSE 9020

CMD ["java", "-jar", "app.jar"]