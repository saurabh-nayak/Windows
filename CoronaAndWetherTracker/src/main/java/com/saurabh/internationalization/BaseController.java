package com.saurabh.internationalization;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcProperties.LocaleResolver;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BaseController 
{
	@Autowired
	MessageSource messageSource;
	@RequestMapping(value="/internationalize" , method = RequestMethod.GET)
 public String internationalized(
//		 @RequestHeader(name = "Accept-Language",required = false) Locale local
		 )
 {
//		 return messageSource.getMessage("my.message", null, local);
	 return messageSource.getMessage("my.message", null, LocaleContextHolder.getLocale());
 }
	
}
