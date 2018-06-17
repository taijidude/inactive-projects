package model.entity;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
@DiscriminatorValue(value="ANSWER")
public class AnswerLikeEntity extends LikeEntity {
	
	@ManyToOne
	private AnswerEntity answerEntity;

	public AnswerLikeEntity() {}
	
	public AnswerLikeEntity(AnswerEntity answerEntity, AccountEntity accountEntity) {
		super(accountEntity);
		this.answerEntity = answerEntity;
	}

	public AnswerEntity getAnswer() {
		return answerEntity;
	}

	public void setAnswer(AnswerEntity answerEntity) {
		this.answerEntity = answerEntity;
	}
}
