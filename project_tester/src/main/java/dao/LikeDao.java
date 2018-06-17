package dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import exception.DataNotFoundException;
import model.Like;
import model.LikeIF;
import model.LikeItemTyp;
import model.entity.AccountEntity;
import model.entity.AnswerEntity;
import model.entity.AnswerLikeEntity;
import model.entity.FeedbackEntity;
import model.entity.FeedbackLikeEntity;
import model.entity.LikeEntity;
import model.entity.ProjectEntity;
import model.entity.ProjectLikeEntity;

@Repository
public class LikeDao implements LikeDaoIF {

	@Autowired
	private ProjectDaoIF projectDao;
	
	@Autowired
	private AccountDaoIF accountDao;
	
	@Autowired
	private FeedbackDaoIF feedbackDao;
	
	@PersistenceContext
	private EntityManager em;

	@Override
	@Transactional
	public long persistLike(LikeIF like) throws DataNotFoundException {
		LikeItemTyp itemTyp = like.getItemTyp();
		long itemId = like.getItemId();
		long userId = like.getUserId();
		AccountEntity accountEntity = accountDao.getEntityById(userId);
		LikeEntity entity = null;
		if(itemTyp.equals(LikeItemTyp.PROJECT)) {
			ProjectEntity projectEntity = projectDao.getEntityById(itemId);
			entity = new ProjectLikeEntity(projectEntity, accountEntity);
		} else if(itemTyp.equals(LikeItemTyp.FEEDBACK)) {
			FeedbackEntity feedbackEntity = feedbackDao.getFeedbackEntityById(itemId);
			entity = new FeedbackLikeEntity(feedbackEntity, accountEntity);
		} else if(itemTyp.equals(LikeItemTyp.ANSWER)) {
			AnswerEntity answerEntity = feedbackDao.getAnswerEntityById(itemId);
			entity = new AnswerLikeEntity(answerEntity, accountEntity);
		}
		em.persist(entity);
		return entity.getId();
	}

	@Override
	@Transactional
	public LikeEntity getEntityLikeById(long id) throws DataNotFoundException {
		Query query = em.createQuery("from LikeEntity ae where ae.id = :parameter");
		query.setParameter("parameter", id);
		try {
			return (LikeEntity) query.getSingleResult();
		} catch (NoResultException nre) {
			throw new DataNotFoundException();
		}
	}

	@Override
	@Transactional
	public void deleteLikeById(long id) {
		Query query = em.createQuery("delete from LikeEntity where id = :id");
		query.setParameter("id", id);
		query.executeUpdate();	
	}

	@Override
	@Transactional
	public List<LikeIF> getLikesById(long itemId, LikeItemTyp itemTyp) throws DataNotFoundException {
		StringBuilder hqlBuilder = new StringBuilder("from ");
		if(itemTyp.equals(LikeItemTyp.PROJECT)) {
			hqlBuilder.append("ProjectLikeEntity where project.id = :itemId");
		} else if(itemTyp.equals(LikeItemTyp.FEEDBACK)) {
			hqlBuilder.append("FeedbackLikeEntity where feedback.id = :itemId");
		} else if(itemTyp.equals(LikeItemTyp.ANSWER)) {
			hqlBuilder.append("AnswerLikeEntity where answerEntity.id = :itemId");
		}
		
		Query query = em.createQuery(hqlBuilder.toString());
		query.setParameter("itemId", itemId);
		List<LikeEntity> resultList = query.getResultList();
		if(resultList.isEmpty()) {
			throw new DataNotFoundException();	
		}
		List<LikeIF> result = new ArrayList<LikeIF>();
		for (LikeEntity likeEntity : resultList) {
			if(likeEntity instanceof ProjectLikeEntity) {
				ProjectLikeEntity projectLike = (ProjectLikeEntity) likeEntity;
				result.add(new Like(LikeItemTyp.PROJECT, projectLike.getAccount().getId(), projectLike.getProject().getId()));
			} else if(likeEntity instanceof FeedbackLikeEntity) {
				FeedbackLikeEntity feedbackLike = (FeedbackLikeEntity) likeEntity;
				result.add(new Like(LikeItemTyp.FEEDBACK, feedbackLike.getAccount().getId(), feedbackLike.getFeedback().getId()));
			} else if(likeEntity instanceof AnswerLikeEntity) {
				AnswerLikeEntity answerLike = (AnswerLikeEntity) likeEntity;
				result.add(new Like(LikeItemTyp.ANSWER, answerLike.getAccount().getId(), answerLike.getAnswer().getId()));
			}
		}
		return result;
	}

	@Override
	public List<LikeIF> getLikesByUserId(long userId) throws DataNotFoundException {
		AccountEntity accountEntity = accountDao.getEntityById(userId);
		Query query = em.createQuery("from LikeEntity le where le.account = :account");
		query.setParameter("account", accountEntity);
		List<LikeEntity> likeEntities = query.getResultList();
		List<LikeIF> result = new ArrayList<LikeIF>();
		LikeItemTyp itemTyp = null;
		long itemId = -1;
		for (LikeEntity likeEntity : likeEntities) {
			if(likeEntity instanceof ProjectLikeEntity) {
				ProjectLikeEntity projectLikeEntity = (ProjectLikeEntity) likeEntity;
				itemId = projectLikeEntity.getId();
				itemTyp = LikeItemTyp.PROJECT;
			} else if(likeEntity instanceof FeedbackLikeEntity) {
				FeedbackLikeEntity feedbackLikeEntity = (FeedbackLikeEntity) likeEntity;
				itemId = feedbackLikeEntity.getId();
				itemTyp = LikeItemTyp.FEEDBACK;
			} else if(likeEntity instanceof AnswerLikeEntity) {
				AnswerLikeEntity answerLikeEntity = (AnswerLikeEntity) likeEntity;
				itemId = answerLikeEntity.getId();
				itemTyp = LikeItemTyp.ANSWER;
			}
			if(itemId != -1 && itemTyp != null) {
				result.add(new Like(itemTyp, likeEntity.getAccount().getId(), itemId));				
			} else {
				throw new IllegalStateException("Falscher Zustand des neu instanzierten Likes");
			}
		}
		return result;
	}
	
	private Query createAlreadyExistsQuery(StringBuilder hqlBuilder, AccountEntity accountEntity) {
		Query query = em.createQuery(hqlBuilder.toString());
		query.setParameter("accountEntity", accountEntity);
		return query;
	}
	
	/**
	 * FIXME: Eigentlich nicht gut sich hier auf Exceptions zu verlassen
	 * @throws DataNotFoundException 
	 */
	@Override
	public boolean alreadyExists(LikeIF like) throws DataNotFoundException {
		StringBuilder hqlBuilder = new StringBuilder("from ");
		AccountEntity accountEntity = accountDao.getEntityById(like.getUserId());
		Query query = null;
		LikeItemTyp itemTyp = like.getItemTyp();
		if(itemTyp.equals(LikeItemTyp.PROJECT)) {
			hqlBuilder.append("ProjectLikeEntity where project = :projectEntity and account = :accountEntity");
			query = createAlreadyExistsQuery(hqlBuilder, accountEntity);
			query.setParameter("projectEntity", projectDao.getEntityById(like.getItemId()));
		} else if(itemTyp.equals(LikeItemTyp.FEEDBACK)) {
			hqlBuilder.append("FeedbackLikeEntity where feedback = :feedbackEntity and account = :accountEntity");
			query = createAlreadyExistsQuery(hqlBuilder, accountEntity);
			query.setParameter("feedbackEntity", feedbackDao.getFeedbackEntityById(like.getItemId()));
		} else if(itemTyp.equals(LikeItemTyp.ANSWER)) {
			hqlBuilder.append("AnswerLikeEntity where answerEntity = :answerEntity and account = :accountEntity");
			query = createAlreadyExistsQuery(hqlBuilder, accountEntity);
			query.setParameter("answerEntity", feedbackDao.getAnswerEntityById(like.getItemId()));
		}
		try {
			query.getSingleResult();
			return true;
		} catch(NoResultException nre) {
			return false;
		} catch (NonUniqueResultException nure) {
			throw new IllegalStateException("Den Like gibt es breits mehr als 1x. userId:"+like.getUserId()+" projectId:"+like.getItemId());
		}
	}


}