# tournament-site

A very basic tournament API for me to give spring boot, microservices, and event sourcing a quick spin.

Needs rabbitmq, zipkin, mysql, and eventstore running (I'll containerise it soon).

Naming Service: 8761
Api Gateway Server: 8765
Player Service: 8100
Tournament Service: 8200

# Example endpoints

Post a player:
POST http://localhost:8765/player-service/player
{
	"name":"Jace"
}

Get players:
GET http://localhost:8765/player-service/player

Get a player:
GET http://localhost:8765/player-service/player/{id}

Post a tournament:
POST http://localhost:8765/tournament-service/tournament
{
	"name":"tournament1",
	"type":"some type"
}

Delete a tournament:
DELETE http://localhost:8765/tournament-service/tournament/{id}

Add players to a tournament:
POST http://localhost:8765/tournament-service/tournament/{id}/players/
[
  1,
  2,
  5,
  6,
  7
]

Post results:
PUT http://localhost:8765/tournament-service/tournament/{id}/result/
{
	"player1Id":1,
	"player1Wins":1,
	"player2Id":2,
	"player2Wins":2
}
