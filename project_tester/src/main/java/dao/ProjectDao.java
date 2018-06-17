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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import exception.DataNotFoundException;
import model.FeedbackIF;
import model.Project;
import model.ProjectIF;
import model.UploadIF;
import model.entity.AccountEntity;
import model.entity.CategoryEntity;
import model.entity.HashtagEntity;
import model.entity.ProjectEntity;
import model.entity.UploadEntity;


/**
 * @author John
 *
 */
@Repository
public class ProjectDao implements ProjectDaoIF {

	@Autowired
	private HashtagDaoIF hashtagDao;

	@Autowired
	private AccountDaoIF accountDao;

	@Autowired
	private CategoryDaoIF categoryDao;
	
	@Autowired
	private UploadDaoIF uploadDao;
	
	@Autowired
	private FeedbackDaoIF feedbackDao;
	
	@PersistenceContext
	private EntityManager em;

	@Override
	@Transactional
	public ProjectIF get(long id) throws DataNotFoundException {
		return null;
		
//		ProjectEntity singleResult = getEntityById(id);
//		Project project = new Project(singleResult.getId(), singleResult.getName(), singleResult.getLocation(), 
//				singleResult.getDescription(), singleResult.getSpecialFeatures(), singleResult.getMotivation(), 
//				singleResult.getExpirationDate(), singleResult.getAccount().getId());
//		Set<HashtagEntity> hashtags = singleResult.getHashtags();
//		if(!hashtags.isEmpty()) {
//			for (HashtagEntity hashtagEntity : hashtags) {
//				project.addHashtagId(Long.valueOf(hashtagEntity.getId()));
//			}
//		}
//		Set<CategoryEntity> categories = singleResult.getCategories();
//		if(!categories.isEmpty()) {
//			for (CategoryEntity categoryEntity : categories) {
//				project.addCategory(Long.valueOf(categoryEntity.getId()));
//			}
//		}
//		Set<UploadEntity> uploads = singleResult.getUploads();
//		if(!uploads.isEmpty()) {
//			for (UploadEntity uploadEntity : uploads) {
//				project.addUpload(Long.valueOf(uploadEntity.getId()));
//			}
//		}
//		return project;
	}

	/**
	 * TODO: Was ist der Unterschied zwischen find und der jpql Query?
	 * @param id
	 * @return
	 * @throws DataNotFoundException
	 */
	@Override
	@Transactional
	public ProjectEntity getEntityById(long id) throws DataNotFoundException {
		try {
			Query query = em.createQuery("from ProjectEntity pe where pe.id = :id");
			query.setParameter("id", id);
			return (ProjectEntity) query.getSingleResult();	
		} catch(NoResultException nre) {
			throw new DataNotFoundException();
		}
	}

	/**
	 * @throws DataNotFoundException 
	 */
	@Override
	@Transactional
	public long persist(ProjectIF project) throws DataNotFoundException {
		
		AccountEntity accountEntity = accountDao.getEntityById(project.getAccountId());
		if(accountEntity != null) {
			
			Set<HashtagEntity> hashtags = hashtagDao.createIfNotExists(project.getHashtags());
			Set<String> categories = project.getCategories();
			ProjectEntity projectEntity = new ProjectEntity();
			
//			ProjectEntity projectEntity = new ProjectEntity(
//					project.getName(),
//					project.getDuration(),
//					project.getSpot(),
//					project.getDescription  (),
//					project.getInnovation(),
//					project.getMotivation (),
//					project.getExpirationDate (),
//					accountEntity,
//					hashtags,
//					categories,
//					uploads);
			
			em.persist(projectEntity);
			return projectEntity.getId();
		}
		throw new DataNotFoundException();
	}

	@Override
	@Transactional
	public List<ProjectIF> getAll() {
		return null;
//		Query query = em.createQuery("from ProjectEntity");
//		try {
//			List<ProjectEntity> resultList = query.getResultList();
//			Iterator<ProjectEntity> iterator = resultList.iterator();
//			List<ProjectIF> result = new ArrayList<ProjectIF>();
//			while(iterator.hasNext()) {
//				ProjectEntity projectEntity = iterator.next();
//				ProjectIF project = new Project(
//						projectEntity.getId(),
//						projectEntity.getName(),
//						projectEntity.getLocation(),
//						projectEntity.getDescription(),	
//						projectEntity.getSpecialFeatures(),
//						projectEntity.getMotivation(),
//						projectEntity.getExpirationDate(),
//						projectEntity.getAccount().getId());
//				result.add(project);
//			}
//			return result;
//		} catch(NoResultException nre) {
//			return null; 
//		}
	}

	@Override
	@Transactional
	public void delete(long id) {
		Query query = em.createQuery("delete from ProjectEntity pe where pe.id = :id");
		query.setParameter("id", id);
		query.executeUpdate();
	}

	@Override
	@Transactional
	public void update(ProjectIF project) throws DataNotFoundException {
//		AccountEntity accountEntity = accountDao.getEntityById(project.getAccountId());
//		ProjectEntity projectEntity = em.find(ProjectEntity.class, project.getId());
//		if(projectEntity != null) {
//			projectEntity.setName(project.getName());
//			projectEntity.setLocation(project.getLocation());
//			projectEntity.setDescription(project.getDescription());
//			projectEntity.setSpecialFeatures(project.getSpecialFeatures());
//			projectEntity.setMotivation(project.getMotivation());
//			projectEntity.setExpirationDate(project.getExpirationDate());
//			projectEntity.setAccount(accountEntity);
//		}
	}

	@Override
	@Transactional
	public void addHashtag(long projectId, long hashtagId) throws DataNotFoundException {
//		HashtagEntity hashtagEntity = hashtagDao.getEntityById(hashtagId);
//		ProjectEntity projectEntity = em.find(ProjectEntity.class,projectId);
//		if(projectEntity != null) {
//			Set<HashtagEntity> hashtags = projectEntity.getHashtags();
//			hashtags.add(hashtagEntity);
//			projectEntity.setHashtags(hashtags);
//		}
//		em.merge(projectEntity);
	}

	@Override
	@Transactional
	public void removeHashtag(long projectId, long hashtagId) throws DataNotFoundException {
//		HashtagEntity hashtagEntity = hashtagDao.getEntityById(hashtagId);
//		ProjectEntity projectEntity = em.find(ProjectEntity.class, projectId);
//		if(projectEntity != null) {
//			Set<HashtagEntity> hashtags = projectEntity.getHashtags();
//			hashtags.remove(hashtagEntity);
//			projectEntity.setHashtags(hashtags);
//		}
//		em.merge(projectEntity);
	}

	@Override
	@Transactional
	public void addCategory(long projectId, long categoryId) throws DataNotFoundException {
//		CategoryEntity categoryEntity = categoryDao.getEntityById(categoryId);
//		ProjectEntity projectEntity = em.find(ProjectEntity.class, projectId);
//		if(projectEntity != null) {
//			Set<CategoryEntity> categories = projectEntity.getCategories();
//			categories.add(categoryEntity);
//			projectEntity.setCategories(categories);
//		}
//		em.merge(projectEntity);
	}

	@Override
	@Transactional
	public void removeCategory(long projectId, long categoryId) throws DataNotFoundException {
//		CategoryEntity categoryEntity = categoryDao.getEntityById(categoryId);
//		ProjectEntity projectEntity = em.find(ProjectEntity.class, projectId);
//		if(projectEntity != null) {
//			Set<CategoryEntity> categories = projectEntity.getCategories();
//			categories.remove(categoryEntity);
//			projectEntity.setCategories(categories);
//		}
//		em.merge(projectEntity);
	}

	@Override
	@Transactional
	public void addUpload(long projectId, long uploadId) throws DataNotFoundException {
//		UploadEntity uploadEntity = uploadDao.getEntityById(uploadId);
//		ProjectEntity projectEntity = em.find(ProjectEntity.class, projectId);
//		if(projectEntity != null) {
//			Set<UploadEntity> uploads = projectEntity.getUploads();
//			uploads.add(uploadEntity);
//			projectEntity.setUploads(uploads);
//		}
//		em.merge(projectEntity);
	}

	@Override
	@Transactional
	public void removeUpload(long projectId, long uploadId) throws DataNotFoundException {
//		UploadEntity uploadEntity = uploadDao.getEntityById(uploadId);
//		ProjectEntity projectEntity = em.find(ProjectEntity.class, projectId);
//		if(projectEntity != null) {
//			Set<UploadEntity> uploads = projectEntity.getUploads();
//			uploads.remove(uploadEntity);
//			projectEntity.setUploads(uploads);
//		}
//		em.merge(projectEntity);
	}
}