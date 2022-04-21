FROM amazoncorretto:17
ADD build/libs/*SNAPSHOT.jar resource-processor.jar
ENTRYPOINT ["java","-jar","resource-processor.jar"]