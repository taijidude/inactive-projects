package model;

import java.util.Calendar;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class Feedback implements FeedbackIF {

	private long projectId;
	private long answerId;
	private long accountId;
	private String text;
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm")
	private Date creationDate;
	private long feedbackId;
	
	public Feedback() {}
	
	public Feedback(long feedbackId, long projectId, long accountId, String text, Date creationDate) {
		this.feedbackId = feedbackId;
		this.projectId = projectId;
		this.accountId = accountId;
		this.text = text;
		initCreationDate(creationDate);
	}

	public Feedback(long projectId, long accountId, String text, Date creationDate) {
		this.projectId = projectId;
		this.accountId = accountId;
		this.text = text;
		initCreationDate(creationDate);
	}
	
	private void initCreationDate(Date creationDate) {
		if(creationDate == null) {
			this.creationDate = Calendar.getInstance().getTime();
		} else {
			this.creationDate = creationDate;
		}
	}

	@Override
	public long getAccountId() {
		return accountId;
	}

	public void setAccountId(long accountId) {
		this.accountId = accountId;
	}
	@Override
	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	@Override
	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	@Override
	public long getProjectId() {
		return projectId;
	}
	
	public void setProjectId(long projectId) {
		this.projectId = projectId;
	}

	@Override
	public long getAnswerId() {
		return answerId;
	}

	public void setAnswerId(long answerId) {
		this.answerId = answerId;
	}

	@Override
	public long getFeedbackId() {
		return feedbackId;
	}
}