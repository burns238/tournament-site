package com.mab2.tournamentservice;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mab2.tournamentservice.events.CreateTournamentEvent;
import com.mab2.tournamentservice.events.DeleteTournamentEvent;
import com.mab2.tournamentservice.events.FinishTournamentEvent;
import com.mab2.tournamentservice.events.PostResultEvent;
import com.mab2.tournamentservice.exception.BadRequestException;
import com.mab2.tournamentservice.exception.NotFoundException;

@RestController
public class TournamentMaintenanceController {

	@Autowired
	TournamentMaintenanceService service;
	
	@PostMapping(path="/tournament")
	public ResponseEntity<UUID> createTournament(@RequestBody Tournament tournament) throws JsonProcessingException {
		CreateTournamentEvent createTournamentEvent = 
				new CreateTournamentEvent(UUID.randomUUID(), 
						tournament.getName(), tournament.getType());  
		
		service.writeEvent(createTournamentEvent, CreateTournamentEvent.class.getName());
		return new ResponseEntity<UUID>(createTournamentEvent.getId(), HttpStatus.CREATED);
		
	}
	
	@DeleteMapping(path="/tournament/{id}")
	public ResponseEntity<UUID> deleteTournament(@PathVariable UUID id) throws JsonProcessingException {
		if (service.tournamentDoesntExistOrIsDeleted(id)) {
			throw new NotFoundException("Tournament " + id);
		}
		service.writeEvent(new DeleteTournamentEvent(id), DeleteTournamentEvent.class.getName());
		return ResponseEntity.ok().build();
	}
	
	@PostMapping(path="/tournament/{id}/finish")
	public ResponseEntity<UUID> finishTournament(@PathVariable UUID id) throws JsonProcessingException {
		if (service.tournamentDoesntExistOrIsDeleted(id)) {
			throw new NotFoundException("Tournament " + id);
		}
		service.writeEvent(new FinishTournamentEvent(id), FinishTournamentEvent.class.getName());
		return ResponseEntity.ok().build();
	}
	
	@PostMapping(path="/tournament/{id}/players")
	public ResponseEntity<UUID> addPlayersToTournament(@PathVariable UUID id, @RequestBody List<Integer> players) 
			throws JsonProcessingException, BadRequestException {
		if (service.tournamentDoesntExistOrIsDeleted(id)) {
			throw new NotFoundException("Tournament " + id);
		}
		List<String> errors = service.validationErrorsForPlayerList(id, players);
		if (errors.isEmpty()) {
			service.writeAddPlayerEvents(id, players);
		} else {
			throw new BadRequestException(errors); 
		}
		return ResponseEntity.ok().build();
	}
	
	@PostMapping(path="/tournament/{id}/result")
	public ResponseEntity<UUID> postResult(@PathVariable UUID id, @RequestBody Result result) throws JsonProcessingException, BadRequestException {
		if (service.tournamentDoesntExistOrIsDeleted(id)) {
			throw new NotFoundException("Tournament " + id); 
		} else if (!service.tournamentContainsResultPlayers(id, result)) {
			throw new BadRequestException("All players for this result must have been added to the tournament"); 
		}
		PostResultEvent postResultEvent = 
				new PostResultEvent(id, result.getPlayer1Id(), result.getPlayer1Wins(),
						result.getPlayer2Id(), result.getPlayer2Wins());  
		service.writeEvent(postResultEvent, PostResultEvent.class.getName());
		return ResponseEntity.ok().build();
	}
}

