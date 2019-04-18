package com.mab2.tournamentviewservice;

import com.mab2.tournamentviewservice.events.PostResultEvent;

import lombok.Value;

@Value
public class Result {
	Integer player1Id;
	Integer player1Wins;
	Integer player2Id;
	Integer player2Wins;
	
	public static Result getResultFromResultEvent(PostResultEvent postResultEvent) {
		return new Result(postResultEvent.getPlayer1Id(), postResultEvent.getPlayer1Wins(),
				postResultEvent.getPlayer2Id(), postResultEvent.getPlayer2Wins());
	}
}
