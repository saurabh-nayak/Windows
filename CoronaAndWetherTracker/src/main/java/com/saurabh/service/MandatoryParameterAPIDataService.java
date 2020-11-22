package com.saurabh.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.saurabh.POJO.MandatoryParameterAPIData;
import com.saurabh.repository.MandatoryParameterAPIDataRepository;

@Service
public class MandatoryParameterAPIDataService 
{
	
	
	@Autowired
	MandatoryParameterAPIDataRepository repository;
	
  public MandatoryParameterAPIData retrieveAPIParam(String apiName)
  {
	  return repository.retrieveAPIParam(apiName);
  }
}
