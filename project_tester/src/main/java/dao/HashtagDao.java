package dao;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import exception.DataNotFoundException;
import model.Hashtag;
import model.HashtagIF;
import model.entity.HashtagEntity;

/**
 * @author John
 *
 */
@Repository
public class HashtagDao implements HashtagDaoIF{

	@PersistenceContext
	private EntityManager em;

	@Override
	@Transactional
	public long persist(String hashtag) {
		HashtagEntity hashtagEntity = new HashtagEntity();
		hashtagEntity.setText(hashtag);
		em.persist(hashtagEntity);
		return hashtagEntity.getId();
	}

	@Override
	@Transactional
	public Set<HashtagIF> getAll() {
		Query query = em.createQuery("from HashtagEntity");
		List<HashtagEntity> entityList = (List<HashtagEntity>)query.getResultList();
		Set<HashtagIF> dtoList = new HashSet<HashtagIF>();
		Iterator<HashtagEntity> iterator = entityList.iterator();
		while(iterator.hasNext()) {
			HashtagEntity entity = iterator.next();
			dtoList.add(new Hashtag(entity.getId(), entity.getText()));
		}
		return dtoList;
	}

	@Override
	@Transactional
	public HashtagIF get(long id) throws DataNotFoundException {
		HashtagEntity singleResult = getEntityById(id);
		return new Hashtag(singleResult.getId(), singleResult.getText());
	}
	
	@Override
	@Transactional
	public HashtagEntity getEntityById(long id) throws DataNotFoundException {
		Query query = em.createQuery("from HashtagEntity he where he.id = :parameter");
		query.setParameter("parameter", id);
		try {
			return (HashtagEntity) query.getSingleResult();
		} catch(NoResultException nre) {
			throw new DataNotFoundException();
		}
	}
	
	
	@Override
	public void delete(long id) {
		Query query = em.createQuery("delete from HashtagEntity ae where ae.id = :parameter");
		query.setParameter("parameter", id);
		query.executeUpdate();
	}

	@Override
	public HashtagEntity getEntityByString(String hashtag) {
		Query query = em.createQuery("from HashtagEntity he where he.text = :text");
		query.setParameter("text", hashtag);
		try {
			return (HashtagEntity) query.getSingleResult();
		} catch(NoResultException nre) {
			return null;
		}
	}

	@Override
	public Set<HashtagEntity> createIfNotExists(Set<String> hashTags) {
		Query query = em.createQuery("from HashtagEntity where text = :text");
		Set<HashtagEntity> result = new HashSet<HashtagEntity>();
		for (String hashtag : hashTags) {
			query.setParameter("text", hashtag);
			try {
				HashtagEntity singleResult = (HashtagEntity) query.getSingleResult();
				result.add(singleResult);
			} catch(NoResultException nre) {
				persist(hashtag);
				result.add(getEntityByString(hashtag));
			}
		}
		return result;
	}
}