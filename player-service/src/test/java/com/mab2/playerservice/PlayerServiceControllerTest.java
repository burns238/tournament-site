package com.mab2.playerservice;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.joda.time.DateTimeUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@WebMvcTest(PlayerServiceController.class)
public class PlayerServiceControllerTest {

	@Autowired 
	private MockMvc mvc;
	
	@MockBean
	private PlayerRepository playerRepositoryMock;

    private SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss:SSS");

    @Before
    public void before() throws Exception {
        // define a fixed date-time
        Date fixedDateTime = DATE_FORMATTER.parse("01/07/2016 16:45:00:000");
        DateTimeUtils.setCurrentMillisFixed(fixedDateTime.getTime());
    }
 
    @After
    public void after() throws Exception {
        // Make sure to cleanup afterwards
        DateTimeUtils.setCurrentMillisSystem();
    }
	
	@Test
	public void test_retrieveAllPlayers_returnsEmptyListAs200() throws Exception {
	 
		Mockito.when(playerRepositoryMock.findAll()).thenReturn(List.of());
	    mvc.perform(get("/player/")
	    		.contentType(MediaType.APPLICATION_JSON))
	    		.andExpect(status().isOk())
	    		.andExpect(jsonPath("$", hasSize(0)));
		
	}
	
	@Test
	public void test_retrieveAllPlayers_returnsPopulatedListAs200() throws Exception {
	 
		Mockito.when(playerRepositoryMock.findAll()).thenReturn(
				List.of(new Player(), new Player(), new Player()));
	    mvc.perform(get("/player/")
	    		.contentType(MediaType.APPLICATION_JSON))
	    		.andExpect(status().isOk())
	    		.andExpect(jsonPath("$", hasSize(3)));
		
	}
	
	@Test
	public void test_retrieveAllPlayers_returnsPlayersNames() throws Exception {
	 
		Mockito.when(playerRepositoryMock.findAll()).thenReturn(List.of(new Player(1, "Name")));
	    mvc.perform(get("/player/")
	    		.contentType(MediaType.APPLICATION_JSON))
	    		.andExpect(status().isOk())
	    		.andExpect(jsonPath("$", hasSize(1)))
	    		.andExpect(jsonPath("$[0].name", is("Name")));
		
	}
	
	@Test
	public void test_retrievePlayer_returnsExistingPlayerAs200() throws Exception {
	 
		Mockito.when(playerRepositoryMock.findById(1)).thenReturn(Optional.of(new Player(1, "Name")));
	    mvc.perform(get("/player/1")
	    		.contentType(MediaType.APPLICATION_JSON))
	    		.andExpect(status().isOk())
	    		.andExpect(jsonPath("$.id", is(1)));
		
	}
	
	@Test
	public void test_retrievePlayer_nonExistingPlayerAs404() throws Exception {
	 
		Mockito.when(playerRepositoryMock.findById(1)).thenReturn(Optional.empty());
	    mvc.perform(get("/player/1")
	    		.contentType(MediaType.APPLICATION_JSON))
	    		.andExpect(status().isNotFound());
		
	}
	
	@Test
	public void test_retrievePlayer_nonExistingPlayerReturnsMessage() throws Exception {
	 
		String uri = "/player/1";
		Mockito.when(playerRepositoryMock.findById(1)).thenReturn(Optional.empty());
	    mvc.perform(get(uri)
	    		.contentType(MediaType.APPLICATION_JSON))
	    		.andExpect(jsonPath("$.errors[0]", is("player-1 not found")));
		
	}
	
	@Test
	public void test_retrievePlayer_nonExistingPlayerReturnsUri() throws Exception {
	 
		String uri = "/player/1";
		Mockito.when(playerRepositoryMock.findById(1)).thenReturn(Optional.empty());
	    mvc.perform(get(uri)
	    		.contentType(MediaType.APPLICATION_JSON))
	    		.andExpect(jsonPath("$.details", is("uri="+uri)));
		
	}
	
	@Test
	public void test_savePlayer_playerWithNullId_Returns200() throws Exception {
		
		var player = new Player();
		player.setName("Name");
		
		String uri = "/player";
		Mockito.when(playerRepositoryMock.save(player)).thenReturn(new Player(1, "Name"));
	    mvc.perform(post(uri)
	    		.content("{\"name\":\"Name\"}")
	    		.contentType(MediaType.APPLICATION_JSON))
	    		.andExpect(status().isCreated());
		
	}
	
	@Test
	public void test_savePlayer_playerWithNullId_returnsLocationOfGetInHeader() throws Exception {
	 
		var player = new Player();
		player.setName("Name");
		
		String uri = "/player";
		Mockito.when(playerRepositoryMock.save(player)).thenReturn(new Player(1, "Name"));
	    mvc.perform(post(uri)
	    		.content("{\"name\":\"Name\"}")
	    		.contentType(MediaType.APPLICATION_JSON))
	    		.andExpect(header().string("Location", "http://localhost/player/1"));
		
	}
	
	@Test
	public void test_savePlayer_playerWithExistingId_returns400() throws Exception {
	 
		var player = new Player();
		player.setId(1);
		player.setName("Name");
		
		String uri = "/player";
		Mockito.when(playerRepositoryMock.findById(1)).thenReturn(Optional.of(player));
	    mvc.perform(post(uri)
	    		.content("{\"id\":1,\"name\":\"Name\"}")
	    		.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest());
		
	}
	
}
