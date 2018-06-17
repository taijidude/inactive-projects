package model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="UPLOAD")
public class UploadEntity {

	@Id
	@GeneratedValue
	private long id;
	
	@Column(name="ACCOUNT_ID")
	private long accountId;
	
	@Column(name="FILENAME")
	private String filename;
	
	@ManyToOne
	private ProjectEntity project;
	
	public UploadEntity() {}
	
	public UploadEntity(long accountId, String filename) {
		this.accountId = accountId;
		this.filename = filename;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public void setAccountId(long accountId) {
		this.accountId = accountId;
	}

	public long getAccountId() {
		return accountId; 
	}	
}