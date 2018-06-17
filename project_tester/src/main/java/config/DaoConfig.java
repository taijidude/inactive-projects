package config;

import java.io.File;
import java.util.Properties;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import dao.UploadDao;
import dao.UploadDaoIF;

@Configuration
@EnableTransactionManagement
@ComponentScan(basePackages="dao")
@PropertySource("classpath:config_dev.properties")
public class DaoConfig {

	@Autowired
	private Environment env;
	
	/**
	 * TODO:Das Upload Dao wird nicht über ComponentScan erzeugt, weil
	 * hier seperat der Uploadsfolder übergeben werden muss und ich nicht
	 * weiß ob das über ComponentScan geht. 
	 * @return
	 */
	@Bean
	public UploadDaoIF uploadDoa() {
		return new UploadDao(new File(env.getProperty("uploads.targetfolder")));
	}
	
	@Bean
	public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
		HibernateJpaVendorAdapter adapter = new HibernateJpaVendorAdapter();
		adapter.setShowSql(true);
		adapter.setGenerateDdl(true);
		adapter.setDatabase(Database.MYSQL);
		
		Properties properties = new Properties();
		properties.setProperty("hibernate.format_sql", "true");
		
		LocalContainerEntityManagerFactoryBean emfb = new LocalContainerEntityManagerFactoryBean();
		emfb.setDataSource(dataSource());
		emfb.setPackagesToScan("model.entity");
		emfb.setJpaProperties(properties);
		emfb.setJpaVendorAdapter(adapter);
		
		return emfb;
	}
		
	@Bean
	public PlatformTransactionManager transactionManager() {
		return new JpaTransactionManager();
	}
		
	@Bean 
	public DataSource dataSource() {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName(env.getProperty("database.driver"));
		dataSource.setUrl(env.getProperty("database.url"));
		dataSource.setUsername(env.getProperty("database.user"));
		dataSource.setPassword(env.getProperty("database.passwort"));
		return dataSource;
	}
	
	@Bean
	public JdbcTemplate jdbcTemplate() {
		return new JdbcTemplate(dataSource());
	}
}
