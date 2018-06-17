package model;

public class Account implements AccountIF {

	private long id;
	private String firstname;
	private String lastname;
	private String city;
	private String job;
	private String password; 
	private String email;
	
	public Account() {}
	
	/**
	 * Dieser Konstruktor sollte nur von vom Dao aus aufgerufen werden! 
	 * 
	 * @param id
	 * @param birthyear
	 * @param sex
	 * @param username
	 * @param city
	 * @param password
	 * @param email
	 */
	public Account(long id, String firstname, 
			String lastname, String city, 
			String job,	String email, 
			String password) {
		this.id = id;
		this.firstname = firstname;
		this.lastname = lastname;
		this.city = city;
		this.job = job;
		this.password = password;
		this.email = email;
	}
	
	/**
	 * Dieser Konstruktor bezieht die Id nicht mit ein. 
	 * Er kann deshalb auch in Unit Tests aufgerufen werden.
	 * 
	 * @param birthyear
	 * @param sex
	 * @param username
	 * @param city
	 * @param password
	 * @param email
	 */
	public Account(String firstname, 
			String lastname, String city, 
			String job, String password, 
			String email) {
		this.firstname = firstname;
		this.lastname = lastname;
		this.city = city;
		this.job = job;
		this.password = password;
		this.email = email;
	}

	public Account(String firstname, String lastname, String email, String password) {
		this.firstname = firstname;
		this.lastname = lastname;
		this.email = email;
		this.password = password;
	}

	@Override
	public long getId() {
		return id;
	}

	@Override
	public void setId(long id) {
		this.id = id;
	}

	@Override
	public String getFirstname() {
		return firstname;
	}

	@Override
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	@Override
	public String getLastname() {
		return lastname;
	}

	@Override
	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	@Override
	public String getCity() {
		return city;
	}

	@Override
	public void setCity(String city) {
		this.city = city;
	}

	@Override
	public String getJob() {
		return job;
	}

	@Override
	public void setJob(String job) {
		this.job = job;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String getEmail() {
		return email;
	}

	@Override
	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((city == null) ? 0 : city.hashCode());
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + ((firstname == null) ? 0 : firstname.hashCode());
		result = prime * result + ((job == null) ? 0 : job.hashCode());
		result = prime * result + ((lastname == null) ? 0 : lastname.hashCode());
		result = prime * result + ((password == null) ? 0 : password.hashCode());
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
		Account other = (Account) obj;
		if (city == null) {
			if (other.city != null)
				return false;
		} else if (!city.equals(other.city))
			return false;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		if (firstname == null) {
			if (other.firstname != null)
				return false;
		} else if (!firstname.equals(other.firstname))
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
		if (password == null) {
			if (other.password != null)
				return false;
		} else if (!password.equals(other.password))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Account [id=" + id + ", firstname=" + firstname + ", lastname=" + lastname + ", city=" + city + ", job="
				+ job + ", password=" + password + ", email=" + email + "]";
	}	
}