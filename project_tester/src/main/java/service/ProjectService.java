package service;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dao.FeedbackDaoIF;
import dao.ProjectDaoIF;
import exception.DataNotFoundException;
import model.AnswerIF;
import model.FeedbackIF;
import model.ProjectIF;

@Service
public class ProjectService implements ProjectServiceIF {

	@Autowired
	private ProjectDaoIF projectDao;
	
	@Autowired
	private FeedbackDaoIF feedbackDao;
	
	@Override
	public void update(ProjectIF project) throws DataNotFoundException {
		projectDao.update(project);
	}
	@Override
	public long create(ProjectIF project) throws DataNotFoundException {
		return projectDao.persist(project);
	}
	@Override
	public ProjectIF get(long id) throws DataNotFoundException {
		return projectDao.get(id);
	}
	@Override
	public void delete(long id) {
		projectDao.delete(id);
	}
	
	@Override
	public List<ProjectIF> getAll() {
		return projectDao.getAll();
	}
	@Override
	public void addHashtag(long projectId, long hashtagId) throws DataNotFoundException {
		projectDao.addHashtag(projectId, hashtagId);
		
	}
	@Override
	public void removeHashtag(long projectId, long hashtagId) throws DataNotFoundException {
		projectDao.removeHashtag(projectId, hashtagId);
		
	}
	@Override
	public void addCategory(long projectId, long categoryId) throws DataNotFoundException {
		projectDao.addCategory(projectId, categoryId);
	}
	@Override
	public void removeCategory(long projectId, long categoryId) throws DataNotFoundException {
		projectDao.removeCategory(projectId, categoryId);
		
	}
	@Override
	public void addUpload(long projectId, long uploadId) throws DataNotFoundException {
		projectDao.addUpload(projectId, uploadId);
		
	}
	@Override
	public void removeUpload(long projectId, long uploadId) throws DataNotFoundException {
		projectDao.removeUpload(projectId, uploadId);
	}
	@Override
	public long addFeedback(FeedbackIF feedback) throws DataNotFoundException {
		return feedbackDao.persistFeedback(feedback);
	}
	@Override
	public FeedbackIF getFeedback(long feedbackId) throws DataNotFoundException {
		return feedbackDao.getFeedback(feedbackId);
	}
	@Override
	public void deleteFeedback(long feedbackId) throws DataNotFoundException {
		feedbackDao.deleteFeedback(feedbackId);
	}
	@Override
	public void updateFeedback(FeedbackIF feedback) throws DataNotFoundException {
		feedbackDao.updateFeedback(feedback);
	}
	@Override
	public long pesistAnswer(AnswerIF answer) throws DataNotFoundException {
		return feedbackDao.persistAnswer(answer);
	}
	@Override
	public AnswerIF getAnswer(long answerId) throws DataNotFoundException {
		return feedbackDao.getAnswer(answerId);
	}
	@Override
	public void removeAnswer(long answerId) throws DataNotFoundException {
		feedbackDao.deleteAnswer(answerId);
	}
	@Override
	public void updateAnswer(AnswerIF answer) throws DataNotFoundException {
		feedbackDao.updateAnswer(answer);
	}	
}