FROM eclipse-temurin:17-jre

WORKDIR /app

COPY target/*.jar smsorderservice.jar

EXPOSE 9001

ENTRYPOINT ["java","-jar","smsorderservice.jar"]