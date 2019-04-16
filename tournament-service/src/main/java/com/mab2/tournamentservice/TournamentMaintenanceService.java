package com.mab2.tournamentservice;

import java.util.UUID;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.msemys.esjc.EventData;
import com.github.msemys.esjc.EventStore;
import com.github.msemys.esjc.ExpectedVersion;
import com.github.msemys.esjc.ResolvedEvent;
import com.mab2.tournamentservice.events.CreateTournamentEvent;
import com.mab2.tournamentservice.events.DeleteTournamentEvent;
import com.mab2.tournamentservice.events.FinishTournamentEvent;
import com.mab2.tournamentservice.events.TournamentEvent;
import com.mab2.tournamentservice.utils.TournamentEventUtils;


@Service
public class TournamentMaintenanceService {

	@Autowired
	private EventStore eventstore;
	private static final ObjectMapper mapper = new ObjectMapper();
	private final String streamName = "tournaments";
	
	public UUID createTournament(Tournament tournament) throws JsonProcessingException {
		
		CreateTournamentEvent createTournamentEvent = 
				new CreateTournamentEvent(UUID.randomUUID(), 
						tournament.getName(), tournament.getType());  
		
		writeEvent(createTournamentEvent, CreateTournamentEvent.class.getName());
		
		return createTournamentEvent.getId();
		
	}
	
	public void deleteTournament(UUID id) throws JsonProcessingException {
		
		if (!tournamentWasCreated(id) || tournamentWasDeleted(id)) {
			throw new RuntimeException("That tournament don't exist");
		}
		
		writeEvent(new DeleteTournamentEvent(id), DeleteTournamentEvent.class.getName());
	}
	
	public void finishTournament(UUID id) throws JsonProcessingException {
		
		if (!tournamentWasCreated(id) || tournamentWasDeleted(id)) {
			throw new RuntimeException("That tournament don't exist");
		}
		
		writeEvent(new FinishTournamentEvent(id), FinishTournamentEvent.class.getName());
	}
	
	private void writeEvent(TournamentEvent event, String eventType) throws JsonProcessingException {
		eventstore.appendToStream(streamName, ExpectedVersion.ANY,
			    EventData.newBuilder()
			        .type(eventType)
			        .data(mapper.writeValueAsString(event))
			        .build()
			).thenAccept(r -> System.out.println(r.logPosition));
	}
	
	private Stream<ResolvedEvent> getEventsByTournamentId(UUID id) {
		return eventstore.streamEventsForward(streamName, 0, 4000, false)
				.filter(e -> TournamentEventUtils.tournamentEventMatchesId(id, e));
	}
	
	private Boolean tournamentWasCreated(UUID id) {
		return (getEventsByTournamentId(id)
				.anyMatch(e -> TournamentEventUtils.tournamentEventIsACreation(e)));
	}
	
	private Boolean tournamentWasDeleted(UUID id) {
		return (getEventsByTournamentId(id)
				.anyMatch(e -> TournamentEventUtils.tournamentEventIsADeletion(e)));
	}
	
	//addPlayers
	//removePlayers
	//postResult
	
}
