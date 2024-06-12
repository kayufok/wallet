# Blockchain wallet statistic application

## (Optional) Setup MYSQL Database in Docker in Ubuntu
### prerequisites - setup docker network

```bash
docker network create -d bridge nginx-net
```

### MYSQL database initialization

```bash
# Run a Docker container named 'wallet-db' with the following specifications:
# - Use the 'mysql:latest' image.
# - Set the root password for MySQL to 'Qwer1234'.
# - Connect the container to the 'nginx-net' network.
# - Mount the '/home/database/crypto' directory on the host machine to '/var/lib/mysql' inside the container.
# - Map the host port 3306 to the container port 3306.
docker container run -d \
  --name wallet-db \
  -e MYSQL_ROOT_PASSWORD=Qwer1234 \
  --network nginx-net \
  -v /home/database/crypto:/var/lib/mysql \
  -p 3306:3306 \
  mysql:latest
```

### Docker housekeeping

```bash
docker image prune -a -f
```

### Build the application using the following command
```bash
docker build -t wallet-app-image .
```

### start the application using the following command
```bash
docker run -p 8080:8080 --name wallet-app --network nginx-net wallet-app-image:latest
```