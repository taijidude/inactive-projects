package service;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dao.HashtagDaoIF;
import exception.DataNotFoundException;
import model.Hashtag;
import model.HashtagIF;

@Service
public class HashTagService implements HashTagServiceIF {

	@Autowired
	private HashtagDaoIF dao;
	
	@Override
	public long persist(String hashtag) {
		return dao.persist(hashtag);
	}

	@Override
	public Hashtag get(long id) throws DataNotFoundException {
		return (Hashtag) dao.get(id);
	}

	@Override
	public Set<HashtagIF> getAll() {
		return dao.getAll();
	}

	@Override
	public void delete(long id) {
		dao.delete(id);
	}
}