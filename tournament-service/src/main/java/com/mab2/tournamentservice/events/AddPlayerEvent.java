package com.mab2.tournamentservice.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddPlayerEvent implements TournamentEvent {
	UUID id;
	Integer playerId;
}
