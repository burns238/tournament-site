package com.mab2.tournamentservice.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateTournamentEvent implements TournamentEvent {
	UUID id;
    String name;
    String type;
}
