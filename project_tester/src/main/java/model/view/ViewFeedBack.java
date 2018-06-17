package model.view;

public class ViewFeedBack implements ViewFeedbackIF {

	private int id;
	private String publishedDate;
	private ViewAccount user;
	private String content;
	private int likes;
	private boolean selfLiked;

	public ViewFeedBack(int id, String publishedDate, ViewAccount user, String content, int likes, boolean selfLiked) {
		this.id = id;
		this.publishedDate = publishedDate;
		this.user = user;
		this.content = content;
		this.likes = likes;
		this.selfLiked = selfLiked;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getPublishedDate() {
		return publishedDate;
	}

	public void setPublishedDate(String publishedDate) {
		this.publishedDate = publishedDate;
	}

	public ViewAccount getUser() {
		return user;
	}

	public void setUser(ViewAccount user) {
		this.user = user;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getLikes() {
		return likes;
	}

	public void setLikes(int likes) {
		this.likes = likes;
	}

	public boolean isSelfLiked() {
		return selfLiked;
	}

	public void setSelfLiked(boolean selfLiked) {
		this.selfLiked = selfLiked;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((content == null) ? 0 : content.hashCode());
		result = prime * result + id;
		result = prime * result + likes;
		result = prime * result + ((publishedDate == null) ? 0 : publishedDate.hashCode());
		result = prime * result + (selfLiked ? 1231 : 1237);
		result = prime * result + ((user == null) ? 0 : user.hashCode());
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
		ViewFeedBack other = (ViewFeedBack) obj;
		if (content == null) {
			if (other.content != null)
				return false;
		} else if (!content.equals(other.content))
			return false;
		if (id != other.id)
			return false;
		if (likes != other.likes)
			return false;
		if (publishedDate == null) {
			if (other.publishedDate != null)
				return false;
		} else if (!publishedDate.equals(other.publishedDate))
			return false;
		if (selfLiked != other.selfLiked)
			return false;
		if (user == null) {
			if (other.user != null)
				return false;
		} else if (!user.equals(other.user))
			return false;
		return true;
	}
}