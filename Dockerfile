FROM openjdk:11-jdk-slim as build
WORKDIR /khumu

COPY gradle gradle
COPY build.gradle gradlew gradlew.bat ./
COPY src src

RUN ./gradlew build -x test

FROM openjdk:11-jre-slim
WORKDIR /khumu
COPY --from=build /khumu/src/main/resources resources
COPY --from=build /khumu/build/libs/*.jar community.jar
# java -Dspring.profiles.active=dev -jar community.jar --spring.config.location=resources/
ENTRYPOINT ["java", "-Dspring.profiles.active=dev", "-jar", "community.jar", "--spring.config.location=resources/"]
