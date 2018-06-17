package model.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name="HASHTAG")
public class HashtagEntity {

	@Id
	@GeneratedValue
	private long id;
	
	@Column(name="TEXT")
	private String text;
	
	@ManyToMany(mappedBy = "hashtags")
	private Set<ProjectEntity> projects = new HashSet<ProjectEntity>(); 
	
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

	public Set<ProjectEntity> getProjects() {
		return projects;
	}

	public void setProjects(Set<ProjectEntity> projects) {
		this.projects = projects;
	}	
}