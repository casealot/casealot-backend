FROM openjdk:17-jdk-slim
ARG JAR_FILE=build/libs/*.jar

# 작업 디렉토리 설정
WORKDIR /app

# 빌드된 JAR 파일 복사
COPY ${JAR_FILE} casealot-backend.jar

# 실행
CMD ["java","-jar","casealot-backend.jar"]

