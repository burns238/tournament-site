package com.mab2.tournamentviewservice;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import com.mab2.tournamentviewservice.events.CreateTournamentEvent;

import lombok.Value;

@Value
public class Tournament {
    UUID id;
	String name;
    String type;
    List<Integer> players;
    List<Result> results;
    Date created;
    Boolean finished;
    
    public static Tournament getTournamentFromCreateEvent(CreateTournamentEvent event, Date creation) {
		return new Tournament(event.getId(), event.getName(), event.getType(), 
						List.of() , List.of(), creation ,false);
	}
    
}
