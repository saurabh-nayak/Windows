package com.saurabh.CoronaAndWetherTracker;



import java.time.Duration;
import java.util.Locale;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

import com.saurabh.repository.MandatoryParameterAPIDataRepository;
import com.saurabh.repository.UserDetailRepository;
import com.saurabh.repository.UserRepository;

@EnableJpaRepositories(basePackageClasses = {UserRepository.class, UserDetailRepository.class, MandatoryParameterAPIDataRepository.class})
@SpringBootApplication(
//		scanBasePackages  = { "com.saurabh.controller" ,"com.saurabh.POJO" ,"com.saurabh.service", 
//		"com.saurabh.Enum", "com.saurabh.filter", "com.saurabh.CustomException",
//        "com.saurabh.repository" , "com.saurabh.operation" , "com.saurabh.springSecurityConfig",
//        "com.saurabh.CoronaAndWetherTracker"}
		)


@ComponentScan(basePackages={ "com.saurabh.controller" ,"com.saurabh.internationalization", "com.saurabh.POJO" ,"com.saurabh.service", 
		"com.saurabh.Enum", "com.saurabh.filter", "com.saurabh.CustomException",
        "com.saurabh.repository" , "com.saurabh.operation" , "com.saurabh.springSecurityConfig",
        "com.saurabh.CoronaAndWetherTracker"} )
@EntityScan({"com.saurabh.POJO"})


public class MyBootApplication {

	public static void main(String[] args) {
		SpringApplication.run(MyBootApplication.class, args);
}
	@Bean
	 RestTemplate getRestTemplate(RestTemplateBuilder templateBuilder)
	{
		return templateBuilder.setConnectTimeout(Duration.ofMillis(5000)).build();
	}
	
	@Bean  
	public  LocaleResolver localeResolver()  
	{  
		AcceptHeaderLocaleResolver localeResolver = new AcceptHeaderLocaleResolver();  
//	SessionLocaleResolver localeResolver = new SessionLocaleResolver();  
	localeResolver.setDefaultLocale(Locale.US);  
	return localeResolver;  
	} 
	
	}


