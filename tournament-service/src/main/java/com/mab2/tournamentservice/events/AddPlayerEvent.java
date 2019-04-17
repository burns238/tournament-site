package com.mab2.tournamentservice.events;

import lombok.Value;
import java.util.UUID;

@Value
public class AddPlayerEvent implements TournamentEvent {
	UUID id;
	Integer playerId;
}
