FROM openjdk:17
ARG JAR_FILE=build/libs/*.jar
ENV SPRING_PROFILES_ACTIVE=develop

# 작업 디렉토리 설정
WORKDIR /app

# 빌드된 JAR 파일 복사
COPY ${JAR_FILE} casealot-backend.jar

RUN apk add tzdata && ln -snf /usr/share/zoneinfo/Asia/Seoul /etc/localtime

# 실행
CMD ["java","-jar","-Dspring.profiles.active=${SPRING_PROFILES_ACTIVE}","casealot-backend.jar"]

