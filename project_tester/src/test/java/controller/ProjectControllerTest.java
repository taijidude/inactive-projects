
package controller;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.fileUpload;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.omg.CORBA.OMGVMCID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer.ContentTypeOptionsConfig;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import config.RootContext;
import model.Account;
import model.AccountIF;
import model.Answer;
import model.AnswerIF;
import model.Category;
import model.Feedback;
import model.FeedbackIF;
import model.Project;
import model.Upload;
import model.UploadIF;

/**
 * @author John
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=RootContext.class)
@WebAppConfiguration //Der Test wird wie eine WebApp �ber das Dispatcher Servlet geleitet
public class ProjectControllerTest {

	@Autowired
	private WebApplicationContext wac;

	private MockMvc mockMvc;	

	private Project project1;
	private Project project2;
	private Project project3;
	private ObjectMapper mapper = new ObjectMapper();

	private Calendar calendar;

	@Before
	public void setup() throws JsonProcessingException {
		mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();

		calendar = Calendar.getInstance();
		calendar.set(2016, 2, 2);

		project1 = project1 = new Project("Project1", 
				new HashSet<String>(Arrays.asList("cat1" ,"cat2")), 
				15, 
				"beschreibung1", 
				"innovation1", 
				"motivation1", 
				"belohnung1", 
				new HashSet<String>(Arrays.asList("ht1", "h2")));
		
		project2 = new Project("Project1", 
				new HashSet<String>(Arrays.asList("cat1" ,"cat2")), 
				15, 
				"beschreibung1", 
				"innovation1", 
				"motivation1", 
				"belohnung1", 
				new HashSet<String>(Arrays.asList("ht1", "h2")));
		
		project3  = new Project("Project1", 
				new HashSet<String>(Arrays.asList("cat1" ,"cat2")), 
				15, 
				"beschreibung1", 
				"innovation1", 
				"motivation1", 
				"belohnung1", 
				new HashSet<String>(Arrays.asList("ht1", "h2")));
	}
	
	@Test
	@Transactional
	public void testCreateGet() throws Exception {
		long accountId = postAccount();

		String projectAsJson = mapper.writeValueAsString(project1);
		MockHttpServletRequestBuilder post = post("/project").content(projectAsJson).contentType(MediaType.APPLICATION_JSON);
		MvcResult postResult = mockMvc.perform(post).andExpect(status().isOk()).andReturn();
		String id = postResult.getResponse().getContentAsString();
		project1.setId(Long.parseLong(id));
		MvcResult response = mockMvc.perform(get("/project/{id}", Long.parseLong(id))).andExpect(status().isOk()).andReturn();
		String responseText = response.getResponse().getContentAsString();
		Project retrievedProject = mapper.readValue(responseText, Project.class);
		assertEquals("Das ausgelesene Project entspricht nicht den Erwartungen!", project1, retrievedProject);
	}

	@Test
	@Transactional
	public void testReadAll() throws Exception {
		AccountIF account = new Account("firstname", "lastname","test@project_tester.de", "passwort");
		String accountAsJson = mapper.writeValueAsString(account);
		MockHttpServletRequestBuilder postAccount = post("/account").content(accountAsJson).contentType(MediaType.APPLICATION_JSON);
		MvcResult postAccountResult = mockMvc.perform(postAccount).andExpect(status().isOk()).andReturn();
		String accountId = postAccountResult.getResponse().getContentAsString();
		String project1AsJson = mapper.writeValueAsString(project1);
		String project2AsJson = mapper.writeValueAsString(project2);
		String project3AsJson = mapper.writeValueAsString(project3);
		MockHttpServletRequestBuilder post1 = post("/project").content(project1AsJson).contentType(MediaType.APPLICATION_JSON);
		MockHttpServletRequestBuilder post2 = post("/project").content(project2AsJson).contentType(MediaType.APPLICATION_JSON);
		MockHttpServletRequestBuilder post3 = post("/project").content(project3AsJson).contentType(MediaType.APPLICATION_JSON);

		MvcResult post1Result = mockMvc.perform(post1).andExpect(status().isOk()).andReturn();
		String p1Id = post1Result.getResponse().getContentAsString();
		project1.setId(Long.parseLong(p1Id));
		MvcResult post2Result = mockMvc.perform(post2).andExpect(status().isOk()).andReturn();
		String p2Id = post2Result.getResponse().getContentAsString();
		project2.setId(Long.parseLong(p2Id));
		MvcResult post3Result = mockMvc.perform(post3).andExpect(status().isOk()).andReturn();
		String p3Id = post3Result.getResponse().getContentAsString();
		project3.setId(Long.parseLong(p3Id));

		MvcResult response = mockMvc.perform(get("/project")).andReturn();
		String responseText = response.getResponse().getContentAsString();
		Project[] retrievedProjects = mapper.readValue(responseText, Project[].class);
		for (Project project : retrievedProjects) {
			if(!project.equals(project1)&&!project.equals(project2)&&!project.equals(project3)) {
				fail("Das Project entspricht keinem der erwarteten Projekte");
			}
		}
	}

	@Test
	public void testReadNonExisting() throws Exception {
		try {
			mockMvc.perform(get("/project"));
		} catch(HttpClientErrorException hcee) {
			assertEquals("Es wird ein 404 Fehler erwartet!","404 Not Found", hcee.getMessage());
		}
	}

	@Test
	public void testDeleteNonExisting() throws Exception {
		try {
			mockMvc.perform(delete("/project/{id}", 1));
		} catch(HttpClientErrorException hcee) {
			assertEquals("Es wird ein 404 Fehler erwartet!","404 Not Found", hcee.getMessage());
		}
	}

	@Test
	@Transactional
	public void testDelete() throws Exception {
		String project1AsJson = mapper.writeValueAsString(project1);
		System.out.println("ProjectControllerTest.testDelete()");
		MockHttpServletRequestBuilder post = post("/project").content(project1AsJson).contentType(MediaType.APPLICATION_JSON);
		mockMvc.perform(post);
		mockMvc.perform(delete("/project/{id}", project1.getId()));
		try {
			mockMvc.perform(get("/project/{id}", project1.getId()));
		} catch(HttpClientErrorException hcee) {
			assertEquals("Es wird ein 404 Fehler erwartet!","404 Not Found", hcee.getMessage());
		}
	}

	private long postAccount() throws Exception {
		AccountIF account = new Account("firstname","lastname", "test@project_tester.de", "passwort");
		String accountAsJson = mapper.writeValueAsString(account);
		MockHttpServletRequestBuilder postAccount = post("/account").content(accountAsJson).contentType(MediaType.APPLICATION_JSON);
		MvcResult postAccountResult = mockMvc.perform(postAccount).andExpect(status().isOk()).andReturn();
		return Long.parseLong(postAccountResult.getResponse().getContentAsString());
	}
	
	private String project1AsJson; 
	
	private long postProject(long accountId) throws Exception {
		project1AsJson = mapper.writeValueAsString(project1);
		MockHttpServletRequestBuilder postProject = post("/project").content(project1AsJson).contentType(MediaType.APPLICATION_JSON);
		MvcResult postResult = mockMvc.perform(postProject).andExpect(status().isOk()).andReturn();
		return Long.parseLong(postResult.getResponse().getContentAsString());
	}
	
	@Test
	@Transactional
	public void Update() throws Exception {
		long accountId = postAccount();
		long projectId = postProject(accountId);
		project1.setId(projectId);
		project1.setDescription("beschreibung15");
		project1AsJson = mapper.writeValueAsString(project1);
		mockMvc.perform(put("/project").content(project1AsJson).contentType(MediaType.APPLICATION_JSON));
		MvcResult response = mockMvc.perform(get("/project/{id}", project1.getId())).andReturn();
		String responseString = response.getResponse().getContentAsString();
		assertTrue("Es wird 'testCategory' als String in der ResponseEntity erwartet!", responseString.contains("beschreibung15"));
	}

	private long postHashtag(String text) throws Exception {
		MockHttpServletRequestBuilder post = post("/hashtag").content(text).contentType(MediaType.APPLICATION_JSON);
		MvcResult response = mockMvc.perform(post).andReturn();
		return Long.parseLong(response.getResponse().getContentAsString());
	}
	//test
	@Test
	@Transactional
	public void testAddHashTag() throws Exception {
		long hashtagId = postHashtag("#1234");
		long accountId = postAccount();
		long projectId = postProject(accountId);
		
		MockHttpServletRequestBuilder post = post("/project/{projectId}/hashtags/{hashtagId}", projectId, hashtagId);
		mockMvc.perform(post);
		
		MockHttpServletRequestBuilder get = get("/project/{projectId}", Long.valueOf(projectId));
		MvcResult getResult = mockMvc.perform(get).andReturn();
		String projectString = getResult.getResponse().getContentAsString();
		
		
		Project project = mapper.readValue(projectString, Project.class);
		Set<String> hashtags = project.getHashtags();
		assertFalse("Es werden hashtags erwartet!", hashtags.isEmpty());
		assertEquals("Die Id entspricht nicht den Erwartungen!","ht1",hashtags.iterator().next()); 
	}
	
	@Test
	@Transactional
	public void testRemoveHashtag() throws Exception {
		long hashtagId = postHashtag("#1234");
		long accountId = postAccount();
		long projectId = postProject(accountId);
		
		MockHttpServletRequestBuilder post = post("/project/{projectId}/hashtags/{hashtagId}", Long.valueOf(projectId), Long.valueOf(hashtagId));
		mockMvc.perform(post);
		
		MockHttpServletRequestBuilder get = get("/project/{projectId}", Long.valueOf(projectId));
		MvcResult getResult = mockMvc.perform(get).andReturn();
		String projectString = getResult.getResponse().getContentAsString();
		
		
		Project project = mapper.readValue(projectString, Project.class);
		Set<String> hashtags = project.getHashtags();
		assertFalse("Es werden hashtagIds erwartet!", hashtags.isEmpty());
		assertEquals("Die Id entspricht nicht den Erwartungen!","ht2",hashtags.iterator().next());
		
		//Hashtag l�schen 
		MockHttpServletRequestBuilder delete = delete("/project/{projectId}", Long.valueOf(projectId));
		mockMvc.perform(delete).andExpect(status().isOk());
		
		//hashtag erneut auslesen
		mockMvc.perform(get).andExpect(status().isNotFound());
	}
	
	private long postCategory(String categorytext) throws Exception {
		Category category = new Category(categorytext);
		String categoryAsJson = mapper.writeValueAsString(category);
		MockHttpServletRequestBuilder post = post("/category").content(categoryAsJson).contentType(MediaType.APPLICATION_JSON);
		MvcResult result = mockMvc.perform(post).andExpect(status().isOk()).andReturn();
		return Long.parseLong(result.getResponse().getContentAsString());
	}
	
	@Test
	@Transactional
	public void testAddCategory() throws Exception {
		long categoryId = postCategory("FASHION");
		long accountId = postAccount();
		long projectId = postProject(accountId);
		
		MockHttpServletRequestBuilder post = post("/project/{projectId}/categories/{categoryId}", projectId, categoryId);
		mockMvc.perform(post);
		
		MockHttpServletRequestBuilder get = get("/project/{projectId}", Long.valueOf(projectId));
		MvcResult getResult = mockMvc.perform(get).andReturn();
		String projectString = getResult.getResponse().getContentAsString();
		
		Project project = mapper.readValue(projectString, Project.class);
		Set<String> categories = project.getCategories();
		assertFalse("Es werden Kategorien erwartet!", categories.isEmpty());
		assertEquals("Die Id entspricht nicht den Erwartungen!", "cat1", categories.iterator().next());
	}
	
	private Project getProject(long projectId) throws Exception {
		MockHttpServletRequestBuilder get = get("/project/{projectId}", Long.valueOf(projectId));
		MvcResult getResult = mockMvc.perform(get).andReturn();
		String projectString = getResult.getResponse().getContentAsString();
		return mapper.readValue(projectString, Project.class);
	}
	
	@Test
	@Transactional
	public void testRemoveCategory() throws Exception {
		long categoryId = postCategory("FASHION");
		long accountId = postAccount();
		long projectId = postProject(accountId);
		
		MockHttpServletRequestBuilder post = post("/project/{projectId}/categories/{categoryId}", projectId, categoryId);
		mockMvc.perform(post);
		Project project = getProject(projectId);
		
		Set<String> categories = project.getCategories();
		assertFalse("Es werden Kategorien erwartet!", categories.isEmpty());
		assertEquals("Die Id entspricht nicht den Erwartungen!", "cat1", categories.iterator().next());
		
		//l�schen und versuchen neu aus zu lesen
		MockHttpServletRequestBuilder delete = delete("/project/{projectId}/categories/{categoryId}", projectId, categoryId);
		mockMvc.perform(delete);
		
		Project retrievedproject = getProject(projectId);
		categories = retrievedproject.getCategories();
		assertTrue("Es werden keine Kategorien erwartet!", categories.isEmpty());
	}
	
	private long postUpload(long accountId) throws Exception {
		File firstFileToUpload = new File("src/main/resources/china-information.jpg");
		assertTrue(firstFileToUpload.exists());
		File secondFileToUpload = new File("src/main/resources/test2.png");
		assertTrue(secondFileToUpload.exists());

		MockMultipartFile firstMultipartFile = new MockMultipartFile("files", 
				firstFileToUpload.getName(), 
				"multipart/form-data", Files.readAllBytes(firstFileToUpload.toPath()));
		
		MockMultipartHttpServletRequestBuilder upload = fileUpload("/upload/{userId}", 1, 2).file(firstMultipartFile);
		MvcResult response = mockMvc.perform(upload).andExpect(status().isOk()).andReturn();
		String responseContent = response.getResponse().getContentAsString();
		return Long.valueOf(responseContent.split(":")[0].replace("{", "").replace("\"", ""));
	}
	
	@Test
	@Transactional
	public void testAddUpload() throws Exception {
		long accountId = postAccount();
		long projectId = postProject(accountId);
		long uploadId = postUpload(accountId);
		MockHttpServletRequestBuilder post = post("/project/{projectId}/uploads/{uploadId}", projectId, uploadId);
		mockMvc.perform(post);
		Project retrievedProject = getProject(projectId);
		Set<UploadIF> retrievedUploadIds = retrievedProject.getUploads();
		assertEquals("Es sollte eine UploadId zugeordnet sein!",1,retrievedUploadIds.size());  
		assertEquals("Die Upload Id entspricht nicht den Erwartungen!",uploadId, retrievedUploadIds.iterator().next());
	}
	
	@Test
	@Transactional
	public void testRemoveUpload() throws Exception {
		long accountId = postAccount();
		long projectId = postProject(accountId);
		long uploadId = postUpload(accountId);
		MockHttpServletRequestBuilder post = post("/project/{projectId}/uploads/{uploadId}", projectId, uploadId);
		mockMvc.perform(post);
		MockHttpServletRequestBuilder delete = delete("/project/{projectId}/uploads/{uploadId}", projectId, uploadId);
		mockMvc.perform(delete).andExpect(status().isOk());
		Project retrievedProject = getProject(projectId);
		Set<UploadIF> retrievedUploadIds = retrievedProject.getUploads();
		assertTrue("Es sollten keine Uploads zugeordnet werden!", retrievedUploadIds.isEmpty());
	}
	
	private long postFeedback(long projectId, long accountId) throws Exception {
		String feedbacktext = "Das ist ein Testfeedback!";
		MockHttpServletRequestBuilder post = post("/project/{projectId}/feedback/{accountId}", projectId, accountId).content(feedbacktext);
		MvcResult response = mockMvc.perform(post).andReturn();
		String feedbackIdString = response.getResponse().getContentAsString();
		return Long.parseLong(feedbackIdString);
	}
	
	private void updateFeedback(FeedbackIF feedback) throws Exception {
		String feedbackAsJson = mapper.writeValueAsString(feedback);
		MockHttpServletRequestBuilder put = put("/feedback").content(feedbackAsJson).contentType(MediaType.APPLICATION_JSON);
		mockMvc.perform(put).andExpect(status().isOk());
	}
	
	@Test
	@Transactional
	public void testAddFeedback() throws Exception {
		long accountId = postAccount();
		long projectId = postProject(accountId);
		long feedbackId = postFeedback(projectId, accountId);
		Feedback feedback = getFeedback(feedbackId); 
		System.err.println(mapper.writeValueAsString(feedback));
		assertEquals("Der Text des ausgelesenen Feedbacks entspricht nicht den Erwartungen!", "Das ist ein Testfeedback!", feedback.getText());
	}
	
	@Test
	@Transactional
	public void testRemoveFeedback() throws Exception {
		long accountId = postAccount();
		long projectId = postProject(accountId);
		long feedbackId = postFeedback(projectId, accountId);
		MockHttpServletRequestBuilder delete = delete("/project/feedback/{feedbackId}", feedbackId);
		mockMvc.perform(delete);
		MockHttpServletRequestBuilder get = get("/project/feedback/{feedbackId}", feedbackId);
		mockMvc.perform(get).andExpect(status().isNotFound());
	}
	
	private Feedback getFeedback(long feedbackId) throws Exception {
		MockHttpServletRequestBuilder get = get("/feedback/{feedbackId}", feedbackId);
		MvcResult getResult = mockMvc.perform(get).andReturn();
		String contentAsString = getResult.getResponse().getContentAsString();
		return mapper.readValue(contentAsString, Feedback.class);
	}
	
	@Test
	@Transactional
	public void testUpdateFeedback() throws Exception {
		String updatedText = "das ist ein ver�nderter Beispieltext";
		long accountId = postAccount();
		long projectId = postProject(accountId);
		long feedbackId = postFeedback(projectId, accountId);
		Feedback feedback = getFeedback(feedbackId);
		feedback.setText(updatedText);
		updateFeedback(feedback);
		Feedback updatedFeedback = getFeedback(feedbackId);
		assertEquals("Der Text des Feedbacks entspricht nicht den Erwartungen!", updatedText, updatedFeedback.getText());
	}
	
	private long postAnswer(long feedbackId, long accountId, String antwortText) throws Exception {
		MockHttpServletRequestBuilder post = post("/feedback/{feedbackId}/answer/{accountId}", feedbackId, accountId).content(antwortText);
		MvcResult postResponse = mockMvc.perform(post).andReturn();
		String contentAsString = postResponse.getResponse().getContentAsString();
		return Long.parseLong(contentAsString);
	}
	
	@Test
	@Transactional
	public void testAddAnswer() throws Exception {
		String antwortText = "Antwort Text!";
		long accountId = postAccount();
		long projectId = postProject(accountId);
		long feedbackId = postFeedback(projectId, accountId);
		long answerId = postAnswer(feedbackId, accountId, antwortText); 		 
		AnswerIF retreivedAnswer = getAnswer(answerId);
		assertEquals("Der ausgelesene Text entspricht nicht Erwartungen!", antwortText, retreivedAnswer.getText()); 
	}
	
	private AnswerIF getAnswer(long answerId) throws Exception {
		MockHttpServletRequestBuilder get = get("/answer/{answerId}", answerId);
		MvcResult response = mockMvc.perform(get).andReturn();
		return mapper.readValue(response.getResponse().getContentAsString(), Answer.class);
	}

	@Test
	@Transactional
	public void testRemoveAnswer() throws Exception {
		long accountId = postAccount();
		long projectId = postProject(accountId);
		long feedbackId = postFeedback(projectId, accountId);
		long answerId = postAnswer(feedbackId, accountId, "Antwort Text!"); 		 
		MockHttpServletRequestBuilder delete = delete("/answer/{answerId}", answerId);
		mockMvc.perform(delete).andExpect(status().isOk());
		mockMvc.perform(get("/answer/{answerId}", answerId)).andExpect(status().isNotFound());
	}
	
	@Test
	@Transactional
	public void testUpdateAnswer() throws Exception {
		String newText = "updated Text!";
		long accountId = postAccount();
		long projectId = postProject(accountId);
		long feedbackId = postFeedback(projectId, accountId);
		long answerId = postAnswer(feedbackId, accountId, "Anwort Text!");
		AnswerIF retrivedAnswer1 = getAnswer(answerId);
		retrivedAnswer1.setText(newText); 
		String retrievedAnswer1AsJson = mapper.writeValueAsString(retrivedAnswer1);
		MockHttpServletRequestBuilder put = put("/answer").content(retrievedAnswer1AsJson).contentType(MediaType.APPLICATION_JSON);
		mockMvc.perform(put).andExpect(status().isOk());
		MockHttpServletRequestBuilder getAnswer = get("/answer/{answerId}", answerId);
		MvcResult getReturn = mockMvc.perform(getAnswer).andReturn();
		String answerString = getReturn.getResponse().getContentAsString();
		Answer answer = mapper.readValue(answerString, Answer.class);
		assertEquals("Der Text der ausgelesenen Antwort entspricht nicht den Erwartungen!", newText, answer.getText()); 
	}
	
	@Test
	@Transactional
	public void testAddMultipleProjectsAndGetThem() throws Exception {
		long accountId = postAccount();
		postProject(accountId);
		postProject(accountId);
		postProject(accountId);
		postProject(accountId);
		postProject(accountId);
		postProject(accountId);
		postProject(accountId);
		postProject(accountId);
		postProject(accountId);
		postProject(accountId);
		postProject(accountId);
		postProject(accountId);
		postProject(accountId);
		postProject(accountId);
		postProject(accountId);
		postProject(accountId);
		postProject(accountId);
		
		MvcResult response = mockMvc.perform(get("/project")).andReturn();
		String responseText = response.getResponse().getContentAsString();
		Project[] retrievedProjects = mapper.readValue(responseText, Project[].class);
		assertEquals("Es werden 17 Projekte erwartet!", 17, retrievedProjects.length);
		
		
	}
}