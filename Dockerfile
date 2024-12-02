FROM openjdk:17-jdk-slim
ARG JAR_FILE=RevUp-api/build/libs/RevUp-api-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar

# wait-for-it.sh 스크립트를 복사
COPY wait-for-it.sh /wait-for-it.sh
RUN chmod +x /wait-for-it.sh

# MySQL 클라이언트 설치
RUN apt-get update && apt-get install -y mysql-client

ENTRYPOINT ["java","-Dspring.profiles.active=local", "-jar", "/app.jar"]