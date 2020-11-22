package com.saurabh.operation;

import java.io.IOException;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;

@Component
public class XMLParser 
{
	public Map<String,String> parse(String request)
	{
		XmlMapper mapper=new XmlMapper();
		Map<String, String> requestMap=null;
		try {
			requestMap = mapper.readValue(request, Map.class);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return requestMap;
	}
}
