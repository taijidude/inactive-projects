package model;

import java.util.Date;

public interface AnswerIF {

	long getAccountId();
	
	long getFeedbackId();

	void setFeedbackId(long feedbackId);

	String getText();

	Date getCreationDate();

	void setText(String text);

	long getAnswerId();
}