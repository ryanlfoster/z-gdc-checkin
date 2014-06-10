package com.adobe.gdc.checkin.impl;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import org.apache.commons.lang.StringUtils;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.apache.jackrabbit.commons.JcrUtils;
import org.apache.sling.jcr.api.SlingRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.adobe.gdc.checkin.QuarterlyBDOCalendarService;
import com.adobe.gdc.checkin.QuarterlyBDORepositoryClient;
import com.adobe.gdc.checkin.UserManagementService;
import com.adobe.gdc.checkin.constants.QuartelyBDOConstants;
import com.adobe.gdc.checkin.utils.QuarterlyBDOUtils;
import com.day.cq.commons.jcr.JcrConstants;

@Component(metatype = true)
@Service(QuarterlyBDORepositoryClient.class)

public class QuarterlyBDORepositoryClientImpl  implements QuarterlyBDORepositoryClient{

	Logger log = LoggerFactory.getLogger(QuarterlyBDORepositoryClientImpl.class);

	@Reference
	QuarterlyBDOCalendarService quarterlyBDOCalendarService;

	@Reference
	UserManagementService userManagementService;

	@Reference
	SlingRepository repository;

	@Override
	public boolean createOrUpdateQuarterlyBDOData(String action, Map<String, String[]> params) {

		int quarterNumber = Integer.parseInt(params.get(QuartelyBDOConstants.QUARTER_NUMBER)[0]);
		int annualYear = Integer.parseInt(params.get(QuartelyBDOConstants.ANNUAL_YEAR)[0]);
		String userID = params.get(QuartelyBDOConstants.USER_ID)[0];
		Session adminSession = null;
		try {
		//First get the repository base-path
		String repositoryPath = QuarterlyBDOUtils.getQuarterlyBDORepositoryPath(annualYear, quarterNumber, userID);

	    adminSession = getAdminSession();

		//Get or create the Quarterly BDO node to store data
		Node quarterlyBDONode = JcrUtils.getOrCreateByPath(repositoryPath, JcrConstants.NT_UNSTRUCTURED, adminSession);	

		//Now save the BDO data as the node properties
		QuarterlyBDOUtils.setNodeProperties(quarterlyBDONode, getQuarterlyBDOProperties(action, params));

		if(adminSession.hasPendingChanges()) {
			adminSession.save();
			return true;
		}
		} 
		catch(Exception e) {
			log.error("Exception", e);
		}
		finally {
			if(adminSession != null && adminSession.isLive()) {
				adminSession.logout();
			}
		}

		return false;
	}

	@Override
	public boolean createOrUpdateEmployeeProfileData(Map<String, String[]> params) {

		String userID = params.get(QuartelyBDOConstants.USER_ID)[0];

		String repositoryPath = QuarterlyBDOUtils.getEmployeeProfileBasePath(userID); 
		Session adminSession = null;
		boolean result = false;
		
		try {
			adminSession = getAdminSession();

			//Get or create the employee Profile node to store/update Employee profile data
			Node employeeProfileNode = JcrUtils.getOrCreateByPath(repositoryPath, JcrConstants.NT_UNSTRUCTURED, adminSession);

			//Now save the employee profile data as the node properties
			QuarterlyBDOUtils.setNodeProperties(employeeProfileNode, getEmployeeProfileProperties(params));

			if(adminSession.hasPendingChanges()) {
				adminSession.save();
				result = true;
			}
		}
		catch(Exception e) {
			log.error("[Exception]", e);
			
		}
		finally {
			if(adminSession != null && adminSession.isLive()) {
				adminSession.logout();
			}
		}

		return result;

	}

	@Override
	public boolean createOrUpdateEmployeeProfileOnLogin(String userID) {
		
		Map<String, String[]> params = new HashMap<String, String[]>();
		params.put(QuartelyBDOConstants.USER_ID, new String[] { userID });
		params.put(QuartelyBDOConstants.DESIGNATION, new String[] { userManagementService.getEmployeeDesignation(userID) });
		params.put(QuartelyBDOConstants.NAME, new String[] { userManagementService.getEmployeeName(userID) });
		params.put(QuartelyBDOConstants.MANAGER, new String[] { userManagementService.getManagersLdap(userID) });
		
		if(userManagementService.isManager(userID)) {
			params.put(QuartelyBDOConstants.DIRECT_REPORTS, userManagementService.getManagersDirectReportees(userID) );
		}
		
		return createOrUpdateEmployeeProfileData(params);
	}

	@Override
	public Map<String, String[]> getQuarterlyBDOData(int quarterNumber, int annualYear, String userID, boolean escapeNewline) {

		Map<String, String[]> bdoDataMap = new HashMap<String, String[]>();

		String repositoryPath = QuarterlyBDOUtils.getQuarterlyBDORepositoryPath(annualYear, quarterNumber, userID);
		Session adminSession = null;

		try {
		    adminSession = getAdminSession();

			//Get the quarterly BDO node from the repository
			Node quarterlyBDONode = JcrUtils.getNodeIfExists(repositoryPath, adminSession);

			//If node exists, read the node properties 
			if(quarterlyBDONode != null) {
				bdoDataMap = QuarterlyBDOUtils.readNodeproperties(quarterlyBDONode,escapeNewline);
			} 
		}
		catch(Exception e) {
			log.error("[Exception]", e);
		}
		finally {
			if(adminSession != null && adminSession.isLive()) {
				adminSession.logout();
			}
		}

		return bdoDataMap;
	}

	@Override
	public Map<String, String[]> getEmployeeProfileData(String userID) {

		Map<String, String[]> employeeDataMap = new HashMap<String, String[]>();
		String repositoryPath = QuarterlyBDOUtils.getEmployeeProfileBasePath(userID);

		Session adminSession = null;

		try {
			adminSession = getAdminSession();

			if(StringUtils.isNotBlank(repositoryPath)) {
				//Get the Employee Profile node from the repository
				Node employeeProfileNode = JcrUtils.getNodeIfExists(repositoryPath, adminSession);
	
				//If node exists, read the node properties 
				if(employeeProfileNode != null) {
					employeeDataMap = QuarterlyBDOUtils.readNodeproperties(employeeProfileNode, false);
				}
			}
		}			
		catch(Exception e) {
			log.error("[Exception]", e);
		}
		finally {
			if(adminSession != null && adminSession.isLive()) {
				adminSession.logout();
			}
		}

		return employeeDataMap;
	}

	
	@Override
	public String[] getDirectReportees(String userID) {

		String [] directReportee = {};
		String repositoryPath = QuarterlyBDOUtils.getEmployeeProfileBasePath(userID);

		Session adminSession = null;

		try {
			adminSession = getAdminSession();

			//Get the Employee Profile node from the repository
			Node employeeProfileNode = JcrUtils.getNodeIfExists(repositoryPath, adminSession);

			//If node exists, read the node properties 
			if(employeeProfileNode != null) {
				Map<String, String[]> employeeDataMap = QuarterlyBDOUtils.readNodeproperties(employeeProfileNode, false);
				
				if(employeeDataMap.containsKey(QuartelyBDOConstants.DIRECT_REPORTS)) {
					directReportee = employeeDataMap.get(QuartelyBDOConstants.DIRECT_REPORTS);
				}
			}
		}
		catch(Exception e) {
			log.error("[Exception]", e);
		}
		finally {
			if(adminSession != null && adminSession.isLive()) {
				adminSession.logout();
			}
		}

		return directReportee;
	}
	
	
	@Override
	public String getEmployeeName(String userID) {

		String name = QuartelyBDOConstants.EMPTY_STRING;
		String repositoryPath = QuarterlyBDOUtils.getEmployeeProfileBasePath(userID);

		Session adminSession = null;

		try {
			
			if(StringUtils.isNotBlank(repositoryPath)) {
				adminSession = getAdminSession();
	
				//Get the Employee Profile node from the repository
				Node employeeProfileNode = JcrUtils.getNodeIfExists(repositoryPath, adminSession);
	
				//If node exists, read the node properties 
				if(employeeProfileNode != null) {
					Map<String, String[]> employeeDataMap = QuarterlyBDOUtils.readNodeproperties(employeeProfileNode, false);
					
					if(employeeDataMap.containsKey(QuartelyBDOConstants.NAME)) {
						name = employeeDataMap.get(QuartelyBDOConstants.NAME)[0];
					}
				}
			}
		}
		catch(Exception e) {
			log.error("[Exception]", e);
		}
		finally {
			if(adminSession != null && adminSession.isLive()) {
				adminSession.logout();
			}
		}

		return name;
	}
	

	
	@Override
	public boolean createOrUpdateGDCBDOConfiguration(Map<String, String> params) {

		Session adminSession = null;
		try {
		//First get the repository calendar base-path
		String repositoryPath = QuartelyBDOConstants.GDC_CHECKIN_REPOSITORY_CALENDAR_BASE_PATH;

	    adminSession = getAdminSession();

		//Get or create the calendar configuration node to store data
		Node calendarBDONode = JcrUtils.getOrCreateByPath(repositoryPath, JcrConstants.NT_UNSTRUCTURED, adminSession);	

		//Now save the BDO data as the node properties
		QuarterlyBDOUtils.setNodeStringProperties(calendarBDONode, getBDOCalendarStringProperties(params));
		QuarterlyBDOUtils.setNodeDateProperties(calendarBDONode, getBDOCalendarDateProperties(params));
 
		if(adminSession.hasPendingChanges()) {
			adminSession.save();
			return true;
		}
		} 
		catch(Exception e) {
			log.error("Exception", e);
		}
		finally {
			if(adminSession != null && adminSession.isLive()) {
				adminSession.logout();
			}
		}

		return false;
	}		

	private Map<String, String> getBDOCalendarStringProperties(Map<String, String> params) {
		Map<String,String> properties = new HashMap<String,String>();
		properties.put(QuartelyBDOConstants.FISCAL_YEAR, params.get(QuartelyBDOConstants.FISCAL_YEAR));
		properties.put(QuartelyBDOConstants.BUFFER_DAYS, params.get(QuartelyBDOConstants.BUFFER_DAYS));
		return properties;
	}
	
	private Map<String, Calendar> getBDOCalendarDateProperties(Map<String, String> params) throws ParseException {
		Map<String,Calendar> properties = new HashMap<String,Calendar>();
		
		properties.put(QuartelyBDOConstants.Q1_START_DATE, QuarterlyBDOUtils.convertToCalendar(params.get(QuartelyBDOConstants.Q1_START_DATE)));
		properties.put(QuartelyBDOConstants.Q1_END_DATE, QuarterlyBDOUtils.convertToCalendar(params.get(QuartelyBDOConstants.Q1_END_DATE)));

		properties.put(QuartelyBDOConstants.Q2_START_DATE, QuarterlyBDOUtils.convertToCalendar(params.get(QuartelyBDOConstants.Q2_START_DATE)));
		properties.put(QuartelyBDOConstants.Q2_END_DATE, QuarterlyBDOUtils.convertToCalendar(params.get(QuartelyBDOConstants.Q2_END_DATE)));

		properties.put(QuartelyBDOConstants.Q3_START_DATE, QuarterlyBDOUtils.convertToCalendar(params.get(QuartelyBDOConstants.Q3_START_DATE)));
		properties.put(QuartelyBDOConstants.Q3_END_DATE, QuarterlyBDOUtils.convertToCalendar(params.get(QuartelyBDOConstants.Q3_END_DATE)));

		properties.put(QuartelyBDOConstants.Q4_START_DATE, QuarterlyBDOUtils.convertToCalendar(params.get(QuartelyBDOConstants.Q4_START_DATE)));
		properties.put(QuartelyBDOConstants.Q4_END_DATE, QuarterlyBDOUtils.convertToCalendar(params.get(QuartelyBDOConstants.Q4_END_DATE)));
		
		
		return properties;
	}
	

	private Map<String, String[]> getQuarterlyBDOProperties(String action, Map<String, String[]> params)
	{
		Map<String,String[]> properties = new HashMap<String,String[]>();

		String [] objectives = params.get(QuartelyBDOConstants.OBJECTIVES_ARRAY);
		String [] achievements = params.get(QuartelyBDOConstants.ACHIEVEMENTS_ARRAY);

		List<String> objectiveList =  new ArrayList<String>(Arrays.asList(objectives));
		List<String> achievementList =  new ArrayList<String>(Arrays.asList(achievements));

		Iterator<String> objectiveListIterator = objectiveList.iterator();
		Iterator<String> achievementListIterator = achievementList.iterator();

		//This is to trim out empty values from the Array
		while(objectiveListIterator.hasNext() && achievementListIterator.hasNext()) {

			String objective = objectiveListIterator.next();
			String achievement = achievementListIterator.next();

			if(StringUtils.isBlank(objective) && StringUtils.isBlank(achievement)) {
				objectiveListIterator.remove();
				achievementListIterator.remove();
			}
		}

		objectives = objectiveList.toArray(new String[0]);
		achievements = achievementList.toArray(new String[0]);

		properties.put(QuartelyBDOConstants.OBJECTIVES, objectives);
		properties.put(QuartelyBDOConstants.ACHIEVEMENTS, achievements);
		properties.put(QuartelyBDOConstants.DESIGNATION, params.get(QuartelyBDOConstants.DESIGNATION));

		//Update the BDO SCORE -for manager only
		if(action.equals(QuartelyBDOConstants.COMPLETE)) {
			properties.put(QuartelyBDOConstants.BDO_SCORE, params.get(QuartelyBDOConstants.BDO_SCORE));
		}

		String [] status = {};
		if(action.equals(QuartelyBDOConstants.SUBMIT)) {
			status = new String[] {QuartelyBDOConstants.SUBMITTED};
		}
		else if(action.equals(QuartelyBDOConstants.COMPLETE)) {
			status = new String[] {QuartelyBDOConstants.COMPLETED};
		}
		else {
			status = new String[] {QuartelyBDOConstants.NOT_SUBMITTED};
		}

		properties.put(QuartelyBDOConstants.STATUS, status);

		return properties; 
	}


	private Map<String, String[]> getEmployeeProfileProperties(Map<String, String[]> params) {
		Map<String,String[]> properties = new HashMap<String,String[]>();

		properties.put(QuartelyBDOConstants.DESIGNATION, params.get(QuartelyBDOConstants.DESIGNATION));
		
		//For the first login, employeeID is not present
		if(params.get(QuartelyBDOConstants.EMPLOYEE_ID) != null && params.get(QuartelyBDOConstants.EMPLOYEE_ID).length > 0) {
			properties.put(QuartelyBDOConstants.EMPLOYEE_ID, params.get(QuartelyBDOConstants.EMPLOYEE_ID));
		}
		properties.put(QuartelyBDOConstants.NAME, params.get(QuartelyBDOConstants.NAME));
		
		if(params.get(QuartelyBDOConstants.MANAGER) != null && params.get(QuartelyBDOConstants.MANAGER).length > 0) {
			properties.put(QuartelyBDOConstants.MANAGER, params.get(QuartelyBDOConstants.MANAGER));
		}
		
		if(params.get(QuartelyBDOConstants.DIRECT_REPORTS) != null && params.get(QuartelyBDOConstants.DIRECT_REPORTS).length > 0) {
			properties.put(QuartelyBDOConstants.DIRECT_REPORTS, params.get(QuartelyBDOConstants.DIRECT_REPORTS));
		}

		return properties; 
	}

	private Session getAdminSession() throws RepositoryException {
		return repository.loginAdministrative(repository.getDefaultWorkspace());
	}

}

