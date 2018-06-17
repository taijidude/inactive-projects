package dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import config.DaoConfig;
import exception.DataAlreadyExistsException;
import exception.DataNotFoundException;
import model.Account;
import model.Category;
import model.Project;
import model.ProjectIF;
import model.UploadIF;
import model.entity.HashtagEntity;
import model.entity.ProjectEntity;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=DaoConfig.class)
public class ProjectDaoTest {

	@Autowired
	private HashtagDaoIF hashtagDao;
	
	@Autowired
	private AccountDaoIF accountDao;
	
	@Autowired
	private ProjectDaoIF projectDao;
	
	@Autowired
	private CategoryDaoIF categoryDao;
	
	@Autowired
	private UploadDaoIF uploadDao;
	
	private ProjectIF project1;

	private Account account;
	
	private Calendar calendar;

	@Before
	public void setup() {
		account = new Account("lutz","mustermann","test@test-mail.com","x");
		calendar = Calendar.getInstance();
		calendar.set(2016, 2, 2, 8, 8, 8);
		project1 = new Project("Project1", 
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
	public void testCreateGet() throws DataNotFoundException, DataAlreadyExistsException {
		accountDao.persist(account);
		long id = projectDao.persist(project1);
		ProjectIF retrievedProject = projectDao.get(id);
		assertEquals("Das ausgelesene Project entspricht nicht den Erwartungen!", project1, retrievedProject);
	}
	
	@Test
	@Transactional
	public void testGetNoneExisting() throws DataNotFoundException {
		try {
			projectDao.get(1);
			fail("DataNotFoundException erwartet!");
		} catch (DataNotFoundException e) {}
	}
		
	@Test
	@Transactional
	public void testGetAll() throws Exception {
		long accountId = accountDao.persist(account);
		
		project1 = new Project("Project1", 
				new HashSet<String>(Arrays.asList("cat1" ,"cat2")), 
				15, 
				"beschreibung1", 
				"innovation1", 
				"motivation1", 
				"belohnung1", 
				new HashSet<String>(Arrays.asList("ht1", "h2")));
		
		Project project2 = new Project("Project2", 
				new HashSet<String>(Arrays.asList("cat1" ,"cat2")), 
				15, 
				"beschreibung2", 
				"innovation2", 
				"motivation2", 
				"belohnung2", 
				new HashSet<String>(Arrays.asList("ht1", "h2")));
		
		Project project3 = new Project("Project3", 
				new HashSet<String>(Arrays.asList("cat1" ,"cat2")), 
				15, 
				"beschreibung3", 
				"innovation3", 
				"motivation3", 
				"belohnung3", 
				new HashSet<String>(Arrays.asList("ht1", "h2")));
		
		long p1Id = projectDao.persist(project1);
		project1.setId(p1Id);
		long p2Id = projectDao.persist(project2);
		project2.setId(p2Id);
		long p3Id = projectDao.persist(project3);
		project3.setId(p3Id);
		List<ProjectIF> allProjects = projectDao.getAll();
		assertFalse("Die Projectliste sollte nicht leer sein!", allProjects.isEmpty());
		assertTrue("project1 befindet sich nicht in der Liste!", allProjects.contains(project1));
		assertTrue("project2 befindet sich nicht in der Liste!", allProjects.contains(project2));
		assertTrue("project3 befindet sich nicht in der Liste!", allProjects.contains(project3));
	}
	
	@Test
	@Transactional
	public void testDelete() throws DataNotFoundException, DataAlreadyExistsException {
		long accountId = accountDao.persist(account);
		long id = projectDao.persist(project1);
		ProjectIF projectRetrieved = projectDao.get(id);
		assertNotNull("Das project sollte nicht null sein!", projectRetrieved);
		assertEquals("Es wird das Project mit der Id 1 erwartet!", id, projectRetrieved.getId());
		projectDao.delete(projectRetrieved.getId());
		try {
			projectDao.get(id);
			fail("DataNotFoundException erwartet!");
		} catch(DataNotFoundException exception) {}
	}
	
	@Test
	@Transactional
	public void testUpdate() throws DataNotFoundException, DataAlreadyExistsException {
		long accountId = accountDao.persist(account);
		long id = projectDao.persist(project1);
		assertEquals("Es wird die Beschreibung beschreibung1 erwartet!", "beschreibung1", projectDao.get(id).getDescription());
		project1.setId(id);
		((Project)project1).setDescription("beschreibung15");
		projectDao.update(project1);
		assertEquals("Es wird die Beschreibung beschreibung15 erwartet!", "beschreibung15", projectDao.get(id).getDescription());
	}
	
	@Test
	@Transactional
	public void testAssignHashtagToProject() throws Exception {
		String hashtagText = "#123456";
		long accountId = accountDao.persist(account);
		assertTrue("Das Set sollte leer sein!",project1.getHashtags().isEmpty());
		long id = projectDao.persist(project1);
		long hashtagId = hashtagDao.persist(hashtagText);
		projectDao.addHashtag(id,hashtagId);
		ProjectIF retrievedProject = projectDao.get(id);
		Set<String> hashtags = retrievedProject.getHashtags();
		assertFalse("Das Set darf nicht leer sein!", hashtags.isEmpty());
		assertEquals("Es wird eine HashtagId erwartet!", 1, hashtags.size());
		
		HashtagEntity hashtagEntity = hashtagDao.getEntityById(hashtagId);
		assertEquals("Der hashtagText entspricht nicht den Erwartungen!", hashtagText, hashtagEntity.getText());
	}
	
	@Test
	@Transactional
	public void testAssignCategoryToProject() throws Exception {
		long accountId = accountDao.persist(account);
		long projectId = projectDao.persist(project1);
		long categoryId = categoryDao.persist(new Category("Fashion"));
		projectDao.addCategory(projectId, categoryId);
		ProjectIF retrievedProject = projectDao.get(projectId);
		assertEquals("Es sollte eine Category Id zugeordnet werden!", 1, retrievedProject.getCategories().size()); 
	}
	
	@Test
	@Transactional
	public void testRemoveCategory() throws Exception {
		long accountId = accountDao.persist(account);
		long projectId = projectDao.persist(project1);
		long categoryId = categoryDao.persist(new Category("Fashion"));
		projectDao.addCategory(projectId, categoryId);
		ProjectIF retrievedProject = projectDao.get(projectId);
		assertEquals("Es sollte eine Category Id zugeordnet werden!", 1, retrievedProject.getCategories().size());
		projectDao.removeCategory(projectId, categoryId);
		retrievedProject = projectDao.get(projectId);
		assertTrue("Es sollte keine Kategorien mehr geben!", retrievedProject.getCategories().isEmpty());
	}
	
	@Test
	@Transactional
	public void testAddUpload() throws Exception {
		long uploadId = uploadDao.writeDataToDatabase("test.txt", 1);
		long accountId = accountDao.persist(account);
		long projectId = projectDao.persist(project1);
		projectDao.addUpload(projectId, uploadId);
		ProjectIF retrievedProject = projectDao.get(projectId);
		Set<UploadIF> uploadIds = retrievedProject.getUploads();
		assertFalse("Es werden Uploads erwartet!", uploadIds.isEmpty());
		assertEquals("Es wird die UploadId aus diesem Test erwartet!", uploadId, uploadIds.iterator().next());
	}
	
	@Test
	@Transactional
	public void testRemoveUpload() throws Exception {
		long uploadId = uploadDao.writeDataToDatabase("test.txt", 1);
		long accountId = accountDao.persist(account);
		long projectId = projectDao.persist(project1);
		projectDao.addUpload(projectId, uploadId);
		ProjectIF retrievedProject = projectDao.get(projectId);
		Set<UploadIF> uploadIds = retrievedProject.getUploads();
		assertFalse("Es werden Uploads erwartet!", uploadIds.isEmpty());
		assertEquals("Es wird die UploadId aus diesem Test erwartet!", uploadId, uploadIds.iterator().next());
		projectDao.removeUpload(projectId, uploadId);
	}
	
	@Test
	@Transactional
	public void testEntityToJson() throws Exception {
		long accountId = accountDao.persist(new Account("firstname","lastname","test@test-mail.com", "password"));
		long id = projectDao.persist(project1);
		
		ProjectEntity projectEntity = projectDao.getEntityById(id);
		ObjectMapper objectMapper = new ObjectMapper();
		
		String writeValueAsString = objectMapper.writeValueAsString(projectEntity);
		System.err.println(writeValueAsString);
		
		
	}
	
}