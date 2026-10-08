# Build any service: docker build --build-arg SERVICE=user-service -t user-service .
FROM maven:3.9-eclipse-temurin-17 AS build
ARG SERVICE
WORKDIR /src
COPY . .
RUN mvn -q -pl ${SERVICE} -am package -DskipTests

FROM eclipse-temurin:17-jre
ARG SERVICE
WORKDIR /app
COPY --from=build /src/${SERVICE}/target/app.jar app.jar
ENTRYPOINT ["java","-jar","/app/app.jar"]
