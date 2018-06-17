package service;

import static org.junit.Assert.fail;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import config.DaoConfig;
import config.ServiceConfig;
import exception.DataAlreadyExistsException;
import model.Category;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={DaoConfig.class, ServiceConfig.class})
public class CategoryServiceTest {

	@Autowired
	CategoryServiceIF service;
	
	@Test
	@Transactional
	public void testCategoryAlreadyExists() throws DataAlreadyExistsException {
		service.persist(new Category("FASHION"));
		try {
			service.persist(new Category("FASHION"));
			fail("DataAlreadyExistsException erwartet!");
		} catch(DataAlreadyExistsException alreadyExistsException) {}
	}
}