package dao;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetails;

import exception.DataAlreadyExistsException;
import exception.DataNotFoundException;
import model.AccountIF;
import model.CredentialIF;
import model.entity.AccountEntity;

public interface AccountDaoIF {

	AccountIF get(long id) throws DataNotFoundException;

	List<AccountIF> getAll() throws DataNotFoundException;

	void delete(long id);

	void update(AccountIF account);

	long persist(AccountIF account) throws DataAlreadyExistsException;

	AccountEntity getEntityById(long id) throws DataNotFoundException;

	AccountIF getAccountByName(String username);

	boolean alreadyExists(AccountIF user);

}