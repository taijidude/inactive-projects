package service;

import java.util.List;
import java.util.Map;

import exception.DataAlreadyExistsException;
import exception.DataNotFoundException;
import model.Like;
import model.LikeIF;
import model.LikeItemTyp;

public interface LikeServiceIF {

	long persistLike(LikeIF projectLike) throws DataAlreadyExistsException, DataNotFoundException;

	List<LikeIF> getLikesById(long itemId, LikeItemTyp itemTyp) throws DataNotFoundException;

	void deleteLikeById(long likeId);

	

	

}
