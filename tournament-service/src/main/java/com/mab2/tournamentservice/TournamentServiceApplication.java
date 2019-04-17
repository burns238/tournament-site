package com.mab2.tournamentservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients("com.mab2.tournamentservice")
@EnableDiscoveryClient
public class TournamentServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(TournamentServiceApplication.class, args);
	}

}
