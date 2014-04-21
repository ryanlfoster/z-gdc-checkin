package com.adobe.gdc.checkin.utils;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import org.apache.sling.api.SlingHttpServletRequest;

public class QuarterlyBDOUtils {

	/**
	 * This reads the request paramters and wrap it inside a HashMap
	 * @param request
	 * @return requestParamMap
	 */
	@SuppressWarnings("rawtypes")
	public static Map<String, String> readFormParams(SlingHttpServletRequest request)
	{
		
		Map<String,String> requestParamMap = new HashMap<String,String>();
		Enumeration paramNames = request.getParameterNames();
		while(paramNames.hasMoreElements()){
		    String name = (String) paramNames.nextElement();
		    String value = request.getParameter(name);		  
		    requestParamMap.put(name, value);
		}
				
		return requestParamMap;
	}

}
