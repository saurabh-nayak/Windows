package com.saurabh.operation;



import java.util.ArrayList;
import java.util.Map;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import com.saurabh.Enum.ResponseConstant;
import com.saurabh.POJO.MandatoryParameterAPIData;
import com.saurabh.POJO.ResponseData;
import com.saurabh.service.MandatoryParameterAPIDataService;




@Component
public class ValidateMandatoryParameters 
{
	
	
	@Autowired
	MandatoryParameterAPIDataService service;
	
	Map<String, String> requestMap;
   
//	public ArrayList<Object> validateMandatoryParameters(Map<String, String> requestMap,String api)
//   {
//		ArrayList<Object> responseArray = new ArrayList<>();
//		MandatoryParameterAPIData apiParam=null;
//	   
//
//			   String param=apiParam.getMandatoryParameters();
//			   String[] paramArray=param.split(",");
//			   for(int i=0;i<paramArray.length;i++)
//			   {
//				   if(requestMap.get(paramArray[i])!=null && !requestMap.get(paramArray[i]).isEmpty())
//				   {
//					   if(i==paramArray.length-1)
//					   {
//						   responseArray.add(ResponseConstant.MANDATORY_PARAMETERS_OK.getErrorCode());
//	        		       responseArray.add(paramArray[i]);					   }
//				   }else {
//					   responseArray.add(ResponseConstant.MANDATORY_PARAMETERS_MISSING.getErrorCode());
//					   break;
//				   }			  
//			   }
//			   
//			   return responseArray;
//   }  
	
	public ArrayList<Object> mandatoryParameters(Map<String, String> requestMap,String api)
	   {
		ArrayList<Object> responseArray=new ArrayList<>();
			MandatoryParameterAPIData apiParam=null;
		   try {
		   apiParam=service.retrieveAPIParam(api);
		   }catch(Exception e)
		   { }
		   
		    if(apiParam!=null)
		    {
		    	String param=apiParam.getMandatoryParameters();
		    	responseArray=check(requestMap,param);

		    }else {
		    	responseArray.add(ResponseConstant.GET_MULTIPLE_API_PARAM_TO_VALIDATE.getErrorCode());
		    }
				   
				   return responseArray;
				   
			   
	   }
	
	public ArrayList<Object> check(Map<String, String> requestMap,String param)
	{
		ArrayList<Object> responseArray = new ArrayList<>();
		if(param.contains("/"))
    	{
//outer: for(int i=0;i<param.length();i++)
//        {   
           String subString1=param.trim().substring(param.lastIndexOf("/,")+2,param.length());
           String subString2= param.substring(0,param.lastIndexOf("/"));
           subString2=subString2.substring(0, subString2.lastIndexOf(",")).trim();
           String[] stringArray=subString1.trim().split(",");

        inner:	   for(int j=0;j<stringArray.length;j++)
           {
        	   if(requestMap.get(stringArray[j].trim())!=null && !requestMap.get(stringArray[j].trim()).isEmpty())
        	   {
        		   if(j==stringArray.length-1)
        		   {
//        			   response = new ResponseData(ResponseConstant.MANDATORY_PARAMETERS_OK.getErrorMessage(), ResponseConstant.MANDATORY_PARAMETERS_OK.getErrorCode());
        		       responseArray.add(ResponseConstant.MANDATORY_PARAMETERS_OK.getErrorCode());
        		       responseArray.add(stringArray[j]);
        		       break;
        		   }
        	   }
        	   else if(j==stringArray.length-1)
        	   {
        		   responseArray = check(requestMap,subString2);
        		  break inner;
        	   }
        	   else if(subString2!=null && !subString2.isEmpty()){
        		   responseArray =  check(requestMap,subString2);
        		   break inner;
        	   }
        	   else {
        		   responseArray.add(ResponseConstant.MANDATORY_PARAMETERS_MISSING.getErrorCode());
        		   break;
        	   }
           }
        }
	//}
	else {
		String[] stringArray=param.trim().split(",");
		for(int i=0;i<stringArray.length;i++)
		{
		if(requestMap.get(stringArray[i].trim())!=null && !requestMap.get(stringArray[i].trim()).isEmpty() )
 	   {
			if(i==stringArray.length-1)
			{
			   responseArray.add(ResponseConstant.MANDATORY_PARAMETERS_OK.getErrorCode());
			   responseArray.add(stringArray[i]);
			   break;
			   
			}
			else {
				continue;
			}
 	   }else {
 		  responseArray.add(ResponseConstant.MANDATORY_PARAMETERS_MISSING.getErrorCode());
 		  break;
 	   }
		}
		
	}
	return responseArray;
	}
	
//	
//	public ResponseData checkData(Map<String, String> requestMap,String param)
//	{
//		String[] arrayString=param.split("[,/]");
//		int result = -1;
//		for (String str : arrayString) {
//			if (!str.isEmpty() && str != null) {
//
//				if (requestMap.get(str) != null && !requestMap.get(str).isEmpty()) {
//					result = 1;
//				} else if (requestMap.get(str) == null | requestMap.get(str).isEmpty()) {
//					result = 0;
//				} else if (str == "/" && result == 1) {
//					response = new ResponseData(ResponseConstant.MANDATORY_PARAMETERS_OK.getErrorMessage(),
//							ResponseConstant.MANDATORY_PARAMETERS_OK.getErrorCode());
//				} else if (str == "/" && (result == 0 | result == -1)) {
//					continue;
//				} else {
//					response = new ResponseData(ResponseConstant.MANDATORY_PARAMETERS_MISSING.getErrorMessage(),
//							ResponseConstant.MANDATORY_PARAMETERS_MISSING.getErrorCode());
//				}
//			}else {
//				continue;
//			}
//		}
//		return response;
//	}
}
