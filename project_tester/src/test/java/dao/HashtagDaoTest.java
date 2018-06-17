package dao;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import config.DaoConfig;
import exception.DataNotFoundException;
import model.HashtagIF;
import model.entity.HashtagEntity;


/**
 * @author John
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=DaoConfig.class)
public class HashtagDaoTest {

	@Autowired
	private HashtagDaoIF dao;
	private String hashtag = "#test";
	
	@Test
	@Transactional
	public void testPersist() {
		Long id = dao.persist(hashtag);
		assertNotNull("Die Methode gibt eine ID zur�ck und ist nicht null...", id);
		assertFalse("Die id sollte nicht null sein", id.equals(0));
	}
	
	@Test
	@Transactional
	public void testGetEntityByString() throws Exception {
		dao.persist(hashtag);
		HashtagEntity entity = dao.getEntityByString(hashtag);
		assertNotNull("Das gesuchte hastag wurde nicht gefunden!", entity);
		assertEquals("Das Hashtag entspricht nicht den Erwartungen!", hashtag, entity.getText());
		entity = dao.getEntityByString("nicht vorhanden!");
		assertNull("Das hashtag wurde wieder erwarten gefunden!", entity);
	}
	
	@Test
	@Transactional
	public void testCreateIfNotExists() throws Exception {
		Set<HashtagEntity> hashtags = dao.createIfNotExists(new HashSet<String>(Arrays.asList("ht1", "ht2")));
		assertEquals("Es sollten zwei Enities zur�ckkommen!", 2, hashtags.size());	
	}
	
	@Test
	@Transactional
	public void testGetAll() {
		String h1 = "#t1";
		String h2 = "#t2";
		
		dao.persist(h1);
		dao.persist(h2);
		
		Set<HashtagIF> all = dao.getAll();
		assertNotNull("Die Liste soll nicht null sein!", all);
		assertFalse("Die Liste nicht leer sein!", all.isEmpty());
		for (HashtagIF tag : all) {
			String text = tag.getText();
			if(!text.equals(h1) && !text.equals(h2)) {
				fail("Hashtag Text entspricht nicht den Erwartungen!");
			}
		}
	}
	
	@Test
	@Transactional
	public void testGetbyId() throws DataNotFoundException {
		String h1 = "#t1";
		Long id = dao.persist(h1);
		HashtagIF hashtag = dao.get(id);
		assertEquals("Der Text des Hashtags sollte h1 entsprechen!", h1, hashtag.getText());
	}
	
	@Test
	@Transactional
	public void testDeleteById() throws Exception {
		String h1 = "#t1";
		long id = dao.persist(h1);
		dao.delete(id);
		try {
			dao.get(id);
			fail("DataNotFoundException erwartet!");
		} catch(DataNotFoundException exception) {}
		
	}
}