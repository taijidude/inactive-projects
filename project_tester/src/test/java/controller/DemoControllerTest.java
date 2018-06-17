package controller;

import static org.junit.Assert.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import javax.servlet.Filter;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;

import config.DaoConfig;
import config.RootContext;
import config.WebSecurityConfig;
import dao.AccountDaoIF;
import model.Account;
import model.AccountIF;
import security.TokenAuthenticationFilter;
import security.TokenAuthenticationServiceIF;
import service.AccountServiceIF;

/**
 * Tests um das grunds�tzliche Funktionieren der Authentifizierung 
 * sicher zu stellen.
 * 
 * @author John
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={RootContext.class, WebSecurityConfig.class, DaoConfig.class})
@WebAppConfiguration 
public class DemoControllerTest {

	@Autowired
	private TokenAuthenticationFilter filter;
	
	@Autowired
	private AccountServiceIF accountService;
	
	@Autowired
	private WebApplicationContext wac;
	
	@Autowired
	private TokenAuthenticationServiceIF tas;
	
	@Autowired 
	private FilterChainProxy springSecurityFilterChain;
	
	@Autowired
	private StandardPasswordEncoder standardPasswordEncoder;
	
	@Autowired
	private AccountDaoIF accountDao;
	
	private MockMvc mockMvc;

	private ObjectMapper mapper = new ObjectMapper();

	private Account account;

	@Before
	public void SetupContext() {		
		mockMvc = MockMvcBuilders
				.webAppContextSetup(wac)
				.addFilters(springSecurityFilterChain)
				.apply(springSecurity()).build();
		account = new Account("darth", "vader", "darth@vader.com", "deathstar");
	}
	
	@Test
	public void testSigInFree() throws Exception {
		MockHttpServletRequestBuilder post = post(DemoController.URL_USER_DEMO_SIGNUP).secure(true);
		MvcResult mvcResult = mockMvc.perform(post).andExpect(status().isOk()).andReturn();
		String contentAsString = mvcResult.getResponse().getContentAsString();
		assertEquals("Die erwartete Testausgabe wurde nicht gefunden!", "TEST1 /auth/signup -> Controller reached!", contentAsString);
	}
	
	@Test
	@Transactional
	public void testLoginHttpBasic() throws Exception {
		String passwordunEncrpyted = account.getPassword();
		account.setPassword(standardPasswordEncoder.encode(account.getPassword()));
		accountDao.persist(account);
		MockHttpServletRequestBuilder post = post(DemoController.URL_USER_DEMO_LOGIN).with(httpBasic(account.getEmail(),passwordunEncrpyted)).secure(true);
		MvcResult mvcResult = mockMvc.perform(post).andExpect(status().isOk()).andReturn();
		String token = mvcResult.getResponse().getContentAsString();
		assertNotNull("Es m�sste ein Token zur�ckkommen!",token);
		accountDao.delete(account.getId());
	}
	
	@Test
	@Transactional
	public void testAuthenticateToken() throws Exception {
		accountDao.persist(account);
		AccountServiceIF accountService = EasyMock.createMock(AccountServiceIF.class);
		EasyMock.expect(accountService.loadByName(account.getEmail())).andReturn(account);
		EasyMock.expect(accountService.loadByName(account.getEmail())).andReturn(account);
		tas.setAccountService(accountService);
		EasyMock.replay(accountService);
		
		
		String createTokenForUser = "Bearer "+tas.createTokenForUser(account);
		MockHttpServletRequestBuilder get = get(DemoController.URL_USER_TEST_TOKEN).header("Authorization", createTokenForUser).secure(true);
		MvcResult returnValue = mockMvc.perform(get).andExpect(status().isOk()).andReturn();
		MockHttpServletResponse response = returnValue.getResponse();
		String responseContent = response.getContentAsString();
		assertFalse("Der ResponseString darf nicht leer sein!", responseContent.isEmpty());
	}
	
	@Test
	public void testFileUpdload() throws Exception {
			
		RestTemplate restTemplate = new RestTemplate();
		String json = new StringBuilder()
		.append("{")
		.append("\"project\": [{")
		.append("id: 2,")
		.append("\"name\": \"Unterwasser Grill\",")
		.append("\"images\": [\"http://placehold.it/350x350\", \"http://placehold.it/350x350\"],")
		.append("\"categories\": [\"product design\", \"gadget\"],")
		.append("\"published\": \"2016-10-08T16:23:43+02:00\",")
		.append("\"duration\": 15,")
		.append("\"likes\": 0,")
		.append("\"description\": \"Lorem desription ipsum�.\",")
		.append("\"innovation\": \"Lorem innovation ipsum�.\",")
		.append("\"motivation\": \"Lorem motivation ipsum�.\",")
		.append("\"reward\": \"10 Unterwasser-W�rstchen\",")
		.append("\"hashtags\": [\"grill\", \"wasser\", \"design\"]")
		.append("}],")
		.append("\"spot\": [{")
		.append("\"owner\": \"\",")
		.append("\"city\": \"berlin\",")
		.append("\"application\": \"K14\",")
		.append("		        \"name\": \"K14\",")
		.append("		        \"street\": \"Konkordiastr.\",")
		.append("		        \"number\": \"Konkordiastr.\",")
		.append("		        \"zip\": \"30449\",")
		.append("		        \"times\": [\"Mo.-Fr. 08:00 bis 16:00\", \"Sa.: 10:00-12:00\"],")
		.append("		        \"accept_categories\": [\"Social\", \"Fashion\"]")
		.append("		    }]")
		.append("		}")
		.toString();
		
		MultiValueMap<String, Object> multipartRequest = new LinkedMultiValueMap<>();
		 
		HttpHeaders jsonHeader = new HttpHeaders();
		jsonHeader.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> jsonEntity = new HttpEntity<>(json, jsonHeader);

		Path path = Paths.get("src/test/java/china-information.jpg");
		byte[] picture = Files.readAllBytes(path);
		
		HttpHeaders pictureHeader = new HttpHeaders();
		pictureHeader.setContentType(MediaType.IMAGE_PNG);
		HttpEntity<ByteArrayResource> pictureEntity = new HttpEntity<>(new ByteArrayResource(picture), pictureHeader);
		 
		multipartRequest.add("myAwesomeJsonData", jsonEntity);
		multipartRequest.add("files", pictureEntity);
		
		HttpHeaders multipartHeader = new HttpHeaders();
		multipartHeader.setContentType(MediaType.MULTIPART_FORM_DATA);
		
		HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(multipartRequest, multipartHeader);
		ResponseEntity<String> entity = restTemplate.postForEntity("http://localhost:8080/rest-api/demo/file", requestEntity, String.class);
		
		HttpHeaders headers = entity.getHeaders();
		assertFalse("Es m�ssen Header zur�ckgeliefert werden!",headers.isEmpty());
	}
}