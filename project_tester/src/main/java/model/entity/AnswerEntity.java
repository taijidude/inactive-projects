package model.entity;

import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name="ANSWER")
public class AnswerEntity  {

	@Id
	@GeneratedValue
	@Column(name="ID")
	protected long id;
	
	@Column(name="TEXT")
	protected String text;

	@Column(name="CREATIONDATE")
	private Date creationDate;
	
	@ManyToOne
	protected AccountEntity accountEntity;
	
	@OneToOne
	private FeedbackEntity feedbackEntity;
	
	@OneToMany(mappedBy = "answerEntity")
	private Set<AnswerLikeEntity> likes = new HashSet<AnswerLikeEntity>();
	
	public AnswerEntity() {}
	
	public AnswerEntity(AccountEntity accountEntity, FeedbackEntity feedbackEntity, String text, Date creationDate) {
		this.accountEntity = accountEntity;
		this.feedbackEntity = feedbackEntity;
		this.text = text;
		if(creationDate != null) {
			this.creationDate = creationDate;
		} else {
			this.creationDate = Calendar.getInstance().getTime();
		}
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public AccountEntity getAccountEntity() {
		return accountEntity;
	}

	public void setAccountEntity(AccountEntity accountEntity) {
		this.accountEntity = accountEntity;
	}

	public FeedbackEntity getFeedbackEntity() {
		return feedbackEntity;
	}

	public void setFeedbackEntity(FeedbackEntity feedbackEntity) {
		this.feedbackEntity = feedbackEntity;
	}

	public Set<AnswerLikeEntity> getLikes() {
		return likes;
	}

	public void setLikes(Set<AnswerLikeEntity> likes) {
		this.likes = likes;
	}

}