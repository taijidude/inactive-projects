package model.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * Sollte hier die Id wieder im Constructor �bergeben werden?
 * @author John
 *
 */

@Entity
@Table(name="ACCOUNT")
public class AccountEntity {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
	private long id;
	@Column(name="city")
	private String city;
	@Column(name="password")
	private String password; 
	@Column(name="email")
	private String email;
	@Column(name="firstname")
	private String firstname;
	@Column(name="lastname")
	private String lastname;
	@Column(name="job")
	private String job;
	
	@OneToMany(mappedBy="account")
	private Set<ProjectEntity> projects = new HashSet<ProjectEntity>();
	
	@OneToMany(mappedBy="accountEntity")
	private Set<FeedbackEntity> feedback = new HashSet<FeedbackEntity>();
	
	@OneToMany(mappedBy="account")
	private Set<LikeEntity> likes = new HashSet<LikeEntity>();
	
	public AccountEntity() {}
	
	public AccountEntity(String firstname, String lastname, String email, String password) {
		this.firstname = firstname;
		this.lastname = lastname;
		this.email = email;
		this.password = password;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
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

	public String getJob() {
		return job;
	}

	public void setJob(String job) {
		this.job = job;
	}

	public Set<ProjectEntity> getProjects() {
		return projects;
	}

	public void setProjects(Set<ProjectEntity> projects) {
		this.projects = projects;
	}

	public Set<FeedbackEntity> getFeedback() {
		return feedback;
	}

	public void setFeedback(Set<FeedbackEntity> feedback) {
		this.feedback = feedback;
	}

	public Set<LikeEntity> getLikes() {
		return likes;
	}

	public void setLikes(Set<LikeEntity> likes) {
		this.likes = likes;
	}	
}