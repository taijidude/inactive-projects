package security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

public class TokenAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

	private TokenAuthenticationSuccessHandler successHandler = new TokenAuthenticationSuccessHandler();
	
	private TokenAuthenticationServiceIF tokenAuthenticationService;

	public TokenAuthenticationFilter(TokenAuthenticationServiceIF tokenAuthenticationService) {
		super("/**");
		this.tokenAuthenticationService = tokenAuthenticationService;
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException, IOException, ServletException {
		Authentication authentication = tokenAuthenticationService.getAuthentication((HttpServletRequest) request);
		return authentication;
	}

	@Override
	public void setAuthenticationSuccessHandler(AuthenticationSuccessHandler successHandler) {
		super.setAuthenticationSuccessHandler(successHandler);
	}
	
	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		setAuthenticationSuccessHandler(successHandler);
		super.successfulAuthentication(request, response, chain, authResult);
		chain.doFilter(request, response);
	}
}