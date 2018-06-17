package model;

public interface CategoryIF {

	enum TYP {
		Fashion, Social, Produktdesign, Gadgets, Andere 
	}
	
	String getText();

	long getId();

}
