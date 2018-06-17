package controller;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.*;


import config.RootContext;
import config.WebSecurityConfig;
import model.Account;
import model.AccountIF;
import model.Credential;
import security.TokenAuthenticationServiceIF;
/** 
 * @author John
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={RootContext.class, WebSecurityConfig.class} )
@WebAppConfiguration //Der Test wird wie eine WebApp �ber das Dispatcher Servlet geleitet
public class AccountControllerTest {

	@Autowired
	private StandardPasswordEncoder passwordEncoder;
	
	@Autowired 
	private FilterChainProxy springSecurityFilterChain;
	
	@Autowired
	private TokenAuthenticationServiceIF tokenService;
	
	@Autowired
	private WebApplicationContext wac;

	private MockMvc mockMvc;

	private Account user1;
	private Account user2;
	private Account user3;
	private ObjectMapper mapper = new ObjectMapper();
	String basicAccountUrl = "/account";
	String accountUrlWithId = "/account/{id}";

	@Before
	public void setup() {
		mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
		user1 = new Account("lutz","mustermann","test@test-mail.com","password");
		user2 = new Account("Susanne", "Musterman","test@test.com","x");
		user3 = new Account("Max","Musterman", "test123456789@test.com", "x");
	}

	@Test
	@Transactional
	public void testCreateUserGetUser() throws Exception {
		String userAsJson = mapper.writeValueAsString(user1);
		MockHttpServletRequestBuilder post = post(AccountController.URL_USER_REGISTER).content(userAsJson).contentType(MediaType.APPLICATION_JSON);
		MvcResult postResponse = mockMvc.perform(post).andExpect(status().isOk()).andReturn();
		String contentAsString = postResponse.getResponse().getContentAsString();
		AccountIF account = tokenService.parseAccountFromToken(contentAsString);
		assertEquals("Der User ist entspricht nicht dem erwarteten User!", account.getEmail(), user1.getEmail());	
	}

	@Test
	@Transactional
	public void testLogIn() throws Exception {
		//Es m�ssen Sicherheitsfeatures benutzt werden
		mockMvc = MockMvcBuilders
				.webAppContextSetup(wac)
				.addFilters(springSecurityFilterChain)
				.apply(springSecurity()).build();
		
		String userAsJson = mapper.writeValueAsString(user1);
		MockHttpServletRequestBuilder post = post(AccountController.URL_USER_REGISTER).content(userAsJson).contentType(MediaType.APPLICATION_JSON).secure(true);
		mockMvc.perform(post).andExpect(status().isOk());
		post = post(AccountController.URL_USER_LOGIN).with(httpBasic(user1.getEmail(),user1.getPassword())).secure(true);
		MvcResult loginResult = mockMvc.perform(post).andReturn();
		String contentAsString = loginResult.getResponse().getContentAsString();
		AccountIF account = tokenService.parseAccountFromToken(contentAsString);
		assertEquals("Der User ist entspricht nicht dem erwarteten User!", account.getEmail(), user1.getEmail());
	}
	
	private long getIdFromCreatedUser(MvcResult mvcResult) throws UnsupportedEncodingException {
		String token = mvcResult.getResponse().getContentAsString();
		return tokenService.parseAccountFromToken(token).getId();
	}
	
	@Test
	@Transactional
	public void testgetAllUsers() throws Exception {
		String user1AsJson = mapper.writeValueAsString(user1);
		String user2AsJson = mapper.writeValueAsString(user2);
		String user3AsJson = mapper.writeValueAsString(user3);
		MockHttpServletRequestBuilder postUser1 = post("/v1/api/users/register").content(user1AsJson).contentType(MediaType.APPLICATION_JSON);
		MockHttpServletRequestBuilder postUser2 = post("/v1/api/users/register").content(user2AsJson).contentType(MediaType.APPLICATION_JSON);
		MockHttpServletRequestBuilder postUser3 = post("/v1/api/users/register").content(user3AsJson).contentType(MediaType.APPLICATION_JSON);
		user1.setId(getIdFromCreatedUser(mockMvc.perform(postUser1).andReturn()));
		user2.setId(getIdFromCreatedUser(mockMvc.perform(postUser2).andReturn()));
		user3.setId(getIdFromCreatedUser(mockMvc.perform(postUser3).andReturn()));
		MockHttpServletRequestBuilder get = get(basicAccountUrl);
		MvcResult response = mockMvc.perform(get).andReturn();
		String responseText = response.getResponse().getContentAsString();
		
		List<Account> readValue = mapper.readValue(responseText, new TypeReference<List<Account>>() { });
		assertFalse("Die Liste sollte nicht leer sein!", readValue.isEmpty());
		String email1 = user1.getEmail();
		assertTrue("User1 sollte in der JsonListe enthalten sein!", filterListForUser(readValue, email1));
		String email2 = user2.getEmail();
		assertTrue("User2 sollte in der JsonListe enthalten sein!", filterListForUser(readValue, email2));
		String email3 = user3.getEmail();
		assertTrue("User3 sollte in der JsonListe enthalten sein!", filterListForUser(readValue, email3));
	}
	
	private boolean filterListForUser(List<Account> readValue, final String email) {
		Optional<Account> optional = readValue.
				stream().
				filter(a -> a.getEmail().equals(email)).
				findFirst();
		return optional.isPresent();
	}
	
	@Test
	public void testReadNonExistingUser() throws Exception {
		try {
			MockHttpServletRequestBuilder get = get(basicAccountUrl+"/{id}", 1);
			mockMvc.perform(get);
		} catch(HttpClientErrorException hcee) {
			assertEquals("Es wird ein 404 Fehler erwartet!","404 Not Found", hcee.getMessage());
		}
	}

	@Test
	public void testDeleteUserNonExistingUser() throws Exception {
		try {
			MockHttpServletRequestBuilder delete = delete(accountUrlWithId, 1); 
			mockMvc.perform(delete);
		} catch(HttpClientErrorException hcee) {
			assertEquals("Es wird ein 404 Fehler erwartet!","404 Not Found", hcee.getMessage());
		}
	}

	/**
	 * @throws Exception
	 */
	@Test
	@Transactional
	public void testDeleteUser() throws Exception {
		String user1AsJson = mapper.writeValueAsString(user1);
		MockHttpServletRequestBuilder post = post(basicAccountUrl).content(user1AsJson).contentType(MediaType.APPLICATION_JSON);
		mockMvc.perform(post);
		mockMvc.perform(delete(accountUrlWithId, 1)); 
		try {
			MockHttpServletRequestBuilder get = get(accountUrlWithId, 1);
			mockMvc.perform(get);
		} catch(HttpClientErrorException hcee) {
			assertEquals("Es wird ein 404 Fehler erwartet!","404 Not Found", hcee.getMessage());
		} 
	}

	@Test
	@Transactional
	public void UpdateUser() throws Exception {
		user1.setCity("berlin");
		String user1AsJson = mapper.writeValueAsString(user1);
		MockHttpServletRequestBuilder post = post("/v1/api/users/register").content(user1AsJson).contentType(MediaType.APPLICATION_JSON);
		MvcResult result = mockMvc.perform(post).andReturn();
		String token = result.getResponse().getContentAsString();
		AccountIF createdUser = tokenService.parseAccountFromToken(token);
		user1.setId(createdUser.getId());		
		user1.setCity("hamburg");
		user1AsJson = mapper.writeValueAsString(user1);
		MockHttpServletRequestBuilder put = put(basicAccountUrl).content(user1AsJson).contentType(MediaType.APPLICATION_JSON);
		mockMvc.perform(put);
		
		MockHttpServletRequestBuilder get = get(accountUrlWithId, createdUser.getId());
		MvcResult response = mockMvc.perform(get).andReturn();
		String contentAsString = response.getResponse().getContentAsString();
		Account updatedAccount = mapper.readValue(contentAsString, Account.class);		
		assertEquals("Es wird 'hamburg' als Stadt erwartet!", "hamburg", updatedAccount.getCity());
	} 
}