## Jenkins 세팅

1. 이미지 pull 
```bash
docker pull jenkins/jenkins
```
2. jenkins execute
```bash
docker run --name jenkins-docker -d -p 8080:8080 -p 50000:50000 -u root jenkins/jenkins
```