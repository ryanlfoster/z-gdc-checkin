package com.adobe.gdc.checkin.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import javax.jcr.Node;
import javax.jcr.Property;
import javax.jcr.PropertyIterator;
import javax.jcr.RepositoryException;
import javax.jcr.Value;

import org.apache.commons.lang.StringUtils;
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
		    String[] value = request.getParameterValues(name);
		    requestParamMap.put(name, value);
		}

		return requestParamMap;
	}


	@SuppressWarnings("rawtypes")
	public static Map<String, String> readFormParamStrings(SlingHttpServletRequest request)
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
	
	public static void setNodeStringProperties(Node node, Map<String, String> properties) throws RepositoryException
	{	
		for(String key : properties.keySet()) {
			//Delete the existing properties 
			if(node.hasProperty(key))
				node.getProperty(key).remove();
			String propertyValue = properties.get(key);
			node.setProperty(key, propertyValue);	
		}	
	}
	
	
	public static void setNodeDateProperties(Node node, Map<String, Calendar> properties) throws RepositoryException
	{	
		for(String key : properties.keySet()) {
			//Delete the existing properties 
			if(node.hasProperty(key))
				node.getProperty(key).remove();
			Calendar propertyValue = properties.get(key);
			node.setProperty(key, propertyValue);	
		}	
	}
	
	public static Map<String, String[]> readNodeproperties(Node quarterlyBDONode, boolean escapeNewline) throws RepositoryException {

		Map<String, String[]> nodePropertyMap = new HashMap<String, String[]>();
		PropertyIterator propertyIterator = quarterlyBDONode.getProperties();

		while(propertyIterator.hasNext())
		 {
		 	Property property = propertyIterator.nextProperty(); 	
			String propertyName = property.getName();
		 	String[] propertyValue = {};
			if(!property.isMultiple())
			{
				if(escapeNewline) {
					propertyValue =  new String[] {property.getValue().getString().replace("\n","<br>\\")};
				}
				else {
					propertyValue =  new String[] {property.getValue().getString()};
				}
			}
			else
			{
				Value[] values = property.getValues();
				propertyValue = new String[values.length];
				for(int i=0; i<values.length;i++) {
					if(escapeNewline) {
						propertyValue[i] = values[i].getString().replace("\n","<br>\\");
					}
					else {
						propertyValue[i] = values[i].getString();
					}
				}
			}

			nodePropertyMap.put(propertyName, propertyValue);
			}

		return nodePropertyMap;
	}


	public static Map<String, Calendar> readCalendarProperties(Node bdoCalendarNode) throws RepositoryException {

		Map<String, Calendar> calendarPropertyMap = new HashMap<String, Calendar>();

		PropertyIterator propertyIterator = bdoCalendarNode.getProperties();

		while(propertyIterator.hasNext()) {
			Property property = propertyIterator.nextProperty();
			String propertyName = property.getName();
			Calendar propertyValue = Calendar.getInstance();
			//Set a default Calendar date - obsolete time
			propertyValue.set(1990, Calendar.JANUARY, 01);

			if(propertyName.startsWith(QuartelyBDOConstants.START_DATE) 
					|| propertyName.startsWith(QuartelyBDOConstants.END_DATE)
					&& property.getDate() != null) {
				propertyValue = property.getDate();
			}
			calendarPropertyMap.put(propertyName, propertyValue);
		}
		return calendarPropertyMap;
	}



	/**
	 * 
	 * @param year
	 * @param quarterNumber
	 * @param user
	 * @return
	 */
	public static String getQuarterlyBDORepositoryPath(int year, int quarterNumber, String user) {

		String repositoryBasePath = QuartelyBDOConstants.GDC_CHECKIN_REPOSITORY_BASE_PATH +"/"+
									user.charAt(0) + "/" + user + "/" + year + "/" + "Q" + quarterNumber;
		return repositoryBasePath;
	}

	public static String getEmployeeProfileBasePath(String user) {

		String repositoryBasePath = QuartelyBDOConstants.GDC_CHECKIN_REPOSITORY_BASE_PATH + "/" + user.charAt(0) + "/" + user;
		return repositoryBasePath;
	}



	public static String extractValueFromRawString(String rawString) {

		StringTokenizer tokens = new StringTokenizer(rawString, ",");
		while (tokens.hasMoreElements()) {
			String token = (String) tokens.nextElement();
			if(token.contains("CN") && !token.contains("USERS")) {
				return token.replace("CN=", QuartelyBDOConstants.EMPTY_STRING);
			}
		}
		return QuartelyBDOConstants.EMPTY_STRING;
	}

	public static Calendar convertToCalendar(String dateString) throws ParseException {
		
		Calendar cal = Calendar.getInstance();
		if(StringUtils.isNotBlank(dateString)) {
		    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		    cal.setTime(sdf.parse(dateString));
		}
		
		return cal;
	}
	
}
