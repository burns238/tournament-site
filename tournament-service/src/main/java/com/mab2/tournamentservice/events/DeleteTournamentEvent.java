package com.mab2.tournamentservice.events;

import java.util.UUID;

import lombok.Value;

@Value
public class DeleteTournamentEvent implements TournamentEvent {
	UUID id;
}
