package security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

public class RestAuthenticationFilter extends GenericFilterBean {

	private TokenAuthenticationServiceIF authenticationService;
	private List<String> openUrls = new ArrayList<String>();

	public RestAuthenticationFilter(TokenAuthenticationServiceIF tokenAuthenticationService, String... urlsToIgnore) {
		this.authenticationService = tokenAuthenticationService;
		for (String url : urlsToIgnore) {
			openUrls.add(url);
		} 
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		StringBuffer requestURL = httpRequest.getRequestURL();
		//Ist der richtige Weg hier einfach weiter zu machen? Oder sollte der Filterlauf abgebrochen werden?
		if(isOpenUrl(requestURL.toString())) {
			filterChain.doFilter(request, response);
		} else {
			Authentication authentication = authenticationService.getAuthentication(httpRequest);
			SecurityContextHolder.getContext().setAuthentication(authentication);
			filterChain.doFilter(request, response);
			SecurityContextHolder.getContext().setAuthentication(null);
		}
	}

	private boolean isOpenUrl(String requestURL) {
		for (String urlEnding : openUrls) {
			if(requestURL.endsWith(urlEnding)) {
				return true;
			}
		}
		return false;
	}
}