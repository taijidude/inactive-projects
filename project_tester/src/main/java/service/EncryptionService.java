package service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class EncryptionService implements EncryptionSeviceIF {
	
	@Autowired
	private StandardPasswordEncoder encoder;
	
	@Override
	public String encryptPassword(String password) {
		return encoder.encode(password);
	}
	
	
	
	
}