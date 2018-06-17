package service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dao.LikeDaoIF;
import exception.DataAlreadyExistsException;
import exception.DataNotFoundException;
import model.Like;
import model.LikeIF;
import model.LikeItemTyp;

@Service
public class LikeService implements LikeServiceIF {
    
    @Autowired
    private LikeDaoIF dao;

	@Override
	public long persistLike(LikeIF like) throws DataAlreadyExistsException, DataNotFoundException {
		if(dao.alreadyExists(like)) {
			throw new DataAlreadyExistsException();
		} 
		return dao.persistLike(like);
	}

	@Override
	public List<LikeIF> getLikesById(long itemId, LikeItemTyp itemTyp) throws DataNotFoundException {
		return dao.getLikesById(itemId, itemTyp);
	}

	@Override
	public void deleteLikeById(long likeId) {
		dao.deleteLikeById(likeId);
	}	
}