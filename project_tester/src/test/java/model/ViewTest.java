package model;

import static org.junit.Assert.*;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import model.view.ViewAccount;
import model.view.ViewFeedBack;
import model.view.ViewFeedbackIF;
import model.view.ViewProject;
import model.view.ViewSpot;
import model.view.ViewSpotIF;
import model.view.ViewUpload;
import model.view.ViewUploadIF;

public class ViewTest {

	private ObjectMapper mapper = new ObjectMapper();
	private ViewAccount account;
	private ViewSpotIF spot;

	@Before
	public void setup() {
		account = new ViewAccount(1,"lutz", "mustermann", new ViewUpload(1, "/uploads/1.png"));
		spot = new ViewSpot("berlin",
				"mein Bett",
				"am liebsten jetzt gleich", "Winkelgasse", 12);
		
	}
	
	@Test
	public void createViewSpot() throws JsonProcessingException {
		String spotAsJson = mapper.writeValueAsString(spot);
	}
	
	@Test
	public void testCreateAccount() throws Exception {
		account.setCity("berlin");
		account.setJob("designer");
		String writeValueAsString = new ObjectMapper().writeValueAsString(account);
		System.err.println(writeValueAsString);
		assertTrue("'designer' sollte im json zu finden sein!'", writeValueAsString.contains("designer"));
		assertTrue("'berlin' sollte im json zu finden sein!'", writeValueAsString.contains("berlin"));
	}
	
	@Test
	public void testCreateFeedback() throws Exception {

		ViewFeedBack feedBack = new ViewFeedBack(1, OffsetDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME), account, "Ich finde die Idee GEILO!", 22, true);
		System.out.println(mapper.writeValueAsString(feedBack));
	}
	
	@Test
	public void testCreateProject() throws JsonProcessingException {
		account.setCity("berlin");
		account.setJob("designer");
		
		ViewProject viewProject = new ViewProject(1, 
				OffsetDateTime.now().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME), 
				OffsetDateTime.now().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME), 
				34, 
				"das ist meine Idee!", 
				"Das ist meine Innnovation!", 
				"das ist meine motivation",
				"das ist meine belohnung", 
				"published", 
				spot,
				15, "Test Viewproject", account);

		Set<String> categories = new HashSet<String>();
		categories.add("Social");
		categories.add("Fashion");
		viewProject.setCategories(categories);

		Set<String> hashtags = new HashSet<String>();
		hashtags.add("Geil");
		hashtags.add("und Bombe");
		viewProject.setHashtags(hashtags);
		
		List<ViewUploadIF> images = new ArrayList<ViewUploadIF>();
		images.add(new ViewUpload(4, "uploads/abc1.png"));
		images.add(new ViewUpload(2, "uploads/abc1.png"));
		images.add(new ViewUpload(1, "uploads/abc1.png"));
		images.add(new ViewUpload(3, "uploads/abc1.png"));
		images.stream().sorted((i1, i2) -> i1.getPosition().compareTo(i2.getPosition()));
		viewProject.setImages(images);
		
		List<ViewFeedbackIF> feedback = new ArrayList<ViewFeedbackIF>();
		ViewFeedbackIF f1 = new ViewFeedBack(1,
				OffsetDateTime.now().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME), 
				new ViewAccount(1, "Jan", "Cosinus", new ViewUpload(1, "uploads/pic1.png")),
				"Ich finde die Idee GEILO!", 22, true);
		feedback.add(f1);
		viewProject.setFeedback(feedback);
		System.err.println(mapper.writeValueAsString(viewProject));
	}
	
	@Test
	public void testCreateProjectList() throws Exception {

		account.setCity("berlin");
		account.setJob("designer");
        ViewProject viewProject = new ViewProject(1,OffsetDateTime.now().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME), 
				OffsetDateTime.now().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME), 
				34, new ViewSpot("berlin"), "Test Viewproject", account);
        
        List<ViewUploadIF> images = new ArrayList<ViewUploadIF>();
		images.add(new ViewUpload(4, "uploads/abc1.png"));
		images.add(new ViewUpload(2, "uploads/abc1.png"));
		images.add(new ViewUpload(1, "uploads/abc1.png"));
		images.add(new ViewUpload(3, "uploads/abc1.png"));
		images.stream().sorted((i1, i2) -> i1.getPosition().compareTo(i2.getPosition()));
		viewProject.setImages(images);
		Set<String> categories = new HashSet<String>();
		categories.add("Social");
		categories.add("Fashion");
		viewProject.setCategories(categories);
        System.err.println(mapper.writeValueAsString(viewProject));
	}
}