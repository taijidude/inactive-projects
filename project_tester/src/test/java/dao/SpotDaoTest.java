package dao;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import config.DaoConfig;
import exception.DataNotFoundException;
import model.DetailLevel;
import model.Spot;
import model.SpotIF;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=DaoConfig.class)
public class SpotDaoTest {

	@Autowired
	private SpotDaoIF spotDao;
	private SpotIF spot0;
	private Spot spot1;
	private Spot spot2;
	
	@Before
	public void setup() {
		spot0 = new Spot(
				"PARTNER",
				"Testspot1",
				"Ein Testspot...",
				"Teststraße 35",
				"2",
				"3----",
				"berlin", 
				"14 Uhr Dienstags");
		spot1 = new Spot(
				"PARTNER", 
				"Testspot2",
				"der zweite Testspot",
				"3----",
				"berlin","$5","$6", "Freitags ab 20 Uhr");		
		spot2 = new Spot(
				"PARTNER",
				"Testspot3",
				"Der dritte Testspot",
				"1",
				"3----",
				"berlin",
				"$6",
				 "M�rz und April");		
	}
	
	@Test
	public void persistCreateSpot() throws DataNotFoundException {
		long id = spotDao.persist(spot0);
		SpotIF retrievedSpot = spotDao.getSpotById(id);
		assertEquals("Der gespeicherte Spot stimmt nicht mit dem ausgelesenen Spot �berein!", spot0, retrievedSpot); 
	}
	
	@Test
	@Transactional
	public void testDeleteSpot() throws Exception {
		long id = spotDao.persist(spot0);
		try {
			spotDao.getSpotById(id);	
		} catch (DataNotFoundException e) {
			fail("Es wurde kein Spot zu dieser ID gefunden!");
		}
		
		spotDao.deleteById(id);
		try {
			spotDao.getSpotById(id);
			fail("Der Spot sollte sich nicht mehr in der Datenbank befinden. ");
		} catch (DataNotFoundException e) {}
	}
	
	@Test
	@Transactional
	public void testGetAllSpots() throws Exception {
		spotDao.persist(spot0);
		spotDao.persist(spot1);
		spotDao.persist(spot2);
		List<SpotIF> spots = spotDao.getAllSpots();
		assertEquals("Es wird eine Liste mit 3 Elementen erwartet!", 3, spots.size());
		assertTrue("Spot0 sollte in der Liste sein!", spots.contains(spot0));
		assertTrue("Spot1 sollte in der Liste sein!", spots.contains(spot1));
		assertTrue("Spot2 sollte in der Liste sein!", spots.contains(spot2));
		spots = spotDao.getAllSpots(DetailLevel.FREE);
		SpotIF spot = spots.get(0);
		assertNull("Die VisitTimes sollten null sein!",spot.getVisitTimes());
		spots = spotDao.getAllSpots(DetailLevel.TOKEN);
		spot = spots.get(0);
		assertNotNull("Die VisitTimes sollten nicht null sein!", spot.getVisitTimes());
	}
	
	@Test
	public void testUpdateSpot() throws Exception {
		long id = spotDao.persist(spot0);
		spot0.setId(id);
		String updatedText = "TESTCONCEPT";
		spot0.setConcept(updatedText);
		spotDao.update(spot0);
		SpotIF spotById = spotDao.getSpotById(id);
		assertEquals("Das erwartete Konzept wurde nicht gefunden!", updatedText, spotById.getConcept());
		spotDao.deleteById(id);
		try {
			spotDao.getSpotById(id);
			fail("DataNotFoundException");
		} catch (DataNotFoundException dnfe) {}
	}
	
}