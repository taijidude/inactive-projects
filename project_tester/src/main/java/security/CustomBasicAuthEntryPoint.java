package security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;

/**
 * Dieser Endpoint sorgt daf�r, dass kein Auth Dialog auftaucht.
 * @author John
 */
public class CustomBasicAuthEntryPoint extends BasicAuthenticationEntryPoint {

	public CustomBasicAuthEntryPoint(String realmName) {
		setRealmName(realmName);
	}
	
	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException, ServletException {
		
		response.addHeader("WWW-Authenticate", "Basic realm=\"" +getRealmName() + "\"");		
		response.sendError(HttpServletResponse.SC_UNAUTHORIZED, authException.getMessage());
	}	
}