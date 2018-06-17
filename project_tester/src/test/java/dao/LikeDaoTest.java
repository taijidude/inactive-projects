package dao;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;

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
import model.LikeItemTyp;
import model.Project;
import model.Account;
import model.Answer;
import model.Feedback;
import model.Like;
import model.LikeIF;
import model.entity.AnswerLikeEntity;
import model.entity.FeedbackLikeEntity;
import model.entity.LikeEntity;
import model.entity.ProjectLikeEntity;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=DaoConfig.class)
public class LikeDaoTest {

	@Autowired
	private LikeDaoIF likeDao;

	@Autowired
	private AccountDaoIF accountDao; 

	@Autowired
	private FeedbackDaoIF feedbackDao;

	@Autowired
	private ProjectDaoIF projectDao;

	private long accountId1;

	private long projectId;

	private long projectId1;

	private long feedbackId;

	private long answerId;

	private long accountId2;

	private long accountId3;

	private long projectId2;

	@Before
	public void setup() throws DataNotFoundException, DataAlreadyExistsException {
		accountId1 = accountDao.persist(new Account("firstname", "lastname", "test@test.de", "passwort!"));
		accountId2 = accountDao.persist(new Account("firstname", "lastname", "test@test.de", "passwort!"));
		accountId3 = accountDao.persist(new Account("firstname", "lastname", "test@test.de", "passwort!"));

		projectId = projectDao.persist(new Project("Project1", 
				new HashSet<String>(Arrays.asList("cat1" ,"cat2")), 
				15, 
				"beschreibung1", 
				"innovation1", 
				"motivation1", 
				"belohnung1", 
				new HashSet<String>(Arrays.asList("ht1", "h2"))));

		projectId1 = projectDao.persist(new Project("Project2", 
				new HashSet<String>(Arrays.asList("cat1" ,"cat2")), 
				15, 
				"beschreibung2", 
				"innovation2", 
				"motivation2", 
				"belohnung2", 
				new HashSet<String>(Arrays.asList("ht1", "h2"))));

		projectId2 = projectDao.persist(new Project("Project3", 
				new HashSet<String>(Arrays.asList("cat1" ,"cat2")), 
				15, 
				"beschreibung3", 
				"innovation3", 
				"motivation3", 
				"belohnung3", 
				new HashSet<String>(Arrays.asList("ht1", "h2"))));

		feedbackId = feedbackDao.persistFeedback(new Feedback(projectId1, accountId1, "text", Calendar.getInstance().getTime()));
		answerId = feedbackDao.persistAnswer(new Answer(accountId1, feedbackId, "text", Calendar.getInstance().getTime()));
	}

	@Test
	@Transactional
	public void testSaveAndGetProjectLike() throws DataNotFoundException {
		LikeIF like = new Like(LikeItemTyp.PROJECT,accountId1,projectId1);
		long likeId = likeDao.persistLike(like);
		LikeEntity retrievedLike = likeDao.getEntityLikeById(likeId);
		assertEquals("Die UserId entspricht nicht den Erwartungen!", accountId1, retrievedLike.getAccount().getId());
		assertEquals("Die ProjectId entspricht nicht den Erwartungen!", projectId1, ((ProjectLikeEntity)retrievedLike).getProject().getId());
	}

	@Test
	@Transactional
	public void testSaveAndGetFeedbackLike() throws Exception {
		LikeIF like = new Like(LikeItemTyp.FEEDBACK,accountId1,feedbackId);
		long likeId = likeDao.persistLike(like);
		LikeEntity retrievedLike = likeDao.getEntityLikeById(likeId);
		assertEquals("Die UserId entspricht nicht den Erwartungen!", accountId1, retrievedLike.getAccount().getId());
		assertEquals("Die FeedbackId entspricht nicht den Erwartungen!", feedbackId, ((FeedbackLikeEntity) retrievedLike).getFeedback().getId());
	}

	@Test
	@Transactional
	public void testSaveAndGetAnswerLike() throws Exception {
		LikeIF like = new Like(LikeItemTyp.ANSWER,accountId1,answerId);
		long answerLikeId = likeDao.persistLike(like);
		LikeEntity retrievedLike = likeDao.getEntityLikeById(answerLikeId);
		assertEquals("Die UserId entspricht nicht den Erwartungen!", accountId1, retrievedLike.getAccount().getId());
		assertEquals("Die FeedbackId entspricht nicht den Erwartungen!", answerId, ((AnswerLikeEntity)retrievedLike).getAnswer().getId());
	}

	@Test
	@Transactional
	public void testDeleteProjectLike() throws Exception {
		long likeId = likeDao.persistLike(new Like(LikeItemTyp.PROJECT,accountId1,projectId1));
		long feedbackLikeId = likeDao.persistLike(new Like(LikeItemTyp.FEEDBACK,accountId1,feedbackId));
		long answerLikeId = likeDao.persistLike(new Like(LikeItemTyp.ANSWER,accountId1,answerId));
		likeDao.deleteLikeById(likeId);
		try {
			likeDao.getEntityLikeById(likeId);
			fail("DataNotFoundException erwartet!");
		} catch (DataNotFoundException e) {}

		assertNotNull("Das FeedbackLike Object sollte noch in der Datenbank vorhanden sein!", likeDao.getEntityLikeById(feedbackLikeId)); 
		assertNotNull("Das AnswerLike Object sollte noch in der Datenbank vorhanden sein!", likeDao.getEntityLikeById(answerLikeId)); 
	}

	@Test
	@Transactional
	public void testGetProjectLikesByProject() throws Exception {
		Like pl1 = new Like(LikeItemTyp.PROJECT,accountId1,projectId);
		Like pl2 = new Like(LikeItemTyp.PROJECT,accountId2,projectId1);
		Like pl3 = new Like(LikeItemTyp.PROJECT,accountId3,projectId1);

		likeDao.persistLike(pl1);
		likeDao.persistLike(pl2);
		likeDao.persistLike(pl3);

		List<LikeIF> retrievedList = likeDao.getLikesById(projectId1, LikeItemTyp.PROJECT);
		assertEquals("Es werden zwei Likes erwartet!", 2, retrievedList.size());
		//TODO: Hier muss noch weiter ausformuliert werden!
	}

	@Test
	@Transactional
	public void testGetProjectLikesByUser() throws Exception {
		Like pl1 = new Like(LikeItemTyp.PROJECT, accountId1,projectId);
		Like pl2 = new Like(LikeItemTyp.PROJECT, accountId1,projectId1);
		Like pl3 = new Like(LikeItemTyp.PROJECT, accountId1,projectId2);

		likeDao.persistLike(pl1);
		likeDao.persistLike(pl2);
		likeDao.persistLike(pl3);

		List<LikeIF> retrievedList = likeDao.getLikesByUserId(accountId1);
		assertEquals("Es werden drei Likes erwartet!", 3, retrievedList.size());
	}

	@Test
	@Transactional
	public void testAlreadyExists() throws Exception {
		Like pl1 = new Like(LikeItemTyp.PROJECT,accountId1,projectId);
		likeDao.persistLike(pl1);
		assertTrue("Der projectLike pl1 sollte bereits existierend!", likeDao.alreadyExists(pl1));
	}
}