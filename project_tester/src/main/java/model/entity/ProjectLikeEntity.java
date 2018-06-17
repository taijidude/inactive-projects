package model.entity;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
@DiscriminatorValue(value="PROJECT")
public class ProjectLikeEntity extends LikeEntity {

	@ManyToOne
	private ProjectEntity project;
	
	public ProjectLikeEntity() {}
	
	public ProjectLikeEntity(ProjectEntity project, AccountEntity accountEntity){
		super(accountEntity);
		this.project = project;
	}

	public ProjectEntity getProject() {
		return project;
	}

	public void setProject(ProjectEntity project) {
		this.project = project;
	}
}