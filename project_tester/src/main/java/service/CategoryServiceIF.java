package service;

import exception.DataAlreadyExistsException;
import exception.DataNotFoundException;
import model.CategoryIF;

public interface CategoryServiceIF {

	long persist(CategoryIF category) throws DataAlreadyExistsException;

	CategoryIF get(long id) throws DataNotFoundException;

	void delete(long id);

}
