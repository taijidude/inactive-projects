package model.view;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
/**
 * ggf redundant mit Account?
 * @author John
 *
 */
public class ViewAccount implements ViewAccountIF{

	private int id;
	private String firstname;
	private String lastname;
	private ViewUploadIF userimg;
	@JsonInclude(value=Include.NON_NULL)
	private String city;
	@JsonInclude(value=Include.NON_NULL)
	private String job;

	public ViewAccount(int id, String firstname, String lastname, ViewUploadIF userimg) {
		this.id = id;
		this.firstname = firstname;
		this.lastname = lastname;
		this.userimg = userimg;
	}

	public int getid() {
		return id;
	}

	public void setUid(int uid) {
		this.id = uid;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public ViewUploadIF getUserimg() {
		return userimg;
	}

	public void setUserimg(ViewUploadIF userimg) {
		this.userimg = userimg;
	}
	
	public void setCity(String city) {
		this.city = city;
	}

	public void setJob(String job) {
		this.job = job;
	}

	public String getCity() {
		return city;
	}

	public String getJob() {
		return job;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((city == null) ? 0 : city.hashCode());
		result = prime * result + ((firstname == null) ? 0 : firstname.hashCode());
		result = prime * result + id;
		result = prime * result + ((job == null) ? 0 : job.hashCode());
		result = prime * result + ((lastname == null) ? 0 : lastname.hashCode());
		result = prime * result + ((userimg == null) ? 0 : userimg.hashCode());
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
		ViewAccount other = (ViewAccount) obj;
		if (city == null) {
			if (other.city != null)
				return false;
		} else if (!city.equals(other.city))
			return false;
		if (firstname == null) {
			if (other.firstname != null)
				return false;
		} else if (!firstname.equals(other.firstname))
			return false;
		if (id != other.id)
			return false;
		if (job == null) {
			if (other.job != null)
				return false;
		} else if (!job.equals(other.job))
			return false;
		if (lastname == null) {
			if (other.lastname != null)
				return false;
		} else if (!lastname.equals(other.lastname))
			return false;
		if (userimg == null) {
			if (other.userimg != null)
				return false;
		} else if (!userimg.equals(other.userimg))
			return false;
		return true;
	}
}