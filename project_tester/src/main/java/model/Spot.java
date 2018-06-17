package model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

public class Spot implements SpotIF {
	
	private String type;
	private String name;
	private String concept;
	private String street;
	private String number;
	private String zipCode;
	private String city;
	@JsonInclude(value=Include.NON_NULL)
	private String visitTimes;
	private long id;
	
	public Spot() {}
	
	public Spot(String type, String name, String concept, String street, String number, String zipCode,
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

	public Spot(String type, String name, String concept, String street, String number, String zipCode,
			String city, String visitTimes, long id) {
		this.type = type;
		this.name = name;
		this.concept = concept;
		this.street = street;
		this.number = number;
		this.zipCode = zipCode;
		this.city = city;
		this.visitTimes = visitTimes;
		this.id = id;
	}
	
	//Der Konstrukor wird beim ProjektUpload verwendet
	public Spot(String name, String street, String number, String zipCode, String city, String visitTimes) {
		this.name = name;
		this.street = street;
		this.number = number;
		this.zipCode = zipCode;
		this.city = city;
		this.visitTimes = visitTimes;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String getConcept() {
		return concept;
	}

	@Override
	public void setConcept(String concept) {
		this.concept = concept;
	}

	@Override
	public String getStreet() {
		return street;
	}

	@Override
	public void setStreet(String street) {
		this.street = street;
	}

	@Override
	public String getNumber() {
		return number;
	}

	@Override
	public void setNumber(String number) {
		this.number = number;
	}

	@Override
	public String getZipCode() {
		return zipCode;
	}

	@Override
	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
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
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((city == null) ? 0 : city.hashCode());
		result = prime * result + ((concept == null) ? 0 : concept.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((number == null) ? 0 : number.hashCode());
		result = prime * result + ((street == null) ? 0 : street.hashCode());
		result = prime * result + ((zipCode == null) ? 0 : zipCode.hashCode());
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
		Spot other = (Spot) obj;
		if (city == null) {
			if (other.city != null)
				return false;
		} else if (!city.equals(other.city))
			return false;
		if (concept == null) {
			if (other.concept != null)
				return false;
		} else if (!concept.equals(other.concept))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (number == null) {
			if (other.number != null)
				return false;
		} else if (!number.equals(other.number))
			return false;
		if (street == null) {
			if (other.street != null)
				return false;
		} else if (!street.equals(other.street))
			return false;
		if (zipCode == null) {
			if (other.zipCode != null)
				return false;
		} else if (!zipCode.equals(other.zipCode))
			return false;
		return true;
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
	public String getType() {
		return type;
	}

	@Override
	public void setType(String type) {
		this.type = type;
	}

	@Override
	public String getVisitTimes() {
		return visitTimes;
	}

	@Override
	public void setVisitTimes(String visitTimes) {
		this.visitTimes = visitTimes;
	}
}