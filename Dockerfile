FROM openjdk:17-jdk

WORKDIR /app

COPY target/udemy-0.0.1-SNAPSHOT.jar /app/udemy.jar

EXPOSE 8080

CMD ["java", "-jar", "udemy.jar"]