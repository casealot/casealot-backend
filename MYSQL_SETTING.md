### 🐳 docker mysql pull
```shell
docker pull mysql
```

### 🐳 docker mysql run
```shell
docker run -d -p 3306:3306 -e MYSQL_ROOT_PASSWORD=1234 --name mysql mysql
```

### 🐳 docker mysql execute & create DB
```shell
docker exec -it mysql bash
mysql -u root -p
create database stock;
```

### 🐳 docker Check
```shell
docker ps
```