package service;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import config.DaoConfig;
import config.ServiceConfig;
import exception.DataAlreadyExistsException;
import exception.DataNotFoundException;
import model.Like;
import model.LikeIF;
import model.LikeItemTyp;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={DaoConfig.class, ServiceConfig.class})
public class LikeServiceTest {

	@Autowired
	private LikeServiceIF likeService;
	
	@Test
	@Transactional
	public void testProjectLikeAlreadyExists() throws DataAlreadyExistsException, DataNotFoundException {
		LikeIF projectLike = new Like(LikeItemTyp.PROJECT, 1, 1);
		likeService.persistLike(projectLike);
		try {
			likeService.persistLike(projectLike);
			fail("DataAlreadyExistsException erwartet!");
		} catch(DataAlreadyExistsException daee) {}
		
		
		
		
	}
}