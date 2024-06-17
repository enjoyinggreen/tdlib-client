# tdlib-client

## Docker creation
docker volume create postgreSqlData --opt type=none --opt device=$(pwd) --opt o=bind
docker run --name my-postgres -h 127.0.0.1 --env POSTGRES_PASSWORD=admin --volume postgreSqlData:/var/lib/postgresql/data --publish 5432:5432 --detach postgres
