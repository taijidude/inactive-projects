package model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="LIKES")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class LikeEntity {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
	protected Long id;
	
	@ManyToOne
	protected AccountEntity account;
	
	public LikeEntity() {}
	
	public LikeEntity(AccountEntity account) {
		this.account = account;
	}

	public long getId() {
		return id;
	}

	public AccountEntity getAccount() {
		return account;
	}	
}