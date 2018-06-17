package dao;

import exception.DataNotFoundException;
import model.AnswerIF;
import model.FeedbackIF;
import model.entity.AnswerEntity;
import model.entity.FeedbackEntity;

public interface FeedbackDaoIF {

	long persistFeedback(FeedbackIF feedback) throws DataNotFoundException;

	FeedbackIF getFeedback(long feedbackId) throws DataNotFoundException;

	long persistAnswer(AnswerIF answer) throws DataNotFoundException;

	AnswerIF getAnswer(long answerId) throws DataNotFoundException;

	void deleteFeedback(long feedbackId) throws DataNotFoundException;

	FeedbackEntity getFeedbackEntityById(long id) throws DataNotFoundException;

	void updateFeedback(FeedbackIF feedback) throws DataNotFoundException;

	void deleteAnswer(long answerId) throws DataNotFoundException;

	void updateAnswer(AnswerIF answer) throws DataNotFoundException;

	AnswerEntity getAnswerEntityById(long id) throws DataNotFoundException;
}