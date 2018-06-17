package dao;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import config.DaoConfig;
import exception.DataAlreadyExistsException;
import exception.DataNotFoundException;
import model.Category;
import model.CategoryIF;
import model.entity.CategoryEntity;
import model.entity.HashtagEntity;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=DaoConfig.class)
public class CategoryDaoTest {

	@Autowired
	CategoryDaoIF dao;
	private CategoryIF category = new Category("FASHION");
	
	@Test
	@Transactional
	public void testPersist() throws DataNotFoundException, DataAlreadyExistsException {
		long id = dao.persist(category);
		CategoryIF retrievedCategory = dao.getById(id);
		assertEquals("Der Kategorie Text entspricht nicht den Erwartungen!", category.getText(), retrievedCategory.getText());
	}
	
	@Test
	@Transactional
	public void testGetEntityByString() throws Exception {
		dao.persist(category);
		String text = category.getText();
		CategoryEntity categoryEntity = dao.getEnityByString(text);
		assertEquals("Die Category entspricht nicht den Erwartungen!", categoryEntity.getText(), category.getText());
	}	
	
	@Test
	@Transactional
	public void testAlreadyExists() throws Exception {
		String categoryText = "FASHION";
		CategoryIF category = new Category(categoryText);
		dao.persist(category);
		assertTrue("Eine Kategory mit diesem Text wurde bereits angelegt", dao.alreadyExists(categoryText));
	}
	
	@Test
	@Transactional
	public void testAlreadyExistsDoesntExists() throws Exception {
		assertFalse("Die Kategorie sollte bislang nicht existieren!", dao.alreadyExists("FASHION"));
	}
	
	@Test
	@Transactional
	public void testDelete() throws Exception {
		Category category = new Category("FASHION");
		long id = dao.persist(category);
		dao.delete(id);
		try {
			dao.getById(id);
			fail("DataNotFoundException erwartet!");
		} catch(DataNotFoundException dataNotFoundException) {}
	}
	
	@Test
	@Transactional
	public void testIdGetByName() throws Exception {
		Category category = new Category("FASHION");
		long id = dao.persist(category);
		long idByText = dao.getIdByText("FASHION");
		assertEquals("Die beiden Id's sind nicht identisch!", id, idByText);
	}
}