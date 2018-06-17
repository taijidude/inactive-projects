package dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import config.DaoConfig;
import exception.DataAlreadyExistsException;
import exception.DataNotFoundException;
import model.Account;
import model.AccountIF;


/**
 * @author John
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=DaoConfig.class)
public class AccountDaoTest {

	@Autowired
	private AccountDaoIF dao;

	private AccountIF user1;
	private AccountIF user2;
	private AccountIF user3;


	@Before
	public void setup() {
		user1 = new Account("John","meier","testmail@gmail.com", "x");
		user2 = new Account("Susanne","Musterman","test@test.com","x");
		user3 = new Account("Max","Musterman","test123456789@test.com", "x");
	}

	@Transactional
	@Test
	public void testCreateGet() throws DataNotFoundException, DataAlreadyExistsException {
		long persistedId = dao.persist(user1);
		AccountIF retrievedUser = dao.get(persistedId);
		assertEquals("Der User entspricht nicht den Erwartungen!", user1, retrievedUser);
	}

	@Test
	@Transactional
	public void testAlreadyExists() throws DataAlreadyExistsException {
		dao.persist(user1);
		assertTrue("Der User sollte in der Datenbank gefunden werden!", dao.alreadyExists(user1));
	}
	
	@Test
	@Transactional
	public void testCreateSameEmailTwice() throws Exception {
		dao.persist(user1);
		try {
			dao.persist(user1);			
			fail("DataAlreadyExistsException erwartet!");
		} catch(DataAlreadyExistsException daee) {
		}
	}

	@Test
	@Transactional
	public void testGetAll() throws DataNotFoundException, DataAlreadyExistsException {
		dao.persist(user1);
		dao.persist(user2);
		dao.persist(user3);
		List<AccountIF> allUser = dao.getAll();

		assertEquals("Die beiden User sollten identisch sein!", allUser.get(2).getEmail(), user3.getEmail());

		assertFalse("Die Userliste sollte nicht leer sein!", allUser.isEmpty());
		assertEquals("Es werden drei User erwartet!", allUser.size(), 3);

		assertTrue("User3 befindet sich nicht in der Liste!", allUser.contains(user3));
		assertTrue("User2 befindet sich nicht in der Liste!", allUser.contains(user2));
	}

	@Test
	@Transactional
	public void testDelete() throws DataNotFoundException, DataAlreadyExistsException {
		long id = dao.persist(user1);
		AccountIF user = dao.get(id);
		assertNotNull("Der user sollte nicht null sein!", user);
		assertEquals("Der User sollte mit user1 gleich sein!", user, user1);
		dao.delete(id);
		try {
			dao.get(1);
			fail("DataNotFoundException erwartet!");
		} catch(DataNotFoundException dataNotFoundException){}
	}

	@Test
	@Transactional
	public void testUpdate() throws DataNotFoundException, DataAlreadyExistsException {
		user1.setCity("berlin");
		long id = dao.persist(user1);
		user1.setId(id);
		AccountIF retrievedAccount1 = dao.get(id);
		assertEquals("Als Stadt wird berlin erwartet!", "berlin", retrievedAccount1.getCity());
		retrievedAccount1.setCity("hamburg");
		dao.update(user1);
		AccountIF retrievedAccount2 = retrievedAccount1;
		assertEquals("Als Stadt wird hamburg erwartet!", "hamburg", retrievedAccount2.getCity());
	}

	@Test
	@Transactional
	public void testGetUserBylangname() throws DataAlreadyExistsException {
		dao.persist(user1);
		dao.persist(user2);
		dao.persist(user3);
		AccountIF account = dao.getAccountByName(user1.getEmail());
		assertNotNull("Der abgefragte Account darf nicht null sein", account);
		assertEquals("Der abgefragte Account sollte user1 entsprechen!", user1, account);
	}
}