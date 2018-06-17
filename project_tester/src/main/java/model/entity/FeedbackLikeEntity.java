package model.entity;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
@DiscriminatorValue(value="FEEDBACK")
public class FeedbackLikeEntity extends LikeEntity {

	@ManyToOne
	private FeedbackEntity feedback;

	public FeedbackLikeEntity() {}
	
	public FeedbackLikeEntity(FeedbackEntity feedbackEntity, AccountEntity accountEntity) {
		super(accountEntity);
		this.feedback = feedbackEntity;
	}

	public FeedbackEntity getFeedback() {
		return feedback;
	}

	public void setFeedback(FeedbackEntity feedbackEntity) {
		this.feedback = feedbackEntity;
	}

	
}