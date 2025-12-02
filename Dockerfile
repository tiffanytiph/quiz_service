FROM eclipse-temurin:21-jre
COPY target/quiz_service-0.0.1-SNAPSHOT.jar quiz_service.jar
ENTRYPOINT ["java", "-jar", "quiz_service.jar"]
