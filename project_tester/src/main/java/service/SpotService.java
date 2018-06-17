package service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dao.SpotDaoIF;
import exception.DataNotFoundException;
import model.DetailLevel;
import model.SpotIF;

@Service
public class SpotService implements SpotServiceIF {

	@Autowired
	private SpotDaoIF dao;

	@Override
	public List<SpotIF> getAll() {
		return dao.getAllSpots();
	}

	@Override
	public long persist(SpotIF spot) {
		return dao.persist(spot);
	}

	@Override
	public List<SpotIF> getAll(DetailLevel detailLevel) {
		return dao.getAllSpots(detailLevel);
	}

	@Override
	public SpotIF getById(long id) throws DataNotFoundException {
		return dao.getSpotById(id);
	}

	@Override
	public void delete(long id) {
		dao.deleteById(id);
	}

	@Override
	public void update(SpotIF spot) {
		dao.update(spot);
	}	
}