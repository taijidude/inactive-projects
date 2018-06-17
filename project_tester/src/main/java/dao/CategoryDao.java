package dao;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import exception.DataAlreadyExistsException;
import exception.DataNotFoundException;
import model.Category;
import model.CategoryIF;
import model.entity.CategoryEntity;
import model.entity.HashtagEntity;

@Repository
public class CategoryDao implements CategoryDaoIF {

	@PersistenceContext
	private EntityManager em;

	@Override
	@Transactional
	public long persist(CategoryIF category) throws DataAlreadyExistsException {
		CategoryEntity categoryEntity = new CategoryEntity(category.getText());
		em.persist(categoryEntity);
		return categoryEntity.getId();
	}

	@Override
	@Transactional
	public CategoryIF getById(long id) throws DataNotFoundException {
		Query query = em.createQuery("from CategoryEntity where id = :id");
		query.setParameter("id", id);
		try {
			CategoryEntity singleResult = (CategoryEntity) query.getSingleResult();
			return new Category(singleResult.getId(), singleResult.getText()); 
		} catch (NoResultException exception) {
			throw new DataNotFoundException();
		}
	}

	@Override
	@Transactional
	public CategoryEntity getEntityById(long id) throws DataNotFoundException {
		Query query = em.createQuery("from CategoryEntity where id = :id");
		query.setParameter("id", id);
		try {
			return (CategoryEntity) query.getSingleResult();
		} catch (NoResultException exception) {
			throw new DataNotFoundException();
		}
	}
	
	@Override
	@Transactional
	public void delete(long id) {
		Query query = em.createQuery("delete from CategoryEntity where id = :id");
		query.setParameter("id", id);
		query.executeUpdate();
	}

	@Override
	public long getIdByText(String text) {
		Query query = em.createQuery("select ce.id from CategoryEntity ce where ce.text = :text");
		query.setParameter("text", text);
		Long id = (Long)query.getSingleResult();
		return id.longValue();
	}

	@Override
	public boolean alreadyExists(String categoryText) {
		Query query = em.createQuery("select ce.id from CategoryEntity ce where ce.text = :text");
		query.setParameter("text", categoryText);
		return query.getResultList().size() > 0; 
	}

	@Override
	public CategoryEntity getEnityByString(String categorytext) {
		Query query = em.createQuery("from CategoryEntity ce where ce.text = :text");
		query.setParameter("text", categorytext);
		try {
			return (CategoryEntity) query.getSingleResult();
		} catch(NoResultException nre) {
			return null;
		}
	}


}