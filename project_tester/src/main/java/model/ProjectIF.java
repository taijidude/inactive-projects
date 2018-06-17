package model;

import java.util.Date;
import java.util.Set;

public interface ProjectIF {
	 long getId();
	 void setId(long id);
	 int getDuration();
	 void setDuration(int duration);
	 String getName();
	 void setName(String name);
	 SpotIF getSpot();
	 void setSpot(SpotIF spot);
	 String getDescription();
	 void setDescription(String description);
	 String getInnovation();
	 void setInnovation(String innovation);
	 String getMotivation();
	 void setMotivation(String motivation);
	 Date getExpirationDate();
	 void setExpirationDate(Date expirationDate);
	 long getAccountId();
	 void setAccountId(long accountId);
	 Set<String> getHashtags();
	 void setHashtags(Set<String> hashtags);
	 Set<String> getCategories();
	 void setCategories(Set<String> categories);
	 Set<UploadIF> getUploads();
	 void setUploads(Set<UploadIF> uploads);
	 Set<FeedbackIF> getFeedback();
	 void setFeedback(Set<FeedbackIF> feedback);
}