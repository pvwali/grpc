FROM openjdk:8
COPY target/grpc.examples-1.0-SNAPSHOT-jar-with-dependencies.jar /app/grpc.example.jar

EXPOSE 50051
ENTRYPOINT ["java", "-jar", "/app/grpc.example.jar"]