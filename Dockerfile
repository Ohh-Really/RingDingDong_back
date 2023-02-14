FROM gradle:jdk11-alpine as builder
WORKDIR /build

COPY . .
RUN gradle clean -x test build || return 0;

#APP
FROM openjdk:11-jre

COPY --from=builder /build/build/libs/ringdingdong-0.0.1-SNAPSHOT.jar /app.jar

EXPOSE 80

ENTRYPOINT ["java", "-jar", "/app.jar" ]
