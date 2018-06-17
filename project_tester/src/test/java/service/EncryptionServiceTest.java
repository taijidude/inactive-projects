package service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import config.RootContext;
import config.ServiceConfig;
import config.WebSecurityConfig;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={RootContext.class, WebSecurityConfig.class, ServiceConfig.class})
@WebAppConfiguration 
public class EncryptionServiceTest {

	@Autowired
	private EncryptionSeviceIF service;
	
	@Test
	public void test() {
		System.err.println(service.encryptPassword("test")); 
	}

}
