package security;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

/**
 * Ist das hier richtig? Oder sollte es einen AuthenticationProvider geben? 
 * 
 * 
 * @author John
 *
 */
public class RestAuthenticationManager implements AuthenticationManager {

	public RestAuthenticationManager() {
	}

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		return authentication;
	}

}
