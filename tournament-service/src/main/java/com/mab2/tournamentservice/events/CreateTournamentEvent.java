package com.mab2.tournamentservice.events;

import lombok.Value;
import java.util.UUID;

@Value
public class CreateTournamentEvent implements TournamentEvent {
	UUID id;
    String name;
    String type;
}
