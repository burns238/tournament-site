package com.mab2.tournamentservice;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.msemys.esjc.EventData;
import com.github.msemys.esjc.EventStore;
import com.github.msemys.esjc.ExpectedVersion;
import com.github.msemys.esjc.ResolvedEvent;
import com.mab2.tournamentservice.events.AddPlayerEvent;
import com.mab2.tournamentservice.events.CreateTournamentEvent;
import com.mab2.tournamentservice.events.DeleteTournamentEvent;
import com.mab2.tournamentservice.events.TournamentEvent;
import com.mab2.tournamentservice.proxy.PlayerServiceProxy;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public final class TournamentMaintenanceService {
	
	@Autowired
	private PlayerServiceProxy proxy;

	@Autowired
	private EventStore eventstore;

	private static final ObjectMapper mapper = new ObjectMapper();
	
	private static final String streamName = "tournaments";
	
	public void writeEvent(TournamentEvent event, String eventType) throws JsonProcessingException {
		eventstore.appendToStream(streamName, ExpectedVersion.ANY,
			    EventData.newBuilder()
			        .type(eventType)
			        .jsonData(mapper.writeValueAsString(event))
			        .build()
			).thenAccept(r -> System.out.println(r.logPosition));
	}
	
	public void writeAddPlayerEvents(UUID tournamentId, List<Integer> playerIds) {
		eventstore.appendToStream(streamName, ExpectedVersion.ANY,
				playerIds.parallelStream()
					.map(p -> buildAddPlayerEvent(tournamentId, p))
					.collect(Collectors.toList())
			).thenAccept(r -> System.out.println(r.logPosition));
	}
	
	public Stream<ResolvedEvent> getEventsByTournamentId(UUID id) {
		return eventstore.streamEventsForward(streamName, 0, 4000, false)
				.filter(e -> tournamentEventMatchesId(id, e));
	}
	
	public Boolean tournamentWasCreated(UUID id) {
		return (getEventsByTournamentId(id)
				.anyMatch(e -> tournamentEventIsACreation(e)));
	}
	
	public Boolean tournamentWasDeleted(UUID id) {
		return (getEventsByTournamentId(id)
				.anyMatch(e -> tournamentEventIsADeletion(e)));
	}
	
	public Boolean tournamentDoesntExistOrIsDeleted(UUID id) {
		return (!tournamentWasCreated(id) || tournamentWasDeleted(id));
	}
	
	public Boolean tournamentEventIsACreation(ResolvedEvent event) {
		return tournamentEventIsOfType(event, CreateTournamentEvent.class.getName());
	}
	
	public static Boolean tournamentEventIsADeletion(ResolvedEvent event) {
		return tournamentEventIsOfType(event, DeleteTournamentEvent.class.getName());
	}
	
	public static Boolean tournamentEventIsAnAddPlayer(ResolvedEvent event) {
		return tournamentEventIsOfType(event, AddPlayerEvent.class.getName());
	}
	
	public static Boolean tournamentEventIsOfType(ResolvedEvent event, String type) {
		return event.originalEvent().eventType.contentEquals(type);
	}
	
	public static Boolean tournamentEventMatchesId(UUID id, ResolvedEvent event) {
		try {
			var jsonEvent = new String(event.originalEvent().data);
			var jsonObject = new JSONObject(jsonEvent);
			return (jsonObject.has("id") && jsonObject.getString("id").equals(id.toString())); 
		} catch (Exception e) {
			log.warn("Failed to parse json from event " + event.toString());
			return false;
		}
	}

	private static EventData buildAddPlayerEvent(UUID tournamentId, Integer playerId) {
		try {
			return EventData.newBuilder()
					.type(AddPlayerEvent.class.getName())
					.jsonData(mapper.writeValueAsString(new AddPlayerEvent(tournamentId, playerId)))
					.build();
		} catch (JsonProcessingException e) {
			throw new RuntimeException("Json processing failed. T.Id = "+tournamentId+", P.Id = "+playerId);
		}
	}
	
	public Boolean playerAddedToTournament(UUID tournamentId, Integer playerId) {
		return getEventsByTournamentId(tournamentId)
				.filter(e -> tournamentEventIsAnAddPlayer(e))
				.anyMatch(e -> tournamentEventContainsPlayerId(e, playerId));
	}
	
	public static Boolean tournamentEventContainsPlayerId(ResolvedEvent event, Integer playerId) {
		try {
			var jsonEvent = new String(event.originalEvent().data);
			var jsonObject = new JSONObject(jsonEvent);
			return (jsonObject.has("playerId") && jsonObject.getInt("playerId") == playerId); 
		} catch (Exception e) {
			log.warn("Failed to parse json from event " + event.toString());
			return false;
		}
	}

	public List<String> validationErrorsForPlayerList(UUID tournamentId, List<Integer> playerIds) {
		Stream<String> notFoundStream = playerIds.parallelStream()
								.filter(p -> proxy.retrievePlayer(p).isEmpty())
								.map(p -> p.toString() + " not found");
		
		Stream<String> alreadyAddedStream = playerIds.stream()
								.filter(p -> playerAddedToTournament(tournamentId, p))
								.map(p -> p.toString() + " already exists in tournament");
					
		return Stream.concat(notFoundStream, alreadyAddedStream)
					.collect(Collectors.toList());
	}

	public Boolean tournamentContainsResultPlayers(UUID tournamentId, Result result) {
		return getEventsByTournamentId(tournamentId)
				   .filter(e -> tournamentEventIsAnAddPlayer(e))
		           .filter(e -> tournamentEventContainsPlayerId(e, result.getPlayer1Id()) 
		        		   || tournamentEventContainsPlayerId(e, result.getPlayer2Id()))
		           .distinct()
		           .limit(2)
		           .count() == 2;
	}

}
