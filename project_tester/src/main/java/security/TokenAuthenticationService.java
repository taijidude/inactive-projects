package security;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import controller.JsonToken;
import controller.JsonTokenIF;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import model.AccountIF;
import service.AccountServiceIF;

@Service
public class TokenAuthenticationService implements TokenAuthenticationServiceIF{
	private static final String AUTH_HEADER_NAME = "Authorization";

	private String secret;
	private AccountServiceIF accountService;

    public TokenAuthenticationService(String secret) {
		this.secret = secret;
    }
    
    @Override
    public Authentication getAuthentication(HttpServletRequest request) {
        String header = request.getHeader(AUTH_HEADER_NAME);
		final String token = header.replace("Bearer ", "");
        if (token != null) {
        	AccountIF account = parseAccountFromToken(token);
            if (account != null) {
                return new AccountAuthentication(account);
            }
        }
        return null;
    }

    @Override
    public AccountIF parseAccountFromToken(String token) {
		String username = Jwts.parser()
				.setSigningKey(secret)
				.parseClaimsJws(token)
				.getBody()
				.getSubject();
		return accountService.loadByName(username);
	}

	@Override
    public JsonTokenIF createTokenForUser(AccountIF account) {
		Date now = new Date();
		Date expiration = new Date(now.getTime() + TimeUnit.HOURS.toMillis(1l));
		return new JsonToken(Jwts.builder()
				.setSubject(account.getEmail())
				.setId(""+account.getId())
				.setIssuedAt(now)
				.setExpiration(expiration)
				.signWith(SignatureAlgorithm.HS256, secret)
				.compact());
	}

	@Override
	@Autowired
	public void setAccountService(AccountServiceIF accountService) {
		this.accountService = accountService;
	}
}