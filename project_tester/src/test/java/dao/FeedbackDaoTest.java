package dao;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
import model.AccountIF;
import model.Answer;
import model.AnswerIF;
import model.Feedback;
import model.FeedbackIF;
import model.Project;
import model.ProjectIF;
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=DaoConfig.class)
public class FeedbackDaoTest {
	
	@Autowired
	private AccountDaoIF accountDao;
	
	@Autowired
	private FeedbackDaoIF feedbackDao;
	
	@Autowired
	private ProjectDaoIF projectDao;

	private AccountIF account;

	private ProjectIF project;
	
	private long persistAccount() throws DataAlreadyExistsException {
		account = new Account("lutz","mustermann","test@test-mail.com","password");
		return accountDao.persist(account);	
	}
	
	private long persistProject(long accountId) throws DataNotFoundException {
		Calendar calendar = Calendar.getInstance();
		project = new Project("Project1", 
				new HashSet<String>(Arrays.asList("cat1" ,"cat2")), 
				15, 
				"beschreibung1", 
				"innovation1", 
				"motivation1", 
				"belohnung1", 
				new HashSet<String>(Arrays.asList("ht1", "h2")));
		return projectDao.persist(project);
	}
	
	private long persistFeedback(long projectId, long accountId) throws DataNotFoundException {
		FeedbackIF feedback = new Feedback(0,projectId, accountId, "Das ist ein Beispiel Feedback!",null);
		return feedbackDao.persistFeedback(feedback);
	}
	
	@Test
	@Transactional
	public void testPersistFeedback() throws DataNotFoundException, DataAlreadyExistsException {
		long accountId = persistAccount();
		long projectId = persistProject(accountId);
		long feedbackId = persistFeedback(projectId, accountId);
		assertTrue("Die zur�ck gelieferte ID darf nicht 0 sein!", feedbackId > 0);
		FeedbackIF retrievedFeedback = feedbackDao.getFeedback(feedbackId);
		assertEquals("Die AccountId des ausgelesenen Feedbacks entspricht nicht den Erwartungen!", accountId, retrievedFeedback.getAccountId());
		assertEquals("Die ProjectId des ausgelesenen Feedbacks entspricht nicht den Erwartungen!", projectId, retrievedFeedback.getProjectId());
	}
	
	@Test
	@Transactional
	public void testDeleteFeedback() throws Exception {
		long accountId = persistAccount();
		long projectId = persistProject(accountId);
		long feedbackId = persistFeedback(projectId, accountId);
		long answerId = persistAnswer(accountId, feedbackId, "test");
		feedbackDao.deleteFeedback(feedbackId);
		try {
			feedbackDao.getFeedback(feedbackId);
			fail("Exception erwartet!");
		} catch (DataNotFoundException e) {}
		try {
			feedbackDao.getAnswer(answerId);
			fail("Exception erwartet!");
		} catch (DataNotFoundException e) {}
		ProjectIF project = projectDao.get(projectId);
		Set<FeedbackIF> feedbackIds = project.getFeedback();
		assertFalse("Die Id des gel�schten Feedbacks sollte sich nicht mehr in in der FeedbackId Liste stehen", feedbackIds.contains(feedbackId));
	}
	
	private long persistAnswer(long accountId, long feedbackId, String text) throws DataNotFoundException {
		Answer answer = new Answer(0,accountId,feedbackId,"Das ist eine Textantwort", null);
		return feedbackDao.persistAnswer(answer);
	}
	
	@Test
	@Transactional
	public void testPersistAnswer() throws Exception {
		long accountId = persistAccount();
		long projectId = persistProject(accountId);
		long feedbackId = persistFeedback(projectId, accountId);
		long answerId = persistAnswer(accountId, feedbackId, "Das ist eine Textantwort");
		AnswerIF retrievedAnswer = feedbackDao.getAnswer(answerId);
		assertNotNull(retrievedAnswer);
		assertEquals("Die FeedbackId entspricht nicht den Erwartungen!", retrievedAnswer.getFeedbackId(), feedbackId);
	}
}