package service;

import java.util.List;

import exception.DataNotFoundException;
import model.DetailLevel;
import model.SpotIF;

public interface SpotServiceIF {

	List<SpotIF> getAll();

	long persist(SpotIF spot);

	List<SpotIF> getAll(DetailLevel loggedIn);

	SpotIF getById(long id) throws DataNotFoundException;

	void delete(long id);

	void update(SpotIF spot);

}
