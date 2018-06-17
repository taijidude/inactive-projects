package service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import controller.JsonToken;
import controller.JsonTokenIF;
import dao.AccountDaoIF;
import exception.DataAlreadyExistsException;
import exception.DataNotFoundException;
import model.AccountIF;
import security.TokenAuthenticationServiceIF;

@Service
public class AccountService implements AccountServiceIF{

	@Autowired
	private AccountDaoIF dao;

	@Autowired
	private TokenAuthenticationServiceIF tokenAuthenticationService;

	@Autowired
	private EncryptionSeviceIF encryptionService;
	
	@Override
	public void update(AccountIF account) {
		dao.update(account);
	}
	
	//FIXME: Das verschlüsseln des passworts muss in den Controller verlagert werden
	@Override
	public long create(AccountIF account) throws DataAlreadyExistsException {
		account.setPassword(encryptionService.encryptPassword(account.getPassword()));
		return dao.persist(account);
	}
	
	@Override
	public AccountIF get(long id) throws DataNotFoundException{
		return dao.get(id);
	}
	
	@Override
	public void delete(int id) {
		dao.delete(id);
	}

	@Override
	public List<AccountIF> getAll() throws DataNotFoundException {
		return dao.getAll();
	}
	
	@Override
	public AccountIF loadByName(String username) {
		return dao.getAccountByName(username);
	}
	
	@Override
	public JsonTokenIF createToken(String name) {
		AccountIF account = loadByName(name);
		return tokenAuthenticationService.createTokenForUser(account);
	}
}