package model;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Project implements ProjectIF {
	
	private long id;
	private int duration;
	private String name;
	private SpotIF spot;
	private String description;
	private String innovation;
	private String motivation;
	
	private Date expirationDate;
	private long accountId;
	private Set<String> hashtags = new HashSet<>();
	private Set<String> categories = new HashSet<>();
	private Set<UploadIF> uploads = new HashSet<>();
	private Set<FeedbackIF> feedback = new HashSet<>();
	
	public Project() {}
	
	public Project(String name, Set<String> categories, int durartion, String description, String innovation,
			String motivation, String reward, Set<String> hashTags) {
	}
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public SpotIF getSpot() {
		return spot;
	}

	public void setSpot(SpotIF spot) {
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

	public long getAccountId() {
		return accountId;
	}

	public void setAccountId(long accountId) {
		this.accountId = accountId;
	}

	public Set<String> getHashtags() {
		return hashtags;
	}

	public void setHashtags(Set<String> hashtags) {
		this.hashtags = hashtags;
	}

	public Set<String> getCategories() {
		return categories;
	}

	public void setCategories(Set<String> categories) {
		this.categories = categories;
	}


	public Set<UploadIF> getUploads() {
		return uploads;
	}

	public void setUploads(Set<UploadIF> uploads) {
		this.uploads = uploads;
	}

	public Set<FeedbackIF> getFeedback() {
		return feedback;
	}

	public void setFeedback(Set<FeedbackIF> feedback) {
		this.feedback = feedback;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (accountId ^ (accountId >>> 32));
		result = prime * result + ((categories == null) ? 0 : categories.hashCode());
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + duration;
		result = prime * result + ((expirationDate == null) ? 0 : expirationDate.hashCode());
		result = prime * result + ((feedback == null) ? 0 : feedback.hashCode());
		result = prime * result + ((hashtags == null) ? 0 : hashtags.hashCode());
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + ((innovation == null) ? 0 : innovation.hashCode());
		result = prime * result + ((motivation == null) ? 0 : motivation.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((spot == null) ? 0 : spot.hashCode());
		result = prime * result + ((uploads == null) ? 0 : uploads.hashCode());
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
		Project other = (Project) obj;
		if (accountId != other.accountId)
			return false;
		if (categories == null) {
			if (other.categories != null)
				return false;
		} else if (!categories.equals(other.categories))
			return false;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (duration != other.duration)
			return false;
		if (expirationDate == null) {
			if (other.expirationDate != null)
				return false;
		} else if (!expirationDate.equals(other.expirationDate))
			return false;
		if (feedback == null) {
			if (other.feedback != null)
				return false;
		} else if (!feedback.equals(other.feedback))
			return false;
		if (hashtags == null) {
			if (other.hashtags != null)
				return false;
		} else if (!hashtags.equals(other.hashtags))
			return false;
		if (id != other.id)
			return false;
		if (innovation == null) {
			if (other.innovation != null)
				return false;
		} else if (!innovation.equals(other.innovation))
			return false;
		if (motivation == null) {
			if (other.motivation != null)
				return false;
		} else if (!motivation.equals(other.motivation))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (spot == null) {
			if (other.spot != null)
				return false;
		} else if (!spot.equals(other.spot))
			return false;
		if (uploads == null) {
			if (other.uploads != null)
				return false;
		} else if (!uploads.equals(other.uploads))
			return false;
		return true;
	}

}