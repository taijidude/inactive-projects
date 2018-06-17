package config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import controller.AccountController;
import controller.DemoController;
import controller.ProjectController;
import security.CustomBasicAuthEntryPoint;
import security.RestAuthEntryPoint;
import security.TokenAuthenticationFilter;
import security.TokenAuthenticationService;
import security.TokenAuthenticationServiceIF;
import security.TokenAuthenticationSuccessHandler;


@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Configuration
	@Order(1)
	public static class FreeEndpointsConfig extends WebSecurityConfigurerAdapter {
		@Override
		protected void configure(HttpSecurity http) throws Exception {
			http
			.csrf().disable()
			.requestMatchers()
				.antMatchers(
							AccountController.URL_USER_REGISTER,
							DemoController.URL_USER_DEMO_SIGNUP,
							AccountController.TESTHALT,
							ProjectController.URL_PROJECT_CREATION
						).
			and().authorizeRequests().anyRequest().permitAll();
			http.requiresChannel().anyRequest().requiresSecure();
		}
	}

	@Configuration
	@Import(DaoConfig.class)
	@Order(2)
	public static class HttpBasicAuthConfig extends WebSecurityConfigurerAdapter {

		@Autowired
		private DataSource dataSource;
		
		@Bean
		public StandardPasswordEncoder passwordEncoder() {
			return new StandardPasswordEncoder("secret");
		}
		
		@Autowired
		public void configure(AuthenticationManagerBuilder auth) throws Exception { 
			auth
			.jdbcAuthentication()
			.dataSource(dataSource)
			.usersByUsernameQuery("select username, password, 1 from spring_lookup where username=?")
			.authoritiesByUsernameQuery("select username, 'user' from spring_lookup where username=?")
			.passwordEncoder(passwordEncoder());
		}
		
		@Override
		protected void configure(HttpSecurity http) throws Exception {
			http.requestMatchers().antMatchers(
					AccountController.URL_USER_LOGIN, 
					DemoController.URL_USER_DEMO_LOGIN
			).and().authorizeRequests()
			.anyRequest().hasAuthority("user")
			.and().exceptionHandling().authenticationEntryPoint(new CustomBasicAuthEntryPoint("RealmName"))
			.and().httpBasic()
			.and().csrf().disable();
			http.requiresChannel().anyRequest().requiresSecure();
		}
	}

	@Configuration
	@Order(3)
	public static class TokenAuthConfig extends WebSecurityConfigurerAdapter {

		@Bean 
		public TokenAuthenticationServiceIF tokenAuthenticationService() {
			return new TokenAuthenticationService("secret");
		}

		@Bean
		public TokenAuthenticationSuccessHandler successHandler() {
			return new TokenAuthenticationSuccessHandler();
		}

		@Bean
		@DependsOn("successHandler")
		public TokenAuthenticationFilter tokenAuthenticationFilter() throws Exception {
			TokenAuthenticationFilter result = new TokenAuthenticationFilter(tokenAuthenticationService());
			result.setAuthenticationManager(authenticationManager());
			return result;
		}

		@Override
		protected void configure(HttpSecurity http) throws Exception {
			RestAuthEntryPoint entryPoint = new RestAuthEntryPoint();
			http.requestMatchers().antMatchers(
					DemoController.URL_USER_TEST_TOKEN,
					DemoController.URL_DEMO_FILEUPLOAD
					)
			.and().authorizeRequests().anyRequest().authenticated().and()
			.exceptionHandling().authenticationEntryPoint(entryPoint).and()
			.csrf().disable()
			.addFilterBefore(new TokenAuthenticationFilter(tokenAuthenticationService()),UsernamePasswordAuthenticationFilter.class);
			http.requiresChannel().anyRequest().requiresSecure();
		}
	}
}