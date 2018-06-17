package service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dao.CategoryDaoIF;
import exception.DataAlreadyExistsException;
import exception.DataNotFoundException;
import model.Category;
import model.CategoryIF;

/**
 * @author John
 *
 */
@Service
public class CategoryService implements CategoryServiceIF {
    
    @Autowired
    private CategoryDaoIF dao;

	@Override
	public long persist(CategoryIF category) throws DataAlreadyExistsException {
		if(dao.alreadyExists(category.getText())) {
			throw new DataAlreadyExistsException();
		}
		return dao.persist(category);
	}

	@Override
	public CategoryIF get(long id) throws DataNotFoundException {
		return dao.getById(id);
	}

	@Override
	public void delete(long id) {
		dao.delete(id);
	}
}