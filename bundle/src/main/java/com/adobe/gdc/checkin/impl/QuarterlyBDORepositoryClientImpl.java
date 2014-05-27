package com.adobe.gdc.checkin.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.jcr.Node;
import javax.jcr.NodeIterator;
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
	public boolean createOrUpdateQuarterlyBDOData(String action, Map<String, String[]> params) throws Exception {
	
		int quarterNumber = Integer.parseInt(params.get(QuartelyBDOConstants.QUARTER_NUMBER)[0]);
		int annualYear = Integer.parseInt(params.get(QuartelyBDOConstants.ANNUAL_YEAR)[0]);
		String userID = params.get(QuartelyBDOConstants.USER_ID)[0];
		//First get the repository base-path
		String repositoryPath = QuarterlyBDOUtils.getQuarterlyBDORepositoryPath(annualYear, quarterNumber, userID);
		
		Session adminSession = getAdminSession();
		
		//Get or create the Quarterly BDO node to store data
		Node quarterlyBDONode = JcrUtils.getOrCreateByPath(repositoryPath, JcrConstants.NT_UNSTRUCTURED, adminSession);	
		
		//Now save the BDO data as the node properties
		QuarterlyBDOUtils.setNodeProperties(quarterlyBDONode, getQuarterlyBDOProperties(action, params));
		
		if(adminSession.hasPendingChanges()) {
			adminSession.save();
			return true;
		}
		
		return false;
	}

	@Override
	public boolean createOrUpdateEmployeeProfileData(Map<String, String[]> params)  throws Exception {
		
		String userID = params.get(QuartelyBDOConstants.USER_ID)[0];
		
		String repositoryPath = QuarterlyBDOUtils.getEmployeeProfileBasePath(userID); 
		
		Session adminSession = getAdminSession();
		
		//Get or create the employee Profile node to store/update Employee profile data
		Node employeeProfileNode = JcrUtils.getOrCreateByPath(repositoryPath, JcrConstants.NT_UNSTRUCTURED, adminSession);
		
		//Now save the employee profile data as the node properties
		QuarterlyBDOUtils.setNodeProperties(employeeProfileNode, getEmployeeProfileProperties(params));
				
		if(adminSession.hasPendingChanges()) {
			adminSession.save();
			return true;
		}
		
		return false;

	}
	
	
	@Override
	public Map<String, String[]> getQuarterlyBDOData(int quarterNumber, int annualYear, String userID, boolean escapeNewline) throws Exception {

		Map<String, String[]> bdoDataMap = new HashMap<String, String[]>();
		
		String repositoryPath = QuarterlyBDOUtils.getQuarterlyBDORepositoryPath(annualYear, quarterNumber, userID);
		
		Session adminSession = getAdminSession();
		
		//Get the quarterly BDO node from the repository
		Node quarterlyBDONode = JcrUtils.getNodeIfExists(repositoryPath, adminSession);
		
		//If node exists, read the node properties 
		if(quarterlyBDONode != null) {
			bdoDataMap = QuarterlyBDOUtils.readNodeproperties(quarterlyBDONode,escapeNewline);
		} 
		
		return bdoDataMap;
	}
	
	@Override
	public Map<String, String[]> getEmployeeProfileData(String userID) throws Exception {

		Map<String, String[]> employeeDataMap = new HashMap<String, String[]>();
		String repositoryPath = QuarterlyBDOUtils.getEmployeeProfileBasePath(userID);
		
		Session adminSession = getAdminSession();
		
		//Get the Employee Profile node from the repository
		Node employeeProfileNode = JcrUtils.getNodeIfExists(repositoryPath, adminSession);
		
		//If node exists, read the node properties 
		if(employeeProfileNode != null) {
			employeeDataMap = QuarterlyBDOUtils.readNodeproperties(employeeProfileNode, false);
		}
		
		return employeeDataMap;
	}
	
	
	@Override
	public int getEmployeeProfileBeginYear(String userID) throws Exception {
		
		String repositoryPath = QuarterlyBDOUtils.getEmployeeProfileBasePath(userID); 
		
		Session adminSession = getAdminSession();
		
		//Get Employee Node
		Node employeeNode = JcrUtils.getNodeIfExists(repositoryPath, adminSession);
		
		NodeIterator years = employeeNode.getNodes();
		
		int minYear = Calendar.getInstance().get(Calendar.YEAR);
		
		while(years.hasNext()) {
			
			Node yearNode = (Node) years.next();
			
			int year = Integer.parseInt(yearNode.getName());
			
			minYear = Math.min(minYear,year);
		}
		
		return minYear;
	}
	
	
	private Map<String, String[]> getQuarterlyBDOProperties(String action, Map<String, String[]> params) throws Exception
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

	
	private Map<String, String[]> getEmployeeProfileProperties(Map<String, String[]> params) throws Exception
	{
		Map<String,String[]> properties = new HashMap<String,String[]>();
	
		properties.put(QuartelyBDOConstants.DESIGNATION, params.get(QuartelyBDOConstants.DESIGNATION));
		properties.put(QuartelyBDOConstants.EMPLOYEE_ID, params.get(QuartelyBDOConstants.EMPLOYEE_ID));
		properties.put(QuartelyBDOConstants.NAME, params.get(QuartelyBDOConstants.NAME));
		
		return properties; 
	}

	private Session getAdminSession() throws RepositoryException {
		return repository.loginAdministrative(repository.getDefaultWorkspace());
	}
}


