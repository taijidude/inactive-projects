package config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import security.CorsInterceptor;

@Configuration
@Import({DaoConfig.class, ServiceConfig.class, ControllerConfig.class})
@EnableWebMvc
public class RootContext extends WebMvcConfigurerAdapter {
	
	@Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new CorsInterceptor());
    }
	
	@Bean
	public ViewResolver simpleViewResolver() {
		InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
		viewResolver.setPrefix("/WEB-INF/pages/");
		viewResolver.setSuffix(".jsp");
		return viewResolver;
	}	
	
	/**
	 * 
	 * TODO: Die Größe von Bildern sollte maximal 2 MB sein. 
	 * TODO: Rausfinden, was macht MaxInMemorySize?
	 * 
	 * 
	 * @return
	 */
	@Bean
	public MultipartResolver multipartResolver() {
		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
		//Die Größe wird in bytes angegeben
		multipartResolver.setMaxUploadSize(20971520); // => 20 MB
		multipartResolver.setMaxInMemorySize(1048576);// => 1MB
		
		return multipartResolver;
	}
	
}