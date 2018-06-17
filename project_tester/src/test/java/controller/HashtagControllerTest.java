package controller;

import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import java.util.HashSet;

import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import config.RootContext;
import model.Hashtag;

/**
 * TODO: Es sollten mehrere Hashtags gleichzeitig anlegen lassen.
 * 
 * @author John
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=RootContext.class)
@WebAppConfiguration 
public class HashtagControllerTest {

	@Autowired
	private WebApplicationContext wac;

	private MockMvc mockMvc;
	
	private String hashtagUrl = "/hashtag";
	private String hashtagUrlId = hashtagUrl+"/{id}";
	
	@Before
	public void setup() {
		mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();	
	}
	
	@Test
	@Transactional 
	public void testGetHashtagById() throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		MockHttpServletRequestBuilder post = post(hashtagUrl).content("#hashtag");
		MvcResult postResponse = mockMvc.perform(post).andReturn();
		String responseText = postResponse.getResponse().getContentAsString();
		MockHttpServletRequestBuilder get = get(hashtagUrlId, Long.parseLong(responseText));
		MvcResult getResponse = mockMvc.perform(get).andReturn();
		String getResponseText = getResponse.getResponse().getContentAsString();
		Hashtag hashtag = mapper.readValue(getResponseText, Hashtag.class);
		assertTrue("#hashtag sollte im Response Text enthalten sein!", hashtag.getText().equals("#hashtag"));
	}
	
	@Test
	@Transactional
	public void testDeleteHashTag() throws Exception {
		MockHttpServletRequestBuilder post = post(hashtagUrl).content("#hashtag");
		MvcResult result = mockMvc.perform(post).andReturn();
		String id = result.getResponse().getContentAsString();
		MockHttpServletRequestBuilder delete = delete(hashtagUrlId, Long.parseLong(id));
		mockMvc.perform(delete);
		MockHttpServletRequestBuilder get = get(hashtagUrlId, Long.parseLong(id));
		MvcResult getResult = mockMvc.perform(get).andReturn();
		String responseText = getResult.getResponse().getContentAsString();
		assertTrue("Hier sollte nichts zur�ck kommen!", responseText == null || responseText.isEmpty()); 
	}
	
	@Test
	@Transactional
	public void testGetAll() throws Exception {
		String h1 = "#hashtag1";
		String h2 = "#hashtag2";
		MockHttpServletRequestBuilder post = post(hashtagUrl).content(h1);
		mockMvc.perform(post);
		post = post(hashtagUrl).content(h2);
		mockMvc.perform(post);
		
		MockHttpServletRequestBuilder get = get(hashtagUrl);
		MvcResult response = mockMvc.perform(get).andReturn();
		String contentAsString = response.getResponse().getContentAsString();
		ObjectMapper mapper = new ObjectMapper();
		HashSet<String> retrievedTexts = new HashSet<String>();
		Hashtag[] hashtags = mapper.readValue(contentAsString, Hashtag[].class);
		for (Hashtag hashtag : hashtags) {
			retrievedTexts.add(hashtag.getText());
		}
		assertTrue("#hashtag1 sollte enthalten sein!", retrievedTexts.contains(h1));
		assertTrue("#hashtag2 sollte enthalten sein!", retrievedTexts.contains(h2));
		
	}
}