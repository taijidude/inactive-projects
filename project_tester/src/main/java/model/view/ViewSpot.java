package model.view;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

public class ViewSpot implements ViewSpotIF {

	private String spotCity;
	@JsonInclude(value=Include.NON_NULL)
	private String spotName; 
	@JsonInclude(value=Include.NON_NULL)
	private String visittimes;
	@JsonInclude(value=Include.NON_NULL)
	private String street;
	@JsonInclude(value=Include.NON_NULL)
	private int number;
	
	
	public ViewSpot(String spotCity) {
		this.spotCity = spotCity;
	}
	
	public ViewSpot(String spotCity, String spotName, String visittimes, String street, int number) {
		this.spotCity = spotCity;
		this.spotName = spotName;
		this.visittimes = visittimes;
		this.street = street;
		this.number = number;
	}

	public String getSpotCity() {
		return spotCity;
	}

	public void setSpotCity(String spotCity) {
		this.spotCity = spotCity;
	}

	public String getSpotName() {
		return spotName;
	}

	public void setSpotName(String spotName) {
		this.spotName = spotName;
	}

	public String getVisittimes() {
		return visittimes;
	}

	public void setVisittimes(String visittimes) {
		this.visittimes = visittimes;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + number;
		result = prime * result + ((spotCity == null) ? 0 : spotCity.hashCode());
		result = prime * result + ((spotName == null) ? 0 : spotName.hashCode());
		result = prime * result + ((street == null) ? 0 : street.hashCode());
		result = prime * result + ((visittimes == null) ? 0 : visittimes.hashCode());
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
		ViewSpot other = (ViewSpot) obj;
		if (number != other.number)
			return false;
		if (spotCity == null) {
			if (other.spotCity != null)
				return false;
		} else if (!spotCity.equals(other.spotCity))
			return false;
		if (spotName == null) {
			if (other.spotName != null)
				return false;
		} else if (!spotName.equals(other.spotName))
			return false;
		if (street == null) {
			if (other.street != null)
				return false;
		} else if (!street.equals(other.street))
			return false;
		if (visittimes == null) {
			if (other.visittimes != null)
				return false;
		} else if (!visittimes.equals(other.visittimes))
			return false;
		return true;
	}
}
