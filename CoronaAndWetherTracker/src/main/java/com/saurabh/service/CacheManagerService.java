package com.saurabh.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.saurabh.operation.JSONParser;

@Service
public class CacheManagerService
{
	@Autowired
	JSONParser jsonParser;
	Map<String,Map<String,String>> cache=new HashMap<>();
  public void setCache(String key,String value)
  {
	  Map<String,String> cacheValue= jsonParser.parse(value);
	  cache.putIfAbsent(key, cacheValue);
  }
  
  public Object cacheIsAvailable(String key)
  {
	  if(cache.containsKey(key))
	  {
		  return cache.get(key);
	  }
	  else {
		  return false;
	  }
  }
}
