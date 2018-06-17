package model.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

/**
 * TODO: Mapping auf Projekte anlegen
 *   
 * @author John
 */

@Entity
@Table(name="CATEGORY")
public class CategoryEntity {

	@Id
	@GeneratedValue
	private long id;
	
	@Column(name="TEXT")
	private String text;

	@ManyToMany(mappedBy = "categories")
	private Set<ProjectEntity> projects = new HashSet<ProjectEntity>();
	
	public CategoryEntity() {}
	
	public CategoryEntity(String text) {
		this.text = text;
	}

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

//	@Override
//	public int hashCode() {
//		final int prime = 31;
//		int result = 1;
//		result = prime * result + ((text == null) ? 0 : text.hashCode());
//		return result;
//	}
//
//	@Override
//	public boolean equals(Object obj) {
//		if (this == obj)
//			return true;
//		if (obj == null)
//			return false;
//		if (getClass() != obj.getClass())
//			return false;
//		CategoryEntity other = (CategoryEntity) obj;
//		if (text == null) {
//			if (other.text != null)
//				return false;
//		} else if (!text.equals(other.text))
//			return false;
//		return true;
//	}

	
	
}