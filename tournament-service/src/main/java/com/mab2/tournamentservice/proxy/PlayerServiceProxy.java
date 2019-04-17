package com.mab2.tournamentservice.proxy;

import java.util.Optional;

import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.mab2.tournamentservice.Player;

@FeignClient(name="netflix-zuul-api-gateway-server", decode404 = true)
@RibbonClient(name="player-service")
public interface PlayerServiceProxy {

	@GetMapping("/player-service/player/{id}")
	public Optional<Player> retrievePlayer(@PathVariable("id") Integer id); 
		
}
