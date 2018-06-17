package dao;

import exception.DataAlreadyExistsException;
import exception.DataNotFoundException;
import model.CategoryIF;
import model.entity.CategoryEntity;
import model.entity.HashtagEntity;

public interface CategoryDaoIF {

	long persist(CategoryIF category) throws DataAlreadyExistsException ; 

	CategoryIF getById(long id) throws DataNotFoundException;

	void delete(long id);

	long getIdByText(String text);

	boolean alreadyExists(String categoryText);

	CategoryEntity getEntityById(long id) throws DataNotFoundException;

	CategoryEntity getEnityByString(String text);

}