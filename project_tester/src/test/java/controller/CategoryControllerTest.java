package controller;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;

import config.RootContext;
import model.Category;

/**
 * @author John
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=RootContext.class)
@WebAppConfiguration //Der Test wird wie eine WebApp �ber das Dispatcher Servlet geleitet
public class CategoryControllerTest {

	@Autowired
	private WebApplicationContext wac;
	private MockMvc mockMvc;	
	private ObjectMapper mapper = new ObjectMapper();
	private String baseURL = "/category";
	private String urlWithId = "/category/{id}";
	
	@Before
	public void setup() {
		mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
	}
	
	@Test
	@Transactional
	public void testPersistGet() throws Exception {
		String categoryText = "FASHION";
		MockHttpServletRequestBuilder post = post(baseURL).content(categoryText).contentType(MediaType.APPLICATION_JSON);
		MvcResult postResult = mockMvc.perform(post).andReturn();
		long id = Long.parseLong(postResult.getResponse().getContentAsString());
		MockHttpServletRequestBuilder get = get(urlWithId, id);
		MvcResult getResult = mockMvc.perform(get).andReturn();
		
		Category retreivedCategory = mapper.readValue(getResult.getResponse().getContentAsString(), Category.class);
		assertEquals("Die ausgelesene Category entspricht nicht den Erwartungen!",categoryText, retreivedCategory.getText());
	}
	
	@Test
	@Transactional
	public void testDelete() throws Exception {
		String categoryText = "FASHION";
		MockHttpServletRequestBuilder post = post(baseURL).content(categoryText).contentType(MediaType.APPLICATION_JSON);
		MvcResult postResult = mockMvc.perform(post).andReturn();
		long id = Long.parseLong(postResult.getResponse().getContentAsString());
		MockHttpServletRequestBuilder delete = delete(urlWithId, id);
		mockMvc.perform(delete).andExpect(status().isOk());
		MockHttpServletRequestBuilder get = get(urlWithId, id);
		mockMvc.perform(get).andExpect(status().isNotFound());
	}
}