Docker File

FROM eclipse-temurin:23-jdk AS build

WORKDIR /app

COPY mvnw ./
COPY .mvn .mvn/

RUN chmod +x mvnw

COPY pom.xml ./
RUN ./mvnw dependency:go-offline

COPY src ./src
RUN ./mvnw package -DskipTests

FROM eclipse-temurin:23-jre

WORKDIR /app

COPY --from=build /app/target/*.jar ./app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/app.jar"]


// Terminal Add 

docker build -t <imageName> .

docker build --platform linux/amd64 -t <images-name> .

docker tag  <images-name> <gitusename>/<images-name>:latest

docker push <gitusename>/<images-name>:latest
