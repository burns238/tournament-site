package com.mab2.tournamentviewservice;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.msemys.esjc.EventStore;
import com.github.msemys.esjc.ResolvedEvent;

import lombok.extern.log4j.Log4j2;

import com.mab2.tournamentviewservice.events.AddPlayerEvent;
import com.mab2.tournamentviewservice.events.CreateTournamentEvent;
import com.mab2.tournamentviewservice.events.DeleteTournamentEvent;
import com.mab2.tournamentviewservice.events.FinishTournamentEvent;
import com.mab2.tournamentviewservice.events.PostResultEvent;

@Log4j2
@Service
public final class TournamentViewService {
	
	@Autowired
	private EventStore eventstore;
	
	private static final ObjectMapper mapper = new ObjectMapper();

	private static final String streamName = "tournaments";
	
	private Map<UUID, Tournament> tournaments = new HashMap<UUID,Tournament>();

	public List<Tournament> getTournaments() throws IOException {
		return new ArrayList<>(tournaments.values());
	}
	
	public void rebuildTournamentsFromEventStore() {
		tournaments.clear();
		eventstore.streamEventsForward(streamName, 0, 4000, false)
				.forEach(this::processTournamentEvent);
	}
	
	private void processTournamentEvent(ResolvedEvent event) {
		try {
			switch(event.originalEvent().eventType) {
				case "CreateTournamentEvent":
					CreateTournamentEvent createTournamentEvent = mapper.readValue(event.originalEvent().data, CreateTournamentEvent.class);
					tournaments.putIfAbsent(createTournamentEvent.getId(), 
							Tournament.getTournamentFromCreateEvent(createTournamentEvent, Date.from(event.originalEvent().created)));
					break;
				case "DeleteTournamentEvent":
					DeleteTournamentEvent deleteTournamentEvent = mapper.readValue(event.originalEvent().data, DeleteTournamentEvent.class);
					tournaments.remove(deleteTournamentEvent.getId());
					break;
				case "AddPlayerEvent":
					AddPlayerEvent addPlayerEvent = mapper.readValue(event.originalEvent().data, AddPlayerEvent.class);
					tournaments.put(addPlayerEvent.getId(), addPlayerToExistingTournament(addPlayerEvent.getId(), addPlayerEvent.getPlayerId()));
					break;
				case "PostResultEvent":
					PostResultEvent postResultEvent = mapper.readValue(event.originalEvent().data, PostResultEvent.class);
					tournaments.put(postResultEvent.getId(), 
							addResultToExistingTournament(postResultEvent.getId(), Result.getResultFromResultEvent(postResultEvent)));
					break;
				case "FinishTournamentEvent":
					FinishTournamentEvent finishTournamentEvent = mapper.readValue(event.originalEvent().data, FinishTournamentEvent.class);
					tournaments.put(finishTournamentEvent.getId(), finishExistingTournament(finishTournamentEvent.getId()));
					break;
			}
		} catch (IOException ex) {
			log.warn("Failed to parse json from event " + event.originalEventNumber());
		}
	}
	
	private Tournament addPlayerToExistingTournament(UUID id, Integer playerId) {
		Tournament existing = tournaments.get(id);
		var newPlayersList = new ArrayList<Integer>(existing.getPlayers());
		newPlayersList.add(playerId);
		Tournament proposed = new Tournament(existing.getId(), existing.getName(), existing.getType(),
				newPlayersList, existing.getResults(), existing.getCreated(), existing.getFinished());
		return proposed;
	}
	
	private Tournament addResultToExistingTournament(UUID id, Result result) {
		Tournament existing = tournaments.get(id);
		var newResultsList = new ArrayList<Result>(existing.getResults());
		newResultsList.add(result);
		Tournament proposed = new Tournament(existing.getId(), existing.getName(), existing.getType(),
				existing.getPlayers(), newResultsList, existing.getCreated(), existing.getFinished());
		return proposed;
	}
	
	private Tournament finishExistingTournament(UUID id) {
		Tournament existing = tournaments.get(id);
		Tournament proposed = new Tournament(existing.getId(), existing.getName(), existing.getType(),
				existing.getPlayers(), existing.getResults(), existing.getCreated(), true);
		return proposed;
	}

}
