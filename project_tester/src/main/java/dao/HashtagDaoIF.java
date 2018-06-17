package dao;

import java.util.Set;

import exception.DataNotFoundException;
import model.HashtagIF;
import model.entity.HashtagEntity;

public interface HashtagDaoIF {

	long persist(String hashtag);

	Set<HashtagIF> getAll();

	HashtagIF get(long id) throws DataNotFoundException;

	void delete(long id);

	HashtagEntity getEntityById(long id) throws DataNotFoundException;

	HashtagEntity getEntityByString(String hashtag);

	Set<HashtagEntity> createIfNotExists(Set<String> hashTags);
}