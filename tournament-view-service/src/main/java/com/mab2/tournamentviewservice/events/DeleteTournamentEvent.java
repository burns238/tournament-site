package com.mab2.tournamentviewservice.events;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeleteTournamentEvent implements TournamentEvent {
	UUID id;
}
