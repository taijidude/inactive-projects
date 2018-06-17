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

@Entity
@Table(name="SPOT")
public class SpotEntity {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
	private long id;
	@Column(name="TYPE")
	private String type;
	@Column(name="NAME")
	private String name;
	@Column(name="DESCRIPTION")
	private String concept;
	@Column(name="STREET")
	private String street;
	@Column(name="NUMBER")
	private String number;
	@Column(name="ZIP_CODE")
	private String zipCode;
	@Column(name="CITY")
	private String city;
	@Column(name="VISIT_TIMES")
	private String visitTimes;
		
	@OneToMany(mappedBy="spot")
	private Set<ProjectEntity> projects = new HashSet<ProjectEntity>(); 
	
	
	public SpotEntity() {}
	
	public SpotEntity(String type, String name, String concept, String street, String number, String zipCode,
			String city, String visitTimes) {
		this.type = type;
		this.name = name;
		this.concept = concept;
		this.street = street;
		this.number = number;
		this.zipCode = zipCode;
		this.city = city;
		this.visitTimes = visitTimes;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getConcept() {
		return concept;
	}

	public void setConcept(String concept) {
		this.concept = concept;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}
	
	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipcode) {
		this.zipCode = zipcode;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getVisitTimes() {
		return visitTimes;
	}

	public void setVisitTimes(String visitTimes) {
		this.visitTimes = visitTimes;
	}	
}