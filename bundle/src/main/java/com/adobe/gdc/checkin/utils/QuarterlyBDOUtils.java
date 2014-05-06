package com.adobe.gdc.checkin.utils;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.jcr.Node;
import javax.jcr.Property;
import javax.jcr.PropertyIterator;
import javax.jcr.RepositoryException;
import javax.jcr.Value;

import org.apache.sling.api.SlingHttpServletRequest;

import com.adobe.gdc.checkin.constants.QuartelyBDOConstants;

public class QuarterlyBDOUtils {

	/**
	 * This reads the request parameters and wrap it inside a HashMap
	 * @param request
	 * @return requestParamMap
	 */
	@SuppressWarnings("rawtypes")
	public static Map<String, String[]> readFormParams(SlingHttpServletRequest request)
	{
		
		Map<String,String[]> requestParamMap = new HashMap<String,String[]>();
		
		Enumeration paramNames = request.getParameterNames();
		while(paramNames.hasMoreElements()){
		    String name = (String) paramNames.nextElement();
		    
		    request.getParameterValues(name);
		    String[] value = request.getParameterValues(name);
		    requestParamMap.put(name, value);
		}
				
		return requestParamMap;
	}
		
	
	/**
	 * 
	 * @param node
	 * @param properties
	 * @throws RepositoryException
	 */
	public static void setNodeProperties(Node node, Map<String, String[]> properties) throws RepositoryException
	{	
		for(String key : properties.keySet()) {
			//Delete the existing properties 
			if(node.hasProperty(key))
				node.getProperty(key).remove();
			
			String[] propertyValues = properties.get(key);
			if(propertyValues!= null && propertyValues.length == 1) {
				node.setProperty(key, propertyValues[0]);
			}
			else {
				node.setProperty(key, propertyValues);
			}
		}	
	}
	
	public static Map<String, String[]> readNodeproperties(Node quarterlyBDONode) throws RepositoryException {
		
		Map<String, String[]> nodePropertyMap = new HashMap<String, String[]>();
		PropertyIterator propertyIterator = quarterlyBDONode.getProperties();
		
		while(propertyIterator.hasNext())
		 {
		 	Property property = propertyIterator.nextProperty(); 	
			String propertyName = property.getName();
		 	String[] propertyValue = {};
			if(!property.isMultiple())
			{
				propertyValue =  new String[] {property.getValue().getString()};
			}
			else
			{
				Value[] values = property.getValues();
				propertyValue = new String[values.length];
				for(int i=0; i<values.length;i++) 
					propertyValue[i] = values[i].getString();
			}
			
			nodePropertyMap.put(propertyName, propertyValue);
			}

		return nodePropertyMap;
	}
	
	/**
	 * 
	 * @param year
	 * @param quarterNumber
	 * @param user
	 * @return
	 */
	public static String getQuarterlyBDORepositoryPath(int year, int quarterNumber, String user) {
		
		String repositoryBasePath = QuartelyBDOConstants.GDC_CHECKIN_REPOSITORY_BASE_PATH +"/"+user+"/"+year+"/"+"Q"+quarterNumber;
		return repositoryBasePath;
	}
	
	public static String getEmployeeProfileBasePath(String user) {
		
		String repositoryBasePath = QuartelyBDOConstants.GDC_CHECKIN_REPOSITORY_BASE_PATH +"/"+user;
		return repositoryBasePath;
	}

}
