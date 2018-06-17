package dao;

import java.util.List;

import exception.DataNotFoundException;
import model.ProjectIF;
import model.entity.ProjectEntity;

public interface ProjectDaoIF {

	long persist(ProjectIF project) throws DataNotFoundException;

	ProjectIF get(long id) throws DataNotFoundException;

	List<ProjectIF> getAll();

	void update(ProjectIF project) throws DataNotFoundException;

	void delete(long id);

	void addHashtag(long id, long hashtagId) throws DataNotFoundException;

	void removeHashtag(long projectId, long hashtagId) throws DataNotFoundException;

	void addCategory(long projectId, long categoryId) throws DataNotFoundException;

	void removeCategory(long projectId, long categoryId) throws DataNotFoundException;

	void addUpload(long projectId, long uploadId) throws DataNotFoundException;

	void removeUpload(long projectId, long uploadId) throws DataNotFoundException;

	ProjectEntity getEntityById(long id) throws DataNotFoundException;
}