package com.adobe.gdc.checkin.impl;

import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.UnsupportedRepositoryOperationException;
import javax.jcr.Value;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

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
	public String getCurrentUserName(Session session) throws Exception {
		String userID = getCurrentUser(session);
		return getEmployeeName(userID, session) ;
	}
	
	
	@Override
	public String getManagersEmailId(Session session) throws Exception {
		UserManager userManager = getUserManager(session);
		String managersEmailId = QuartelyBDOConstants.EMPTY_STRING;
		final Authorizable authorizable = userManager.getAuthorizable(session.getUserID());
		if(authorizable.hasProperty(QuartelyBDOConstants.PROFILE_MANAGER))
			managersEmailId =  authorizable.getProperty(QuartelyBDOConstants.PROFILE_MANAGER)[0].getString();
            //	+ QuartelyBDOConstants.ADOBE_EMAIL_EXTENTION;
		
		
		
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
		if(authorizable.hasProperty(QuartelyBDOConstants.PROFILE_GIVENNAME))
			name =  authorizable.getProperty(QuartelyBDOConstants.PROFILE_GIVENNAME)[0].getString();
		if(authorizable.hasProperty(QuartelyBDOConstants.PROFILE_FAMILYNAME))
			name = name + " " + authorizable.getProperty(QuartelyBDOConstants.PROFILE_FAMILYNAME)[0].getString();
		
		return name;
	}

	@Override
	public boolean isManager(Session session) {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public  boolean isAnonymous( HttpServletRequest request) {
		String cookieName = QuartelyBDOConstants.GDC_USER_COOKIE;
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            String name = cookie.getName();
            if (cookieName.equals(name)) {
                return false;
            }
        }
        return true;
    }
	
	private UserManager getUserManager(Session session) throws UnsupportedRepositoryOperationException, RepositoryException {
		 UserManager userManager = ((JackrabbitSession) session).getUserManager();	
		 return userManager;
	}

	
	
	
}
