package dao;

import java.util.List;

import exception.DataNotFoundException;
import model.DetailLevel;
import model.SpotIF;

public interface SpotDaoIF {

	long persist(SpotIF spot);

	SpotIF getSpotById(long id) throws DataNotFoundException;

	void deleteById(long id);

	List<SpotIF> getAllSpots(DetailLevel detailLevel);
	
	List<SpotIF> getAllSpots();

	void update(SpotIF spot);

}
