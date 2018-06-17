package service;

import java.util.List;

import controller.JsonTokenIF;
import exception.DataAlreadyExistsException;
import exception.DataNotFoundException;
import model.AccountIF;

public interface AccountServiceIF {

	long create(AccountIF account) throws DataAlreadyExistsException;

	void update(AccountIF account);

	AccountIF get(long id) throws DataNotFoundException;

	void delete(int id);

	List<AccountIF> getAll() throws DataNotFoundException;

	AccountIF loadByName(String username);

	JsonTokenIF createToken(String name);
}