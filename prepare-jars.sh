cd /Users/mikeburns/Documents/tournament-site-builds/api-gateway-server
cp /Users/mikeburns/Documents/tournament-site/netflix-zuul-api-gateway-server/target/netflix-zuul-api-gateway-server-0.0.1-SNAPSHOT.jar .
docker build . -t api-gateway-server

cd /Users/mikeburns/Documents/tournament-site-builds/player-service
cp /Users/mikeburns/Documents/tournament-site/limits-service/target/limits-service-0.0.1-SNAPSHOT.jar .
docker build . -t limits-service

cd /Users/mikeburns/Documents/tournament-site-builds/naming-service
cp /Users/mikeburns/Documents/tournament-site/netflix-eureka-naming-service/target/netflix-eureka-naming-service-0.0.1-SNAPSHOT.jar .
docker build . -t naming-service


