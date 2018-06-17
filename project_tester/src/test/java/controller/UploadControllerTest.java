package controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.fileUpload;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.File;
import java.nio.file.Files;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import config.RootContext;
import dao.UploadDaoIF;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=RootContext.class) 
@WebAppConfiguration //Der Test wird wie eine WebApp über das Dispatcher Servlet geleitet
public class UploadControllerTest {

	/*
	 * Ich will in einem Text das Uploadverzeichnis kontrollieren.
	 * Deshalb injecte ich hier das Dao 
	 */
	@Autowired
	private UploadDaoIF dao;

	@Autowired
	private WebApplicationContext wac;

	private MockMvc mockMvc;

	@Before
	public void setup() {
		mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
	}

	@Test
	@Transactional
	public void testSendSingleProjectImage() throws Exception {
		File fileToUpload = new File("src/main/resources/china-information.jpg");
		assertTrue(fileToUpload.exists());
		MockMultipartFile multipartFile = new MockMultipartFile("files", /*Das hier muss der identisch sein, mit dem request Parameternamen im Controller*/
				fileToUpload.getName(), 
				"multipart/form-data", 
				Files.readAllBytes(fileToUpload.toPath()));
		MvcResult response = mockMvc.perform(fileUpload("/upload/{userId}", 1, 2).file(multipartFile)).andExpect(status().isOk()).andReturn();
		String responseContent = response.getResponse().getContentAsString();
		assertTrue("Der Dateiname china-information.jpg sollte enthalten sein!" , responseContent.contains("china-information.jpg"));
	}

	@Test
	@Transactional
	public void testSendMultipleProjectImages() throws Exception {
		File firstFileToUpload = new File("src/main/resources/china-information.jpg");
		assertTrue(firstFileToUpload.exists());
		File secondFileToUpload = new File("src/main/resources/test2.png");
		assertTrue(secondFileToUpload.exists());

		MockMultipartFile firstMultipartFile = new MockMultipartFile("files", 
				firstFileToUpload.getName(), 
				"multipart/form-data", 
				Files.readAllBytes(firstFileToUpload.toPath()));

		MockMultipartFile secondMultipartFile = new MockMultipartFile("files", 
				secondFileToUpload.getName(), 
				"multipart/form-data",
				Files.readAllBytes(secondFileToUpload.toPath()));
		MvcResult response = mockMvc.perform(fileUpload("/upload/{userId}", 1, 2).file(firstMultipartFile).file(secondMultipartFile))
				.andExpect(status().isOk()).andReturn();

		String responseContent = response.getResponse().getContentAsString();
		assertTrue("Der Dateiname china-information.jpg sollte enthalten sein!" , responseContent.contains("china-information.jpg"));
		assertTrue("Der Dateiname test2.jpg sollte enthalten sein!" , responseContent.contains("test2.png"));
	}


	/**
	 * Läd eine einzelne Datei hoch und versucht dann über die zurückgegebene
	 * ID, diese hochgeladene Datei zu löschen. 
	 * @throws Exception
	 */
	@Test
	@Transactional
	public void testDeleteFile() throws Exception {
		File uploadsDir = dao.getUploadsDir();
		File[] listFiles = uploadsDir.listFiles();
		for (File file : listFiles) {
			file.delete();
		}
		assertEquals("Das Upload Verzeichnis soll leer sein!", 0, uploadsDir.listFiles().length);  
		File firstFileToUpload = new File("src/main/resources/china-information.jpg");
		assertTrue(firstFileToUpload.exists());
		MockMultipartFile firstMultipartFile = new MockMultipartFile("files", 
				firstFileToUpload.getName(), 
				"multipart/form-data", 
				Files.readAllBytes(firstFileToUpload.toPath()));

		MvcResult response = mockMvc.perform(fileUpload("/upload/{userId}", 1, 2).file(firstMultipartFile))
				.andExpect(status().isOk()).andReturn();
		String responseContent = response.getResponse().getContentAsString();
		String idString = responseContent.split(":")[0];
		idString = idString.replace("\"", "").replace("{", "");
		assertTrue("Es wird eine Datei erwartet!", findFileById(idString, uploadsDir));

		int imageId = Integer.parseInt(idString);
		mockMvc.perform(delete("/upload/{imageId}",imageId)).andExpect(status().isOk());
		assertFalse("Die Datei wurde nicht gelöscht!", findFileById(idString, uploadsDir));
	}
	/**
	 * Hochgeladene Dateien bekommen eine Id. Mit dieser Id sollte sich der relative auslesen. 
	 * @throws Exception
	 */
	@Test
	@Transactional
	public void testGetFilePathById() throws Exception {
		File firstFileToUpload = new File("src/main/resources/china-information.jpg");
		assertTrue(firstFileToUpload.exists());
		MockMultipartFile firstMultipartFile = new MockMultipartFile("files", 
				firstFileToUpload.getName(), 
				"multipart/form-data", 
				Files.readAllBytes(firstFileToUpload.toPath()));

		MvcResult response = mockMvc.perform(fileUpload("/upload/{userId}", 1, 2).file(firstMultipartFile))
				.andExpect(status().isOk()).andReturn();
		String responseContent = response.getResponse().getContentAsString();
		String idString = responseContent.split(":")[0].replace("\"", "").replace("{", "");

		int imageId = Integer.parseInt(idString);
		MvcResult pathResponse = mockMvc.perform(get("/upload/path/{imageId}", imageId))
				.andExpect(status().isOk()).andReturn();

		String pathResponseText = pathResponse.getResponse().getContentAsString();
		boolean containPath = pathResponseText.contains("uploads\\"+imageId+".jpg");
		assertTrue("Die erwartete Pfadangabe wurde nicht gefunden!", containPath);
	}

	private boolean findFileById(String id, File uploadsDir) {
		File[] listFiles = uploadsDir.listFiles();
		for (File file : listFiles) {
			if(file.getName().equals(id+".jpg")) {
				return true;
			}
		}
		return false;
	}
}