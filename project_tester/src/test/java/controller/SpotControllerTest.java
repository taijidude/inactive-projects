package controller;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import config.RootContext;
import model.Spot;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=RootContext.class)
@WebAppConfiguration
public class SpotControllerTest {

	@Autowired
	private WebApplicationContext wac;

	private ObjectMapper mapper = new ObjectMapper();
	
	private MockMvc mockMvc;

	private Spot spot;

	private String urlToken = "/v1/api/spots/token";
	
	private String urlFree = "/v1/api/spots/free";
	
	private String urlId = urlToken+"/{id}";

	private String spotAsJson;
	
	@Before
	public void setup() throws JsonProcessingException {
		mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
		spot = new Spot(
				"PARTNER",
				"TestSpot 1",
				"Ein Testspot...",
				"Lindenstraße",
				"3",
				"3-----",
				"berlin", 
				"14 Uhr Dienstags");
		spotAsJson = mapper.writeValueAsString(spot);
	}
	
	private long persistSpot(String spotAsJson) throws Exception {
		MockHttpServletRequestBuilder postSpot = post(urlToken).content(spotAsJson).contentType(MediaType.APPLICATION_JSON);
		MvcResult mvcResult = mockMvc.perform(postSpot).andExpect(status().isOk()).andReturn();
		String contentAsString = mvcResult.getResponse().getContentAsString();
		return Long.parseLong(contentAsString);
	}
	
	@Test
	@Transactional
	public void testPersistGet() throws Exception {	
		long createdId = persistSpot(spotAsJson);
		MockHttpServletRequestBuilder get = get(urlId, createdId);
		MvcResult mvcResult = mockMvc.perform(get).andExpect(status().isOk()).andReturn();
		Spot retrievedSpot = mapper.readValue(mvcResult.getResponse().getContentAsString(), Spot.class);
		assertEquals("Die beiden Spots sollten sich nicht unterscheiden!", spot, retrievedSpot);
	}
	
	@Test
	@Transactional
	public void testdelete() throws Exception {
		long createdId = persistSpot(spotAsJson);
		MockHttpServletRequestBuilder delete = delete(urlId, createdId);
		mockMvc.perform(delete).andExpect(status().isOk());
		MockHttpServletRequestBuilder get = get(urlId, createdId);
		mockMvc.perform(get).andExpect(status().isNotFound());	
	}
	
	@Test
	@Transactional
	public void testGetAll() throws Exception {
		long spot1Id = persistSpot(spotAsJson);
		MockHttpServletRequestBuilder get = get(urlToken);
		MvcResult mvcResult = mockMvc.perform(get).andExpect(status().isOk()).andReturn();
		String contentAsString = mvcResult.getResponse().getContentAsString();		
		List<Spot> readValue = mapper.readValue(contentAsString, List.class);
		assertEquals("Es sollte ein Spot in der Liste sein!", 1, readValue.size());
		get = get(urlFree);
		mvcResult = mockMvc.perform(get).andExpect(status().isOk()).andReturn();
		contentAsString = mvcResult.getResponse().getContentAsString();
		assertFalse("Der Begriff visitTimes sollte nicht vorkommen!", contentAsString.contains("visitTimes"));
	}
	
	@Test
	public void testUpdate() throws Exception {
		long id = persistSpot(spotAsJson);
		spot.setId(id);
		spot.setConcept("CONCEPT");
		String updatedJsonString = mapper.writeValueAsString(spot);
		MockHttpServletRequestBuilder put = put(urlToken).content(updatedJsonString).contentType(MediaType.APPLICATION_JSON);
		mockMvc.perform(put).andExpect(status().isOk());
		MockHttpServletRequestBuilder get = get(urlToken+"/{id}", id);
		MvcResult mvcResult = mockMvc.perform(get).andExpect(status().isOk()).andReturn();
		Spot updatedSpot = mapper.readValue(mvcResult.getResponse().getContentAsString(), Spot.class);
		assertEquals("Es wird der Concept Text 'CONCEPT erwartet'", "CONCEPT", updatedSpot.getConcept());
		delete(urlId, id);
	}	
}