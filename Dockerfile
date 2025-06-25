# OpenJDK 17을 기반으로 하는 이미지 사용
FROM openjdk:17-jdk-slim

# 작업 디렉토리 설정
WORKDIR /app

# 빌드된 JAR 파일을 컨테이너로 복사
COPY build/libs/*.jar app.jar

# 업로드 디렉토리 생성
RUN mkdir -p /app/upload

# 포트 9090 노출
EXPOSE 9090

# 애플리케이션 실행
ENTRYPOINT ["java", "-jar", "app.jar"]