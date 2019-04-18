package com.mab2.tournamentservice;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
	
	public Set<Integer> getPlayersInResult() {
		return this.playerResults.stream()
					.map(p -> p.getPlayerId())
					.collect(Collectors.toSet());
	}
	
	public List<Integer> getPlayersInResultAsList() {
		return this.playerResults.stream()
					.map(p -> p.getPlayerId())
					.collect(Collectors.toList());
	}
	
	public Boolean equalsPlayers(Result that) {
		return this.getPlayersInResult().equals(that.getPlayersInResult());
	}
	
	public Boolean containsDuplicatePlayers() {
		List<Integer> playersInResult = getPlayersInResultAsList();
		return getPlayersInResult()
				.stream()
				.anyMatch(i -> Collections.frequency(playersInResult, i) > 1);
	}
}
