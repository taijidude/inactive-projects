package model.entity;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="FEEDBACK")
public class FeedbackEntity  {

	@Id
	@GeneratedValue
	@Column(name="ID")
	private long id;
	
	@Column(name="TEXT")
	private String text;

	@Column(name="CREATIONDATE")
	private Date creationDate;
	
	@ManyToOne
	private AccountEntity accountEntity;
	
	@ManyToOne
	private ProjectEntity projectEntity;
	
	@OneToMany(mappedBy = "feedback")
	private Set<FeedbackLikeEntity> likes = new HashSet<FeedbackLikeEntity>();
	
	public FeedbackEntity() {}
	
	public FeedbackEntity(AccountEntity accountEntity, ProjectEntity projectEntity, String text, Date creationDate) {
		this.accountEntity = accountEntity;
		this.projectEntity = projectEntity;
		this.text = text;		
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

	public ProjectEntity getProjectEntity() {
		return projectEntity;
	}

	public void setProjectEntity(ProjectEntity projectEntity) {
		this.projectEntity = projectEntity;
	}

	public Set<FeedbackLikeEntity> getLikes() {
		return likes;
	}

	public void setLikes(Set<FeedbackLikeEntity> likes) {
		this.likes = likes;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((accountEntity == null) ? 0 : accountEntity.hashCode());
		result = prime * result + ((creationDate == null) ? 0 : creationDate.hashCode());
		result = prime * result + ((likes == null) ? 0 : likes.hashCode());
		result = prime * result + ((projectEntity == null) ? 0 : projectEntity.hashCode());
		result = prime * result + ((text == null) ? 0 : text.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FeedbackEntity other = (FeedbackEntity) obj;
		if (accountEntity == null) {
			if (other.accountEntity != null)
				return false;
		} else if (!accountEntity.equals(other.accountEntity))
			return false;
		if (creationDate == null) {
			if (other.creationDate != null)
				return false;
		} else if (!creationDate.equals(other.creationDate))
			return false;
		if (likes == null) {
			if (other.likes != null)
				return false;
		} else if (!likes.equals(other.likes))
			return false;
		if (projectEntity == null) {
			if (other.projectEntity != null)
				return false;
		} else if (!projectEntity.equals(other.projectEntity))
			return false;
		if (text == null) {
			if (other.text != null)
				return false;
		} else if (!text.equals(other.text))
			return false;
		return true;
	}
	
}