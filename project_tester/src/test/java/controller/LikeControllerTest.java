package controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.Calendar;
import java.util.HashSet;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import config.RootContext;
import dao.AccountDaoIF;
import dao.FeedbackDaoIF;
import dao.ProjectDaoIF;
import exception.DataAlreadyExistsException;
import exception.DataNotFoundException;
import model.Account;
import model.Answer;
import model.Feedback;
import model.Project;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=RootContext.class)
@WebAppConfiguration
public class LikeControllerTest {

	@Autowired
	private AccountDaoIF accountDao;
	
	@Autowired
	private FeedbackDaoIF feedbackDao;
	
	@Autowired
	private ProjectDaoIF projectDao;
	
	@Autowired
	private WebApplicationContext wac;

	private MockMvc mockMvc;

	private long userId;

	private long projectId;

	private long feedbackId;
	
	@Before
	public void setup() throws DataNotFoundException, DataAlreadyExistsException {
		mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();	
		userId = accountDao.persist(new Account("firstname", "lastname" ,"email", "passwort"));
		projectId = projectDao.persist(new Project("Project1", 
				new HashSet<String>(Arrays.asList("cat1" ,"cat2")), 
				15, 
				"beschreibung1", 
				"innovation1", 
				"motivation1", 
				"belohnung1", 
				new HashSet<String>(Arrays.asList("ht1", "h2"))));
	}
	
	@After
	public void teardown() throws DataNotFoundException {
		projectDao.delete(projectId);
		accountDao.delete(userId);
		
	}
	
	private long persistLike(String url, long itemId, long userId) throws Exception {
		MockHttpServletRequestBuilder post = post(url, itemId, userId);
		MvcResult postResponse = mockMvc.perform(post).andReturn();
		MockHttpServletResponse response = postResponse.getResponse();
		assertEquals("Der Status der Response sollte ok sein",HttpStatus.OK.value(),response.getStatus());
		String responseText = response.getContentAsString();
		return Long.parseLong(responseText);
	}
	
	private String getLike(String url, long itemId) throws Exception {
		MvcResult getResult = mockMvc.perform(get(url, itemId)).andReturn();
		return getResult.getResponse().getContentAsString();
	}
	
	@Test
	public void testPersistAndReadProjectLikes() throws Exception {
		long projectLikeId = persistLike("/like/project/{projectId}/{userId}", projectId, userId);  
		String getProjectLikeUrl = "/like/project/{projectId}";
		String retrievedJson = getLike(getProjectLikeUrl, projectId);
		assertFalse("Der String sollte nicht null oder leer sein!", retrievedJson.isEmpty());
		assertFalse("Es ist nur ein leeres JSON Array zurück gekommen", retrievedJson.equals("[]"));
		mockMvc.perform(delete("/like/{likeId}", projectLikeId)).andExpect(status().isOk());				
		mockMvc.perform(get(getProjectLikeUrl, projectLikeId)).andExpect(status().isNotFound());
	}
	
	@Test
	public void testPersistReadDeleteFeedbackLikes() throws Exception {
		feedbackId = feedbackDao.persistFeedback(new Feedback(projectId, userId, "text", Calendar.getInstance().getTime()));
		long feedbackLikeId = persistLike("/like/feedback/{feedbackId}/{userId}", feedbackId, userId);
		String getFeedbackLikeUrl = "/like/feedback/{feedbackId}";
		String contentAsString = getLike(getFeedbackLikeUrl, feedbackId); 
		assertFalse("Der String sollte nicht null oder leer sein!", contentAsString.isEmpty());
		assertFalse("Es ist nur ein leeres JSON Array zurück gekommen", contentAsString.equals("[]"));
		mockMvc.perform(delete("/like/{likeId}", feedbackLikeId));				
		mockMvc.perform(get(getFeedbackLikeUrl, feedbackLikeId)).andExpect(status().isNotFound());
		feedbackDao.deleteFeedback(feedbackId);
	}
	
	@Test
	public void testPersistReadDeleteAnswerLikes() throws Exception {
		feedbackId = feedbackDao.persistFeedback(new Feedback(projectId, userId, "text", Calendar.getInstance().getTime()));
		long answerId = feedbackDao.persistAnswer(new Answer(userId, feedbackId, "text", Calendar.getInstance().getTime()));; 
		long answerLikeId = persistLike("/like/answer/{answerId}/{userId}", answerId, userId);
		String getAnswerLikeUrl = "/like/answer/{answerId}";
		String contentAsString = getLike(getAnswerLikeUrl, answerId); 
		assertFalse("Der String sollte nicht null oder leer sein!", contentAsString.isEmpty());
		assertFalse("Es ist nur ein leeres JSON Array zurück gekommen", contentAsString.equals("[]"));
		mockMvc.perform(delete("/like/{likeId}", answerLikeId));				
		mockMvc.perform(get(getAnswerLikeUrl, answerLikeId)).andExpect(status().isNotFound());
		feedbackDao.deleteAnswer(answerId);
		feedbackDao.deleteFeedback(feedbackId);
	}
}