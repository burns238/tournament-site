package com.mab2.tournamentviewservice;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class TournamentViewController {

	@Autowired
	TournamentViewService service;
	
	@GetMapping(path="/tournament")
	public List<Tournament> getTournaments() throws IOException {
		service.rebuildTournamentsFromEventStore();
		return service.getTournaments();
	}
	
}

