package dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import exception.DataNotFoundException;
import model.DetailLevel;
import model.Spot;
import model.SpotIF;
import model.entity.SpotEntity;

@Repository
public class SpotDao implements SpotDaoIF {

	@PersistenceContext
	private EntityManager em;

	
	@Override
	@Transactional
	public long persist(SpotIF spot) {
		SpotEntity spotEntity = new SpotEntity(
					   spot.getType(),
				       spot.getName(),
					   spot.getConcept(),
					   spot.getStreet(),
					   spot.getNumber(), 
					   spot.getZipCode(),
					   spot.getCity(), 
					   spot.getVisitTimes());
		em.persist(spotEntity);
		return spotEntity.getId();
	}

	@Override
	@Transactional
	public SpotIF getSpotById(long id) throws DataNotFoundException {
		Query query = em.createQuery("from SpotEntity se where se.id = :id");
		query.setParameter("id", id);
		
		try {
			SpotEntity spotEntity = (SpotEntity) query.getSingleResult();
			return new Spot(
					spotEntity.getType(),
					spotEntity.getName(), 
					spotEntity.getConcept(), 
					spotEntity.getStreet(), 
					spotEntity.getNumber(), 
					spotEntity.getZipCode(), 
					spotEntity.getCity(),
					spotEntity.getVisitTimes());
		} catch (NoResultException e) {
			throw new DataNotFoundException();
		}
	}

	@Override
	@Transactional
	public void deleteById(long id) {
		Query query = em.createQuery("delete from SpotEntity ae where ae.id = :parameter");
		query.setParameter("parameter", id);
		query.executeUpdate();
	}

	@Override
	@Transactional
	public List<SpotIF> getAllSpots() {
		Query query = em.createQuery("from SpotEntity");
		List<SpotIF> resultList = new ArrayList<>();
		List<SpotEntity> entityList = query.getResultList();
		entityList.forEach(se -> {
			resultList.add(new Spot(se.getType(),se.getName(), se.getConcept(), se.getStreet(), se.getNumber(), se.getZipCode(), se.getCity(), se.getVisitTimes()));
		});
		return resultList;
	}
	

	@Override
	@Transactional
	public void update(SpotIF spot) {
		SpotEntity spotEntity = em.find(SpotEntity.class, spot.getId());
		if(spotEntity != null) {
			spotEntity.setName(spot.getName());
			spotEntity.setConcept(spot.getConcept()); 
			spotEntity.setStreet(spot.getStreet()); 
			spotEntity.setNumber(spot.getNumber()); 
			spotEntity.setZipCode(spot.getZipCode()); 
			spotEntity.setCity(spot.getCity());
		}
	}

	@Override
	public List<SpotIF> getAllSpots(DetailLevel detailLevel) {
		List<SpotIF> allSpots = getAllSpots();
		if(detailLevel.equals(DetailLevel.FREE)) {
			allSpots.forEach(s -> {
				s.setType(null);
				s.setVisitTimes(null);
			});
		}
		return allSpots;
	}

}