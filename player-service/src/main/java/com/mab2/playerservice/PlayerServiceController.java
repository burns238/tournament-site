package com.mab2.playerservice;

import java.net.URI;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.mab2.playerservice.exception.ExistingEntityException;
import com.mab2.playerservice.exception.NotFoundException;

@RestController
public class PlayerServiceController {
	
	@Autowired
	private PlayerRepository playerRepository;
	
	@GetMapping(path="/player")
	public List<Player> retrieveAllPlayers() {
		return playerRepository.findAll();
	}
	
	@GetMapping(path="/player/{id}")
	public Player retrievePlayer(@PathVariable int id) {
		return playerRepository.findById(id)
				.orElseThrow(() -> new NotFoundException("player-"+id));
	}
	
	@PostMapping(path="/player")
	public ResponseEntity<Player> savePlayer(@Valid @RequestBody Player player) {
		if (player.getId() != null && playerRepository.findById(player.getId()).isPresent()) {
			throw new ExistingEntityException("player-"+player.getId());
		}
		
		Player savedPlayer = playerRepository.save(player);
		URI location = ServletUriComponentsBuilder
			.fromCurrentRequest()
			.path("/{id}")
			.buildAndExpand(savedPlayer.getId())
			.toUri();
		return ResponseEntity.created(location).build();
	}
	
	@DeleteMapping(path="/player/{id}")
	public void deletePlayer(@PathVariable int id) {
		int retrievedId = playerRepository.findById(id)
			.orElseThrow(() -> new NotFoundException("player-"+id))
			.getId();
		playerRepository.deleteById(retrievedId);
	}
	
}

