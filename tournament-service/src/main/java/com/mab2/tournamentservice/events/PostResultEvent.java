package com.mab2.tournamentservice.events;

import lombok.Value;

import java.util.UUID;

@Value
public class PostResultEvent implements TournamentEvent {
	
	UUID id;
	
	Integer player1Id;
	
	Integer player1Wins;
	
	Integer player2Id;
	
	Integer player2Wins;
	
}
