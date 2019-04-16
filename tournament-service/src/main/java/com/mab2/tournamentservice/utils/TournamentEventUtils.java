package com.mab2.tournamentservice.utils;

import java.util.UUID;

import org.json.JSONObject;

import com.github.msemys.esjc.ResolvedEvent;
import com.mab2.tournamentservice.events.CreateTournamentEvent;
import com.mab2.tournamentservice.events.DeleteTournamentEvent;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class TournamentEventUtils {

	public static Boolean tournamentEventIsACreation(ResolvedEvent event) {
		String type = event.originalEvent().eventType;
		return (type.equals(CreateTournamentEvent.class.getName()));
	}
	
	public static Boolean tournamentEventIsADeletion(ResolvedEvent event) {
		String type = event.originalEvent().eventType;
		return (type.equals(DeleteTournamentEvent.class.getName()));
	}
	
	public static Boolean tournamentEventMatchesId(UUID id, ResolvedEvent event) {
		String jsonEvent = new String(event.originalEvent().data);
		try {
			JSONObject jsonObject = new JSONObject(jsonEvent);
			return (jsonObject.has("id") && jsonObject.getString("id").equals(id.toString())); 
		} catch (Exception e) {
			log.warn("Failed to parse json from event " + event.toString());
			return false;
		}
	}
}
