package com.adobe.gdc.checkin.impl;

import java.util.HashMap;
import java.util.Map;
import javax.jcr.Node;
import javax.jcr.Session;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.apache.jackrabbit.commons.JcrUtils;
import com.adobe.gdc.checkin.QuarterlyBDOCalendarService;
import com.adobe.gdc.checkin.QuarterlyBDORepositoryClient;
import com.adobe.gdc.checkin.UserManagementService;
import com.adobe.gdc.checkin.utils.QuarterlyBDOUtils;
import com.day.cq.commons.jcr.JcrConstants;

@Component(metatype = true)
@Service(QuarterlyBDORepositoryClient.class)

public class QuarterlyBDORepositoryClientImpl  implements QuarterlyBDORepositoryClient{

	@Reference
	UserManagementService userManagementService;
	
	@Reference
	QuarterlyBDOCalendarService quarterlyBDOCalendarService;
	
	@Override
	public boolean createOrUpdateQuarterlyBDOData(String action, Map<String, String[]> params, Session session) throws Exception {
	
		String userID = userManagementService.getCurrentUser(session);
		int quarterNumber = Integer.parseInt(params.get("quarterNumber")[0]);
		int currentYear = quarterlyBDOCalendarService.getcurrentQuarterAnnualYear();
		
		//First get the repository base-path
		String repositoryPath = QuarterlyBDOUtils.getQuarterlyBDORepositoryPath(currentYear, quarterNumber, userID);
		
		//Get or create the Quarterly BDO node to store data
		Node quarterlyBDONode = JcrUtils.getOrCreateByPath(repositoryPath, JcrConstants.NT_UNSTRUCTURED, session);	
		
		//Now save the BDO data as the node properties
		QuarterlyBDOUtils.setNodeProperties(quarterlyBDONode, getQuarterlyBDOProperties(action, params, session));
		 
		//Set a default status for the stored BDO form data until it is submitted 
		if(!quarterlyBDONode.hasProperty("status"))
			quarterlyBDONode.setProperty("status", "NOT SUBMITTED");
		
		if(session.hasPendingChanges()) {
			session.save();
			return true;
		}
		return false;
	}

	@Override
	public Map<String, String[]> getQuarterlyBDOData(int quarterNumber, int annualYear, Session session) throws Exception {

		Map<String, String[]> bdoDataMap = new HashMap<String, String[]>();
		String userID = userManagementService.getCurrentUser(session);
		String repositoryPath = QuarterlyBDOUtils.getQuarterlyBDORepositoryPath(annualYear, quarterNumber, userID);
		//Get the quarterly BDO node from the repository
		Node quarterlyBDONode = JcrUtils.getNodeIfExists(repositoryPath, session);
		
		//If node exists, read the node properties 
		if(quarterlyBDONode != null) {
			bdoDataMap = QuarterlyBDOUtils.readNodeproperties(quarterlyBDONode);
		} 
		return bdoDataMap;
	}
	
	
	private Map<String, String[]> getQuarterlyBDOProperties(String action, Map<String, String[]> params, Session session) throws Exception
	{
		Map<String,String[]> properties = new HashMap<String,String[]>();
		
		properties.put("objectives", params.get("objectives[]"));
		properties.put("achievements", params.get("achievements[]"));
		properties.put("designation", params.get("designation"));
		properties.put("percentageAchieved", params.get("percentageAchieved"));
		properties.put("employeeID", new String[] {userManagementService.getEmployeeID(session)});
		if(action.equals("submit")) {
			properties.put("status", new String[] {"SUBMITTED"});
		}
		return properties; 
	}

}


