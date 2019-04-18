package com.mab2.tournamentservice;

import java.util.List;

import com.mab2.tournamentservice.events.PostResultEvent;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result {
	List<PlayerResult> playerResults;
	
	public static Result getResultFromResultEvent(PostResultEvent postResultEvent) {
		return new Result(postResultEvent.getPlayerResults());
	}
}
