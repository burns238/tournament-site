package com.mab2.tournamentservice;

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

@RestController
public class TournamentMaintenanceController {

	@Autowired
	TournamentMaintenanceService service;
	
	@PostMapping(path="/tournament")
	public ResponseEntity<UUID> createTournament(@RequestBody Tournament tournament)
		throws JsonProcessingException {
		
		UUID newId = service.createTournament(tournament);
		return new ResponseEntity<UUID>(newId, HttpStatus.CREATED);
	}
	
	@DeleteMapping(path="/tournament/{id}")
	public ResponseEntity<UUID> deleteTournament(@PathVariable UUID id)
		throws JsonProcessingException {
		
		service.deleteTournament(id);
		return ResponseEntity.ok().build();
	}
	
	@PostMapping(path="/tournament/{id}/finish")
	public ResponseEntity<UUID> finishTournament(@PathVariable UUID id)
		throws JsonProcessingException {
		
		service.finishTournament(id);
		return ResponseEntity.ok().build();
	}
}

