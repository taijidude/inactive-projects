package model;

import java.util.Date;

public interface FeedbackIF {

	long getProjectId();

	long getAccountId();

	String getText();

	Date getCreationDate();

	long getAnswerId();

	long getFeedbackId();
}