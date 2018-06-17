package model;

import java.util.Calendar;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class Answer implements AnswerIF {

	private long feedbackId;
	private long accountId;
	private String text;
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm")
	private Date creationDate;
	private long answerId;

	public Answer() {}
	
	public Answer(long answerId, long accountId, long feedbackId, String text, Date creationDate) {
		this.answerId = answerId;
		this.accountId = accountId;
		this.feedbackId = feedbackId;
		this.text = text;
		initCreationDate(creationDate);
	}

	public Answer(long accountId, long feedbackId, String text, Date creationDate) {
		this.accountId = accountId;
		this.feedbackId = feedbackId;
		this.text = text;
		initCreationDate(creationDate);
	}
	
	private void initCreationDate(Date creationDate) {
		if(creationDate != null) {
			this.creationDate = creationDate;			
		} else {
			this.creationDate = Calendar.getInstance().getTime();
		}
	}

	@Override
	public long getFeedbackId() {
		return feedbackId;
	}

	@Override
	public void setFeedbackId(long feedbackId) {
		this.feedbackId = feedbackId;
	}

	@Override
	public long getAccountId() {
		return accountId;
	}
	
	@Override
	public String getText() {
		return text;
	}
	
	@Override
	public void setText(String text) {
		this.text = text;
	}

	@Override
	public Date getCreationDate() {
		return creationDate;
	}
	
	@Override
	public long getAnswerId() {
		return answerId;
	}
}