package com.mab2.tournamentviewservice.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostResultEvent implements TournamentEvent {
	UUID id;
	Integer player1Id;
	Integer player1Wins;
	Integer player2Id;
	Integer player2Wins;
}
