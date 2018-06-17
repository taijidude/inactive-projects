package service;

import java.util.Set;

import exception.DataNotFoundException;
import model.Hashtag;
import model.HashtagIF;

public interface HashTagServiceIF {

	long persist(String hashtag);

	Hashtag get(long id) throws DataNotFoundException;

	Set<HashtagIF> getAll();

	void delete(long id);
}
