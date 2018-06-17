package dao;

import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import exception.DataNotFoundException;
import model.Answer;
import model.AnswerIF;
import model.Feedback;
import model.FeedbackIF;
import model.entity.AccountEntity;
import model.entity.AnswerEntity;
import model.entity.FeedbackEntity;
import model.entity.ProjectEntity;

@Repository
public class FeedbackDao implements FeedbackDaoIF {

	@Autowired
	private ProjectDaoIF projectDao;

	@Autowired
	private AccountDaoIF accountDao;

	@PersistenceContext
	private EntityManager em;

	@Override
	@Transactional
	public long persistFeedback(FeedbackIF feedback) throws DataNotFoundException {
		AccountEntity accountEntity = accountDao.getEntityById(feedback.getAccountId());
		ProjectEntity projectEntity = projectDao.getEntityById(feedback.getProjectId());
		FeedbackEntity feedbackEntity = new FeedbackEntity(accountEntity, projectEntity, feedback.getText(), feedback.getCreationDate());
		em.persist(feedbackEntity);
		return feedbackEntity.getId();
	}

	@Override
	@Transactional
	public FeedbackIF getFeedback(long feedbackId) throws DataNotFoundException {
		FeedbackEntity feedbackEntity = getFeedbackEntityById(feedbackId);
		return new Feedback(feedbackEntity.getId(),feedbackEntity.getProjectEntity().getId(), feedbackEntity.getAccountEntity().getId(), feedbackEntity.getText(), feedbackEntity.getCreationDate());
	}

	@Override
	@Transactional
	public FeedbackEntity getFeedbackEntityById(long id) throws DataNotFoundException {
		Query query = em.createQuery("from FeedbackEntity ae where ae.id = :parameter");
		query.setParameter("parameter", id);
		try {
			return (FeedbackEntity) query.getSingleResult();
		} catch (NoResultException nre) {
			throw new DataNotFoundException();
		}	
	}

	@Override
	@Transactional
	public long persistAnswer(AnswerIF answer) throws DataNotFoundException {
		AccountEntity accountEntity = accountDao.getEntityById(answer.getAccountId());
		FeedbackEntity feedbackEntity = getFeedbackEntityById(answer.getFeedbackId());
		AnswerEntity answerEntity = new AnswerEntity(accountEntity, feedbackEntity, answer.getText(), answer.getCreationDate());
		em.persist(answerEntity);
		return answerEntity.getId();
	}

	@Override
	@Transactional
	public AnswerEntity getAnswerEntityById(long id) throws DataNotFoundException {
		Query query = em.createQuery("from AnswerEntity ae where ae.id = :parameter");
		query.setParameter("parameter", id);
		try {
			return (AnswerEntity) query.getSingleResult();
		} catch (NoResultException nre) {
			throw new DataNotFoundException();
		}
	}

	@Override
	@Transactional
	public AnswerIF getAnswer(long answerId) throws DataNotFoundException {
		AnswerEntity answerEntity =  getAnswerEntityById(answerId);
		AccountEntity accountEntity = answerEntity.getAccountEntity();
		FeedbackEntity feedbackEntity = answerEntity.getFeedbackEntity();
		return new Answer(answerEntity.getId(), accountEntity.getId(), feedbackEntity.getId(), answerEntity.getText(), answerEntity.getCreationDate());
	}

	@Override
	@Transactional
	public void deleteFeedback(long feedbackId) throws DataNotFoundException {
		FeedbackEntity feedbackEntity = getFeedbackEntityById(feedbackId);
		Query deleteAnswerQuery = em.createQuery("delete from AnswerEntity ae where ae.feedbackEntity = :feedback");
		deleteAnswerQuery.setParameter("feedback", feedbackEntity);
		deleteAnswerQuery.executeUpdate();
		ProjectEntity projectEntity = projectDao.getEntityById(feedbackEntity.getProjectEntity().getId());
		Set<FeedbackEntity> feedback = projectEntity.getFeedback();
		feedback.remove(feedbackEntity);
		projectEntity.setFeedback(feedback);

		Query deleteQuery = em.createQuery("delete from FeedbackEntity fe where fe.id = :id");
		deleteQuery.setParameter("id", feedbackId);
		deleteQuery.executeUpdate();
	}

	@Override
	@Transactional
	public void updateFeedback(FeedbackIF feedback) throws DataNotFoundException {
		FeedbackEntity feedbackEntity = getFeedbackEntityById(feedback.getFeedbackId());
		feedbackEntity.setText(feedback.getText());
		em.merge(feedbackEntity);
	}

	@Override
	@Transactional
	public void deleteAnswer(long answerId) throws DataNotFoundException {
		AnswerEntity answerEntity = getAnswerEntityById(answerId);
		Query deleteQuery = em.createQuery("delete from AnswerEntity ae where id = :id");
		deleteQuery.setParameter("id", answerEntity.getId());
		deleteQuery.executeUpdate();
	}

	@Override
	@Transactional
	public void updateAnswer(AnswerIF answer) throws DataNotFoundException {
		AnswerEntity answerEntity = getAnswerEntityById(answer.getAnswerId());
		answerEntity.setText(answer.getText());
		em.merge(answerEntity);
	}
}