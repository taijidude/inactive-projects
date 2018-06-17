package controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import exception.DataAlreadyExistsException;
import exception.DataNotFoundException;
import model.Account;
import model.AccountIF;
import model.Credential;
import model.CredentialIF;
import security.TokenAuthenticationServiceIF;
import service.AccountServiceIF;

/**
 * @author John
 */
@Controller
@EnableWebMvc
public class AccountController {
	
	public static final String TESTHALT = "/testhalt";
	public static final String URL_USER_REGISTER = "/v1/api/users/register";
	public static final String URL_USER_LOGIN = "/v1/api/users/login";
	
	@Autowired
	private AccountServiceIF service;
	
	@Autowired
	private TokenAuthenticationServiceIF tokenService;
	
	/**
	 * @throws DataAlreadyExistsException 
	 * Legt einen neuen Account an.
	 * @param Die Userdaten im Json Format im Requestbody
	 * @return Einen Token f�r den neuen User
	 * @throws
	 */
	@RequestMapping(value=URL_USER_REGISTER, method=RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public @ResponseBody JsonToken create(@RequestBody Account user) throws DataAlreadyExistsException {
		long createdId = service.create(user);
		user.setId(createdId);
		return (JsonToken) tokenService.createTokenForUser(user);
	}
	
	@RequestMapping(value=URL_USER_LOGIN, method=RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public @ResponseBody JsonToken login(){
		SecurityContext context = SecurityContextHolder.getContext();
		Authentication authentication = context.getAuthentication();
		JsonToken result = (JsonToken) service.createToken(authentication.getName());
		context.setAuthentication(null);
		return result;		
	}
	
	/**
	 * Aktualisiert einen Account.
	 * @param Die Userdaten im Json Format im Requestbody. 
	 * Beispiel:{"id":0,"birthyear":1991,"sex":"m",
	 * "username":"John","city":"berlin","password":"passwort",
	 * "email":"testmail@gmail.com"}
	 * @return
	 * @throws
	 */
	@RequestMapping(value="/account", method=RequestMethod.PUT)
	@ResponseStatus(HttpStatus.OK)
	public void update(@RequestBody Account user) {
		service.update(user);
	}
	
	/**
	 * Liest alle Accounts aus.  
	 * @return Eine Liste JSON Daten �ber alle Accounts.
	 * @param
	 * @throws DataNotFoundException 
	 */
	@RequestMapping(value="/account", method=RequestMethod.GET)
	public @ResponseBody List<AccountIF> getAll() throws DataNotFoundException {
		return service.getAll();
	}
	
	/**
	 * Liest einen Account anhand einer ID aus.
	 * @param Die Id des entsprechenden Accounts
	 * @return Die kompletten JSON Daten des Accounts
	 * Beispiel: {"id":0,"birthyear":1991,"sex":"m",
	 * "username":"John","city":"berlin","password":"passwort",
	 * "email":"testmail@gmail.com"}
	 * @throws DataNotFoundException 
	 */
	@RequestMapping(value="/account/{id}", method=RequestMethod.GET)
	public @ResponseBody AccountIF get(@PathVariable int id) throws DataNotFoundException {
		return(AccountIF) service.get(id);
	}
	
	@RequestMapping(value=TESTHALT, method=RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public @ResponseBody String testhalt() {
		return "klappt!";
	}
	
	public static final String URL_USER_DELETE = "/account/{id}";
	/**
	 * Einzelnen Nutzer anhand der ID l�schen 
	 * @param Die Id des Projekts
	 * @return
	 * @throws
	 */
	@RequestMapping(value=URL_USER_DELETE, method=RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.OK)
	public void delete(@PathVariable int id) {
		service.delete(id);
	}

	@ExceptionHandler(DataNotFoundException.class)
	@ResponseStatus(value=HttpStatus.NOT_FOUND)
	public void handleDataNotFoundException() {
		//Gibt nur einen Status zur�ck...
	}
	
	private static final String VIEW_INDEX = "index";
	@RequestMapping(value="/", method=RequestMethod.GET)
	public String welcome(ModelMap model) {
		model.addAttribute("message", "Welcome");
		return VIEW_INDEX;
	}
}