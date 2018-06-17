package security;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.core.Authentication;

import controller.JsonTokenIF;
import model.AccountIF;
import service.AccountServiceIF;

public interface TokenAuthenticationServiceIF {

	Authentication getAuthentication(HttpServletRequest request);

	JsonTokenIF createTokenForUser(AccountIF account);

	void setAccountService(AccountServiceIF accountService);

	AccountIF parseAccountFromToken(String token);

}
