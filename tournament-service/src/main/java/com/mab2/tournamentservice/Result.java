package com.mab2.tournamentservice;

import java.util.Date;

import lombok.Value;

@Value
public class Result {
	Integer player1Id;
	Integer player1Wins;
	Integer player2Id;
	Integer player2Wins;
}
