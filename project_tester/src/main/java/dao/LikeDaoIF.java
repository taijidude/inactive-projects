package dao;

import java.util.List;

import exception.DataNotFoundException;
import model.LikeIF;
import model.LikeItemTyp;
import model.entity.LikeEntity;

public interface LikeDaoIF {

	long persistLike(LikeIF like) throws DataNotFoundException;

	//FIXME: Sollte das hier nicht ein DTO sein!
	LikeEntity getEntityLikeById(long likeId) throws DataNotFoundException;

	void deleteLikeById(long likeId);

	boolean alreadyExists(LikeIF like) throws DataNotFoundException;
	
	List<LikeIF> getLikesById(long projectId, LikeItemTyp itemTyp) throws DataNotFoundException;
	List<LikeIF> getLikesByUserId(long userId) throws DataNotFoundException;

}