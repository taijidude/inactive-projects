package model;

public interface SpotIF {

	String getType();
	
	void setType(String type);
	
	String getName();

	void setName(String name);

	String getConcept();

	void setConcept(String concept);

	String getStreet();

	void setStreet(String street);

	String getNumber();

	void setNumber(String number);

	String getZipCode();

	void setZipCode(String zipCode);

	String getCity();

	void setCity(String city);

	long getId();
	
	void setId(long id);

	String getVisitTimes();

	void setVisitTimes(String visitTimes);
}