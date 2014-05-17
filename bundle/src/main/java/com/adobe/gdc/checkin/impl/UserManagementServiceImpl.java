package com.adobe.gdc.checkin.impl;

import java.util.StringTokenizer;

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
import com.adobe.gdc.checkin.utils.QuarterlyBDOUtils;

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
		String managersEmailId, manager;
		managersEmailId =  manager= QuartelyBDOConstants.EMPTY_STRING;
		final Authorizable authorizable = userManager.getAuthorizable(session.getUserID());
		if(authorizable.hasProperty(QuartelyBDOConstants.PROFILE_MANAGER)) {
			manager =  authorizable.getProperty(QuartelyBDOConstants.PROFILE_MANAGER)[0].getString();
			return QuarterlyBDOUtils.extractValueFromRawString(manager) + QuartelyBDOConstants.ADOBE_EMAIL_EXTENTION;
		}
		
		return QuartelyBDOConstants.EMPTY_STRING;
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
				directReports[i] =  QuarterlyBDOUtils.extractValueFromRawString(directReportsValue[i].getString());
			}
		}
		return directReports;
	}

	@Override
	public boolean isManager(Session session) throws Exception {
		String currentLdapID = getCurrentUser(session);
		UserManager userManager = getUserManager(session);
		Value[] memberOfValues = {};
		final Authorizable authorizable = userManager.getAuthorizable(currentLdapID);
		if(authorizable.hasProperty(QuartelyBDOConstants.PROFILE_MEMBER_OF)) {
			memberOfValues = authorizable.getProperty(QuartelyBDOConstants.PROFILE_MEMBER_OF);
			for(int i=0; i<memberOfValues.length; i++) {
				String mailingGroup = QuarterlyBDOUtils.extractValueFromRawString(memberOfValues[i].getString());
				if(mailingGroup.equalsIgnoreCase("ORG-"+currentLdapID+"-ALL")) {
					return true;
				}
				
			}
		}
		
		return false;
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
