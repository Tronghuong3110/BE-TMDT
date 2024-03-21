FROM openjdk:17
ADD ./api-IOT.jar api-IOT.jar

ENTRYPOINT ["java", "-jar", "api-IOT.jar"]