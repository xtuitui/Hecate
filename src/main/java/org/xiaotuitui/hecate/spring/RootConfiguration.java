package org.xiaotuitui.hecate.spring;

import java.util.Properties;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.xiaotuitui.hecate.application.impl.ServiceMarker;
import org.xiaotuitui.hecate.infrastructure.persistence.RepositoryMarker;

@Configuration
@PropertySource(name = "jdbcProperty", value = {"classpath:jdbc.properties"}, ignoreResourceNotFound = true)
@ImportResource(locations = {"classpath:spring/spring-dataSource.xml"})
@ComponentScan(basePackageClasses = {RepositoryMarker.class, ServiceMarker.class})
public class RootConfiguration {
	
	@Autowired
	private Environment environment;
	
	@Bean(name = "jpaVendorAdapter")
	public JpaVendorAdapter jpaVendorAdapter(){
		return new HibernateJpaVendorAdapter();
	}
	
	@Bean(name = "entityManagerFactory")
	public LocalContainerEntityManagerFactoryBean localContainerEntityManagerFactoryBean(DataSource dataSource, JpaVendorAdapter jpaVendorAdapter){
		LocalContainerEntityManagerFactoryBean localContainerEntityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
		localContainerEntityManagerFactoryBean.setDataSource(dataSource);
		localContainerEntityManagerFactoryBean.setPersistenceUnitName("persistenceUnit");
		localContainerEntityManagerFactoryBean.setPackagesToScan("org.xiaotuitui.hecate.domain.model");
		localContainerEntityManagerFactoryBean.setJpaVendorAdapter(jpaVendorAdapter);
		localContainerEntityManagerFactoryBean.setJpaProperties(createJpaProperties());
		return localContainerEntityManagerFactoryBean;
	}
	
	private Properties createJpaProperties() {
		Properties properties = new Properties();
		properties.put("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");
		properties.put("hibernate.hbm2ddl.auto", "none");
		properties.put("hibernate.enable_lazy_load_no_trans", "true");
		properties.put("hibernate.max_fetch_depth", "3");
		properties.put("hibernate.jdbc.fetch_size", "18");
		properties.put("hibernate.jdbc.batch_size", "10");
		properties.put("hibernate.show_sql", environment.getProperty("hibernate.show_sql"));
		properties.put("hibernate.format_sql", environment.getProperty("hibernate.format_sql"));
		return properties;
	}
	
	@Bean(name = "persistenceExceptionTranslationPostProcessor")
	public PersistenceExceptionTranslationPostProcessor persistenceExceptionTranslationPostProcessor(){
		return new PersistenceExceptionTranslationPostProcessor();
	}
	
	@Bean(name = "transactionManager")
	public JpaTransactionManager transactionManager(EntityManagerFactory entityManagerFactory){
		JpaTransactionManager transactionManager = new JpaTransactionManager();
		transactionManager.setEntityManagerFactory(entityManagerFactory);
		return transactionManager;
	}

}