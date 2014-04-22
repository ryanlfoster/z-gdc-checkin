package com.adobe.gdc.checkin.impl;

import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.UnsupportedRepositoryOperationException;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;
import org.apache.jackrabbit.api.JackrabbitSession;
import org.apache.jackrabbit.api.security.user.Authorizable;
import org.apache.jackrabbit.api.security.user.UserManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.gdc.checkin.UserManagementService;

@Component(metatype = true)
@Service(UserManagementService.class)
public class UserManagementServiceImpl implements UserManagementService{
	
	Logger log = LoggerFactory.getLogger(UserManagementServiceImpl.class);
	
	@Override
	public String getCurrentUser(Session session) {
		return session.getUserID();
	}

	
	@Override
	public String getEmployeeID(Session session) throws Exception {
		UserManager userManager = getUserManager(session);
		String employeeID = "";
		final Authorizable authorizable = userManager.getAuthorizable(session.getUserID());
		
		if(authorizable.hasProperty("profile/employeeID"))
			employeeID =  authorizable.getProperty("profile/employeeID")[0].getString();
		
		return employeeID;
	}

	@Override
	public String getManagersEmailId(Session session) throws Exception {
		UserManager userManager = getUserManager(session);
		String managersEmailId = "";
		final Authorizable authorizable = userManager.getAuthorizable(session.getUserID());
		if(authorizable.hasProperty("profile/managersID"))
			managersEmailId =  authorizable.getProperty("profile/managersID")[0].getString() + "@adobe.com";
		
		return managersEmailId;
	}


	@Override
	public String getEmployeeDesignation(Session session) throws Exception {
		UserManager userManager = getUserManager(session);
		String designation = "";
		final Authorizable authorizable = userManager.getAuthorizable(session.getUserID());
		if(authorizable.hasProperty("profile/designation"))
			designation =  authorizable.getProperty("profile/designation")[0].getString();
		
		return designation;
	}
	
	private UserManager getUserManager(Session session) throws UnsupportedRepositoryOperationException, RepositoryException {
		 UserManager userManager = ((JackrabbitSession) session).getUserManager();	
		 return userManager;
	}


	
	
}
