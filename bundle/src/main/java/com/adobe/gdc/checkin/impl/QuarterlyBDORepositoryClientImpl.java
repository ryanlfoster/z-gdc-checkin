package com.adobe.gdc.checkin.impl;

import java.util.HashMap;
import java.util.Map;
import javax.jcr.Node;
import javax.jcr.Session;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.apache.jackrabbit.commons.JcrUtils;
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
	
	@Override
	public boolean createOrUpdateQuarterlyBDOData(String action, Map<String, String[]> params,  Session session) throws Exception {
	
		int quarterNumber = Integer.parseInt(params.get(QuartelyBDOConstants.QUARTER_NUMBER)[0]);
		int annualYear = Integer.parseInt(params.get(QuartelyBDOConstants.ANNUAL_YEAR)[0]);
		String userID = params.get(QuartelyBDOConstants.USER_ID)[0];
		//First get the repository base-path
		String repositoryPath = QuarterlyBDOUtils.getQuarterlyBDORepositoryPath(annualYear, quarterNumber, userID);
		
		//Get or create the Quarterly BDO node to store data
		Node quarterlyBDONode = JcrUtils.getOrCreateByPath(repositoryPath, JcrConstants.NT_UNSTRUCTURED, session);	
		
		//Now save the BDO data as the node properties
		QuarterlyBDOUtils.setNodeProperties(quarterlyBDONode, getQuarterlyBDOProperties(action, params, session));
		
		if(session.hasPendingChanges()) {
			session.save();
			return true;
		}
		return false;
	}

	@Override
	public Map<String, String[]> getQuarterlyBDOData(int quarterNumber, int annualYear, String userID, Session session) throws Exception {

		Map<String, String[]> bdoDataMap = new HashMap<String, String[]>();
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
		
		properties.put(QuartelyBDOConstants.OBJECTIVES, params.get(QuartelyBDOConstants.OBJECTIVES_ARRAY));
		properties.put(QuartelyBDOConstants.ACHIEVEMENTS, params.get(QuartelyBDOConstants.ACHIEVEMENTS_ARRAY));
		properties.put(QuartelyBDOConstants.DESIGNATION, params.get(QuartelyBDOConstants.DESIGNATION));
		properties.put(QuartelyBDOConstants.PERCENTAGE_ACHIEVED, params.get(QuartelyBDOConstants.PERCENTAGE_ACHIEVED));
		properties.put(QuartelyBDOConstants.NAME, params.get(QuartelyBDOConstants.NAME));
		properties.put(QuartelyBDOConstants.EMPLOYEE_ID, new String[] {userManagementService.getEmployeeID(params.get(QuartelyBDOConstants.USER_ID)[0],session)});
		properties.put(QuartelyBDOConstants.STATUS, action.equals(QuartelyBDOConstants.SUBMIT) 
													? new String[] {QuartelyBDOConstants.SUBMITTED} 
													: new String[] {QuartelyBDOConstants.NOT_SUBMITTED});
		
		return properties; 
	}

}


