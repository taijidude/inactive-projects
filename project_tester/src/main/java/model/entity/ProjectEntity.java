package model.entity;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="PROJECT")
public class ProjectEntity {
	
	@Id
	@GeneratedValue
	private long id;
	
	@Column(name="name")
	private String name;
	
	@Column(name="duration")
	private int duration;
	
	@ManyToOne
	private SpotEntity spot;
	
	@Column(name="description")
	private String description;
	
	@Column(name="innovation")
	private String innovation;
	
	@Column(name="motivation")
	private String motivation;
	
	@Column(name="expirationdate")
	private Date expirationDate;
	
	@ManyToOne
	private AccountEntity account;
	
	@ManyToMany
	private Set<HashtagEntity> hashtags = new HashSet<HashtagEntity>();
	
	@ManyToMany
	private Set<CategoryEntity> categories = new HashSet<CategoryEntity>();
	
	@OneToMany(mappedBy = "project")
	private Set<UploadEntity> uploads = new HashSet<UploadEntity>();
	
	@OneToMany(mappedBy = "projectEntity")
	private Set<FeedbackEntity> feedback = new HashSet<FeedbackEntity>();
	
	@OneToMany(mappedBy = "project")
	private Set<ProjectLikeEntity> likes = new HashSet<ProjectLikeEntity>();
	
	public ProjectEntity() {}

	
	public ProjectEntity(String name, int duration, SpotEntity spot, String description, String innovation,
			String motivation, Date expirationDate, AccountEntity account, Set<HashtagEntity> hashtags,
			Set<CategoryEntity> categories, Set<UploadEntity> uploads) {
		this.name = name;
		this.duration = duration;
		this.spot = spot;
		this.description = description;
		this.innovation = innovation;
		this.motivation = motivation;
		this.expirationDate = expirationDate;
		this.account = account;
		this.hashtags = hashtags;
		this.categories = categories;
		this.uploads = uploads;
	}
	
	public ProjectEntity(String name, int duration, SpotEntity spot, String description, String innovation,
			String motivation, Date expirationDate, AccountEntity account, Set<HashtagEntity> hashtags,
			Set<CategoryEntity> categories, Set<UploadEntity> uploads, Set<FeedbackEntity> feedback,
			Set<ProjectLikeEntity> likes) {
		this.name = name;
		this.duration = duration;
		this.spot = spot;
		this.description = description;
		this.innovation = innovation;
		this.motivation = motivation;
		this.expirationDate = expirationDate;
		this.account = account;
		this.hashtags = hashtags;
		this.categories = categories;
		this.uploads = uploads;
		this.feedback = feedback;
		this.likes = likes;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public SpotEntity getSpot() {
		return spot;
	}

	public void setSpot(SpotEntity spot) {
		this.spot = spot;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getInnovation() {
		return innovation;
	}

	public void setInnovation(String innovation) {
		this.innovation = innovation;
	}

	public String getMotivation() {
		return motivation;
	}

	public void setMotivation(String motivation) {
		this.motivation = motivation;
	}

	public Date getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(Date expirationDate) {
		this.expirationDate = expirationDate;
	}

	public AccountEntity getAccount() {
		return account;
	}

	public void setAccount(AccountEntity account) {
		this.account = account;
	}

	public Set<HashtagEntity> getHashtags() {
		return hashtags;
	}

	public void setHashtags(Set<HashtagEntity> hashtags) {
		this.hashtags = hashtags;
	}

	public Set<CategoryEntity> getCategories() {
		return categories;
	}

	public void setCategories(Set<CategoryEntity> categories) {
		this.categories = categories;
	}

	public Set<UploadEntity> getUploads() {
		return uploads;
	}

	public void setUploads(Set<UploadEntity> uploads) {
		this.uploads = uploads;
	}

	public Set<FeedbackEntity> getFeedback() {
		return feedback;
	}

	public void setFeedback(Set<FeedbackEntity> feedback) {
		this.feedback = feedback;
	}

	public Set<ProjectLikeEntity> getLikes() {
		return likes;
	}

	public void setLikes(Set<ProjectLikeEntity> likes) {
		this.likes = likes;
	}
	
		
	
	
	
}