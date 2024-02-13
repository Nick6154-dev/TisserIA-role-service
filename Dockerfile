FROM eclipse-temurin:21-jre

COPY *.jar /app/role-service.jar

WORKDIR /app

CMD ["java", "-jar", "role-service.jar"]