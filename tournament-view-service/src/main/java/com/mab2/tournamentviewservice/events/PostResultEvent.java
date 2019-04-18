package com.mab2.tournamentviewservice.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

import com.mab2.tournamentviewservice.PlayerResult;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostResultEvent implements TournamentEvent {
	UUID id;
	List<PlayerResult> playerResults;
}
