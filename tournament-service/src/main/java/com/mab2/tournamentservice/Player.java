package com.mab2.tournamentservice;

import java.util.Date;

import lombok.Value;

@Value
public class Player {
	private Integer id;
	
	private String name;
	
	private Date created;
}
