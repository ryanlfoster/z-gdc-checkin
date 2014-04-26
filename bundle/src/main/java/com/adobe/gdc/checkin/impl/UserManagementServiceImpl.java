package com.adobe.gdc.checkin.impl;

import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.UnsupportedRepositoryOperationException;
import javax.jcr.Value;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;
import org.apache.jackrabbit.api.JackrabbitSession;
import org.apache.jackrabbit.api.security.user.Authorizable;
import org.apache.jackrabbit.api.security.user.UserManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.gdc.checkin.UserManagementService;
import com.adobe.gdc.checkin.constants.QuartelyBDOConstants;

@Component(metatype = true)
@Service(UserManagementService.class)
public class UserManagementServiceImpl implements UserManagementService{
	
	Logger log = LoggerFactory.getLogger(UserManagementServiceImpl.class);
	
	@Override
	public String getCurrentUser(Session session) {
		return session.getUserID();
	}

	@Override
	public String getEmployeeID(String userID, Session session) throws Exception {
		UserManager userManager = getUserManager(session);
		String employeeID = QuartelyBDOConstants.EMPTY_STRING;
		final Authorizable authorizable = userManager.getAuthorizable(userID);
		
		if(authorizable.hasProperty(QuartelyBDOConstants.PROFILE_EMPLOYEE_ID))
			employeeID =  authorizable.getProperty(QuartelyBDOConstants.PROFILE_EMPLOYEE_ID)[0].getString();
		
		return employeeID;
	}

	@Override
	public String getManagersEmailId(Session session) throws Exception {
		UserManager userManager = getUserManager(session);
		String managersEmailId = QuartelyBDOConstants.EMPTY_STRING;
		final Authorizable authorizable = userManager.getAuthorizable(session.getUserID());
		if(authorizable.hasProperty(QuartelyBDOConstants.PROFILE_MANAGER_ID))
			managersEmailId =  authorizable.getProperty(QuartelyBDOConstants.PROFILE_MANAGER_ID)[0].getString() 
														+ QuartelyBDOConstants.ADOBE_EMAIL_EXTENTION;
		
		return managersEmailId;
	}

	
	@Override
	public String[] getManagersDirectReportees(String managersID, Session session) throws Exception {
		UserManager userManager = getUserManager(session);
		String[] directReports = {};
		Value[] directReportsValue = {};
		final Authorizable authorizable = userManager.getAuthorizable(managersID);
		if(authorizable.hasProperty(QuartelyBDOConstants.PROFILE_DIRECT_REPORTS)) {
			directReportsValue =  authorizable.getProperty(QuartelyBDOConstants.PROFILE_DIRECT_REPORTS);
			directReports=new String[directReportsValue.length];
			for(int i=0; i<directReportsValue.length; i++) {
				directReports[i] = directReportsValue[i].getString();
			}
		}
		return directReports;
	}

	

	@Override
	public String getEmployeeDesignation(String userID,Session session) throws Exception {
		UserManager userManager = getUserManager(session);
		String designation = QuartelyBDOConstants.EMPTY_STRING;
		final Authorizable authorizable = userManager.getAuthorizable(userID);
		if(authorizable.hasProperty(QuartelyBDOConstants.PROFILE_DESIGNATION))
			designation =  authorizable.getProperty(QuartelyBDOConstants.PROFILE_DESIGNATION)[0].getString();
		
		return designation;
	}
	
	@Override
	public String getEmployeeName(String userID, Session session) throws Exception {
		UserManager userManager = getUserManager(session);
		String name = QuartelyBDOConstants.EMPTY_STRING;
		final Authorizable authorizable = userManager.getAuthorizable(userID);
		if(authorizable.hasProperty(QuartelyBDOConstants.PROFILE_FULL_NAME))
			name =  authorizable.getProperty(QuartelyBDOConstants.PROFILE_FULL_NAME)[0].getString();
		
		return name;
	}

	
	private UserManager getUserManager(Session session) throws UnsupportedRepositoryOperationException, RepositoryException {
		 UserManager userManager = ((JackrabbitSession) session).getUserManager();	
		 return userManager;
	}

	
}
