package security;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import model.AccountIF;

public class AccountAuthentication implements Authentication {

	private final AccountIF account;
	private boolean authenticated = true;
	
	public AccountAuthentication(AccountIF account) {
		this.account = account;
	}
	
	@Override
	public String getName() {
		return account.getEmail();
	}

	/**
	 * Die Authority Funktion von Spring Security wird noch nicht verwendet. 
	 * Deshalb wird hier nur eine leere Liste zurückgegeben.
	 * 
	 */
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return new ArrayList<GrantedAuthority>();
	}
	
	//Eventuell ROLE User vorgeben?
	@Override
	public Object getCredentials() {
		return account.getPassword();
	}

	@Override
	public AccountIF getDetails() {
		return account;
	}

	@Override
	public Object getPrincipal() {
		return account.getEmail();
	}

	@Override
	public boolean isAuthenticated() {
		return authenticated;
	}

	@Override
	public void setAuthenticated(boolean authenticated) throws IllegalArgumentException {
		this.authenticated = authenticated;

	}
}