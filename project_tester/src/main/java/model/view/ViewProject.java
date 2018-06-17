package model.view;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import model.Feedback;
import model.Spot;
import model.Upload;

public class ViewProject implements ViewProjectIF {	
	
	private long id;
	private String publishedDate;
	private String expirationDate;
	private int likes;

	@JsonInclude(value=Include.NON_NULL)
	private String idea;
	@JsonInclude(value=Include.NON_NULL)
	private String innovation;
	@JsonInclude(value=Include.NON_NULL)
	private String motivation;
	@JsonInclude(value=Include.NON_NULL)
	private String reward;
	@JsonInclude(value=Include.NON_NULL)
	private String status;
	private ViewSpotIF spot;
	private int duration;
	private String name;
	private ViewAccountIF owner;
	@JsonInclude(value=Include.NON_NULL)
	private Set<String> hashtags;
	@JsonInclude(value=Include.NON_NULL)
	private Set<String> categories;
	private List<ViewUploadIF> images = new ArrayList<>();
	@JsonInclude(value=Include.NON_NULL)
	private List<ViewFeedbackIF> feedback;
	
	public ViewProject(long id, String publishedDate, String expirationDate, int likes, String idea, String innovation,
			String motivation, String reward, String status, ViewSpotIF spot, int duration, String name, ViewAccountIF owner) {
		this.id = id;
		this.publishedDate = publishedDate;
		this.expirationDate = expirationDate;
		this.likes = likes;
		this.idea = idea;
		this.innovation = innovation;
		this.motivation = motivation;
		this.reward = reward;
		this.status = status;
		this.spot = spot;
		this.duration = duration;
		this.name = name;
		this.owner = owner;
	}

	public ViewProject(long id, String publishedDate, String expirationDate, int likes, ViewSpotIF spot, String name, ViewAccountIF owner) {
		this.id = id;
		this.publishedDate = publishedDate;
		this.expirationDate = expirationDate;
		this.likes = likes;
		this.spot = spot;
		this.name = name;
		this.owner = owner;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getPublishedDate() {
		return publishedDate;
	}

	public void setPublishedDate(String publishedDate) {
		this.publishedDate = publishedDate;
	}

	public String getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(String expirationDate) {
		this.expirationDate = expirationDate;
	}

	public int getLikes() {
		return likes;
	}

	public void setLikes(int likes) {
		this.likes = likes;
	}

	public String getIdea() {
		return idea;
	}

	public void setIdea(String idea) {
		this.idea = idea;
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

	public String getReward() {
		return reward;
	}

	public void setReward(String reward) {
		this.reward = reward;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public ViewSpotIF getSpot() {
		return spot;
	}

	public void setSpot(ViewSpotIF spot) {
		this.spot = spot;
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

	public ViewAccountIF getOwner() {
		return owner;
	}

	public void setOwner(ViewAccountIF owner) {
		this.owner = owner;
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

	public List<ViewUploadIF> getImages() {
		return images;
	}

	public void setImages(List<ViewUploadIF> images) {
		this.images = images;
	}

	public List<ViewFeedbackIF> getFeedback() {
		return feedback;
	}

	public void setFeedback(List<ViewFeedbackIF> feedback) {
		this.feedback = feedback;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((categories == null) ? 0 : categories.hashCode());
		result = prime * result + duration;
		result = prime * result + ((expirationDate == null) ? 0 : expirationDate.hashCode());
		result = prime * result + ((feedback == null) ? 0 : feedback.hashCode());
		result = prime * result + ((hashtags == null) ? 0 : hashtags.hashCode());
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + ((idea == null) ? 0 : idea.hashCode());
		result = prime * result + ((images == null) ? 0 : images.hashCode());
		result = prime * result + ((innovation == null) ? 0 : innovation.hashCode());
		result = prime * result + likes;
		result = prime * result + ((motivation == null) ? 0 : motivation.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((owner == null) ? 0 : owner.hashCode());
		result = prime * result + ((publishedDate == null) ? 0 : publishedDate.hashCode());
		result = prime * result + ((reward == null) ? 0 : reward.hashCode());
		result = prime * result + ((spot == null) ? 0 : spot.hashCode());
		result = prime * result + ((status == null) ? 0 : status.hashCode());
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
		ViewProject other = (ViewProject) obj;
		if (categories == null) {
			if (other.categories != null)
				return false;
		} else if (!categories.equals(other.categories))
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
		if (idea == null) {
			if (other.idea != null)
				return false;
		} else if (!idea.equals(other.idea))
			return false;
		if (images == null) {
			if (other.images != null)
				return false;
		} else if (!images.equals(other.images))
			return false;
		if (innovation == null) {
			if (other.innovation != null)
				return false;
		} else if (!innovation.equals(other.innovation))
			return false;
		if (likes != other.likes)
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
		if (owner == null) {
			if (other.owner != null)
				return false;
		} else if (!owner.equals(other.owner))
			return false;
		if (publishedDate == null) {
			if (other.publishedDate != null)
				return false;
		} else if (!publishedDate.equals(other.publishedDate))
			return false;
		if (reward == null) {
			if (other.reward != null)
				return false;
		} else if (!reward.equals(other.reward))
			return false;
		if (spot == null) {
			if (other.spot != null)
				return false;
		} else if (!spot.equals(other.spot))
			return false;
		if (status == null) {
			if (other.status != null)
				return false;
		} else if (!status.equals(other.status))
			return false;
		return true;
	}
}