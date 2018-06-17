package service;

import java.util.List;
import java.util.Set;

import exception.DataNotFoundException;
import model.AnswerIF;
import model.FeedbackIF;
import model.ProjectIF;

public interface ProjectServiceIF {

	void update(ProjectIF project) throws DataNotFoundException;

	long create(ProjectIF project) throws DataNotFoundException;

	ProjectIF get(long id) throws DataNotFoundException;

	void delete(long id);

	List<ProjectIF> getAll();

	void addHashtag(long projectId, long hashtagId) throws DataNotFoundException;

	void removeHashtag(long projectId, long hashtagId) throws DataNotFoundException;

	void addCategory(long projectId, long categoryId) throws DataNotFoundException;

	void removeCategory(long projectId, long categoryId) throws DataNotFoundException;

	void addUpload(long projectId, long uploadId) throws DataNotFoundException;

	void removeUpload(long projectId, long uploadId) throws DataNotFoundException;

	long addFeedback(FeedbackIF feedback) throws DataNotFoundException;

	FeedbackIF getFeedback(long feedbackId) throws DataNotFoundException;

	void deleteFeedback(long feedbackId) throws DataNotFoundException;

	void updateFeedback(FeedbackIF feedback) throws DataNotFoundException;

	long pesistAnswer(AnswerIF answer) throws DataNotFoundException;

	AnswerIF getAnswer(long answerId) throws DataNotFoundException;

	void removeAnswer(long answerId) throws DataNotFoundException;

	void updateAnswer(AnswerIF answer) throws DataNotFoundException;
}