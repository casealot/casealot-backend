## Jenkins 세팅

1. 이미지 pull 
```bash
docker pull jenkins/jenkins
```
2. jenkins execute
```bash
docker run --name jenkins-docker -d -p 8080:8080 -v /jenkins:/var/jenkins_home -p 50000:50000 -u root jenkins/jenkins
```
-v /jenkins:/var/jenkins_home < 도커의 데이터가 날라가기 때문에 마운트 설정

3. jenkins log
```bash
docker logs jenkins-docker
```