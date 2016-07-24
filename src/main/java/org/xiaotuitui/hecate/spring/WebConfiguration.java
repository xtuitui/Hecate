package org.xiaotuitui.hecate.spring;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;
import org.xiaotuitui.hecate.interfaces.ControllerMarker;

@Configuration
@EnableWebMvc
@ComponentScan(basePackageClasses = {ControllerMarker.class})
public class WebConfiguration extends WebMvcConfigurerAdapter{
	
	@Bean(name = "requestMappingHandlerMapping")
	public RequestMappingHandlerMapping requestMappingHandlerMapping(){
		RequestMappingHandlerMapping requestMappingHandlerMapping = new RequestMappingHandlerMapping();
		return requestMappingHandlerMapping;
	}
	
	@Bean(name = "requestMappingHandlerAdapter")
	public RequestMappingHandlerAdapter requestMappingHandlerAdapter(StringHttpMessageConverter stringHttpMessageConverter, MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter){
		RequestMappingHandlerAdapter requestMappingHandlerAdapter = new RequestMappingHandlerAdapter();
		List<HttpMessageConverter<?>> messageConverters = new ArrayList<HttpMessageConverter<?>>();
		messageConverters.add(stringHttpMessageConverter);
		messageConverters.add(mappingJackson2HttpMessageConverter);
		requestMappingHandlerAdapter.setMessageConverters(messageConverters);
		return requestMappingHandlerAdapter;
	}
	
	public void configureDefaultServletHandling(DefaultServletHandlerConfigurer defaultServletHandlerConfigurer) {
		defaultServletHandlerConfigurer.enable();
	}
	
	@Bean(name = "mappingJacksonHttpMessageConverter")
	public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter(){
		MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter();
		mappingJackson2HttpMessageConverter.setSupportedMediaTypes(mediaTypes());
		return mappingJackson2HttpMessageConverter;
	}
	
	@Bean(name = "stringHttpMessageConverter")
	public StringHttpMessageConverter stringHttpMessageConverter(){
		StringHttpMessageConverter stringHttpMessageConverter = new StringHttpMessageConverter();
		stringHttpMessageConverter.setSupportedMediaTypes(mediaTypes());
		return stringHttpMessageConverter;
	}
	
	private List<MediaType> mediaTypes(){
		List<MediaType> supportedMediaTypes = new ArrayList<MediaType>();
		supportedMediaTypes.add(MediaType.valueOf(MediaType.TEXT_HTML_VALUE + ";charset=UTF-8"));
		supportedMediaTypes.add(MediaType.APPLICATION_JSON_UTF8);
		return supportedMediaTypes;
	}
	
	@Bean(name = "viewResolver")
	public ViewResolver internalResourceViewResolver(){
		InternalResourceViewResolver internalResourceViewResolver = new InternalResourceViewResolver();
		internalResourceViewResolver.setViewClass(JstlView.class);
		internalResourceViewResolver.setPrefix("/WEB-INF/pages");
		internalResourceViewResolver.setSuffix(".jsp");
		return internalResourceViewResolver;
	}
	
	@Bean(name = "multipartResolver")
	public MultipartResolver commonsMultipartResolver(){
		CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver();
		commonsMultipartResolver.setDefaultEncoding("UTF-8");
		commonsMultipartResolver.setMaxUploadSize(32505856);
		commonsMultipartResolver.setMaxInMemorySize(4096);
		return commonsMultipartResolver;
	}
	
	@Bean(name = "messageSource")
	public MessageSource messageSource(){
		ResourceBundleMessageSource resourceBundleMessageSource = new ResourceBundleMessageSource();
		resourceBundleMessageSource.setBasename("i18n/message");
		return resourceBundleMessageSource;
	}
	
	@Bean(name = "localeResolver")
	public LocaleResolver localeResolver(){
		SessionLocaleResolver sessionLocaleResolver = new SessionLocaleResolver();
		sessionLocaleResolver.setDefaultLocale(Locale.CHINA);
		return sessionLocaleResolver;
	}

}