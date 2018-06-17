package dao;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collector;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import exception.DataAlreadyExistsException;
import exception.DataNotFoundException;
import model.Account;
import model.AccountIF;
import model.CredentialIF;
import model.entity.AccountEntity;

@Repository
public class AccountDao implements AccountDaoIF {
	
	@PersistenceContext
	private EntityManager em;

	@Override
	@Transactional
	public long persist(AccountIF user) throws DataAlreadyExistsException {
		if(alreadyExists(user)) {
			throw new DataAlreadyExistsException();
		}
		AccountEntity entity = new AccountEntity(user.getFirstname(), user.getLastname(), user.getEmail(), user.getPassword());
		entity.setCity(user.getCity());
		entity.setJob(user.getCity());
			em.persist(entity);	
		return entity.getId();
	}
	
	@Override
	public boolean alreadyExists(AccountIF user) {
		return getAccountByName(user.getEmail()) != null;
	}
	
	@Override
	@Transactional
	public AccountIF get(long id) throws DataNotFoundException {
			AccountEntity singleResult = getEntityById(id);
			if(singleResult != null) {
				return new Account(singleResult.getId(), singleResult.getFirstname(), singleResult.getLastname()
				, singleResult.getCity(), singleResult.getJob(), singleResult.getEmail(), singleResult.getPassword());
			}
			return null;
	}
	
	@Override
	@Transactional
	public AccountEntity getEntityById(long id) throws DataNotFoundException {
		Query query = em.createQuery("from AccountEntity ae where ae.id = :parameter");
		query.setParameter("parameter", id);
		try {
			return (AccountEntity) query.getSingleResult();
		} catch (NoResultException nre) {
			throw new DataNotFoundException();
		}
	}

	@Override
	@Transactional
	public List<AccountIF> getAll() throws DataNotFoundException {
		List<AccountIF> result = new ArrayList<AccountIF>();
		try {
			Query query = em.createQuery("from AccountEntity");
			List<AccountEntity> resultList = (List<AccountEntity>)query.getResultList();
			for (AccountEntity entity : resultList) {
				result.add(
						new Account(entity.getId(), entity.getFirstname(), entity.getLastname(),
						entity.getCity(), entity.getJob(), entity.getEmail(), entity.getPassword()));
			}
			return result;
		} catch(NoResultException nre) {
			throw new DataNotFoundException();
		}
	}

	@Override
	@Transactional
	public void delete(long id) {
		Query query = em.createQuery("delete from AccountEntity ae where ae.id = :parameter");
		query.setParameter("parameter", id);
		query.executeUpdate();
	}

	/**
	 * Hier muss eine Exception fliegen, wenn der Account nicht zu finden ist.
	 */
	@Override
	@Transactional
	public void update(AccountIF user) {
		//Hierüber darf nicht das passwort zurück gesetzt werden
		AccountEntity accountEntity = em.find(AccountEntity.class, user.getId());
		if(accountEntity != null) {
			accountEntity.setFirstname(user.getFirstname());
			accountEntity.setLastname(user.getLastname());
			accountEntity.setJob(user.getJob());
			accountEntity.setCity(user.getCity());
			accountEntity.setEmail(user.getEmail());
		}
	}

	@Override
	@Transactional
	public AccountIF getAccountByName(String email) {
		try {
			Query query = em.createQuery("from AccountEntity where email = :email");
			query.setParameter("email", email);
			AccountEntity entity = (AccountEntity) query.getSingleResult();
			Account result = new Account(entity.getFirstname(), entity.getLastname(), entity.getEmail(), entity.getPassword());
			result.setId(entity.getId());
			result.setCity(entity.getCity());
			result.setJob(entity.getJob());
			return result;
		} catch (NoResultException e) {
			return null;
		}
	}
}