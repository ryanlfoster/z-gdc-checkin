package com.adobe.gdc.checkin.impl;

import java.util.Iterator;

import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.UnsupportedRepositoryOperationException;
import javax.jcr.Value;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.apache.jackrabbit.api.JackrabbitSession;
import org.apache.jackrabbit.api.security.user.Authorizable;
import org.apache.jackrabbit.api.security.user.Group;
import org.apache.jackrabbit.api.security.user.UserManager;
import org.apache.sling.jcr.api.SlingRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.adobe.gdc.checkin.UserManagementService;
import com.adobe.gdc.checkin.constants.QuartelyBDOConstants;
import com.adobe.gdc.checkin.utils.QuarterlyBDOUtils;

@Component(metatype = true)
@Service(UserManagementService.class)
public class UserManagementServiceImpl implements UserManagementService {

	Logger log = LoggerFactory.getLogger(UserManagementServiceImpl.class);
	
	@Reference
	SlingRepository repository;

	@Override
	public String getCurrentUser(Session session) {
		return session.getUserID();
	}

	@Override
	public String getCurrentUserName(Session session) {
		String userID = getCurrentUser(session);
		return getEmployeeName(userID) ;
	}

	@Override
	public String getManagersEmailId(Session session) {
		
		Session adminSession = null;
		
		try {
			adminSession = getAdminSession();
			UserManager userManager = getUserManager(adminSession);
			String manager;
			final Authorizable authorizable = userManager.getAuthorizable(session.getUserID());
			if(authorizable != null && authorizable.hasProperty(QuartelyBDOConstants.PROFILE_MANAGER)) {
				manager =  authorizable.getProperty(QuartelyBDOConstants.PROFILE_MANAGER)[0].getString();
				return QuarterlyBDOUtils.extractValueFromRawString(manager) + QuartelyBDOConstants.ADOBE_EMAIL_EXTENTION;
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
		return QuartelyBDOConstants.EMPTY_STRING;
	}

	@Override
	public String[] getManagersDirectReportees(String managersID) {
		
		String[] directReports = {};
		Session adminSession = null;
		
		try {
			adminSession = getAdminSession();
		
			UserManager userManager = getUserManager(adminSession);
			
			Value[] directReportsValue = {};
			final Authorizable authorizable = userManager.getAuthorizable(managersID);
			if(authorizable != null && authorizable.hasProperty(QuartelyBDOConstants.PROFILE_DIRECT_REPORTS)) {
				directReportsValue =  authorizable.getProperty(QuartelyBDOConstants.PROFILE_DIRECT_REPORTS);
				directReports=new String[directReportsValue.length];
				for(int i=0; i<directReportsValue.length; i++) {
					directReports[i] =  QuarterlyBDOUtils.extractValueFromRawString(directReportsValue[i].getString());
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
		return directReports;
	}

	@Override
	public boolean isManager(Session session) {
		
		Session adminSession = null;
		
		try {
			adminSession = getAdminSession();
			
			String currentLdapID = getCurrentUser(session);
			UserManager userManager = getUserManager(adminSession);
			Value[] memberOfValues = {};
			final Authorizable authorizable = userManager.getAuthorizable(currentLdapID);
			if(authorizable != null && authorizable.hasProperty(QuartelyBDOConstants.PROFILE_MEMBER_OF)) {
				memberOfValues = authorizable.getProperty(QuartelyBDOConstants.PROFILE_MEMBER_OF);
				for(int i=0; i<memberOfValues.length; i++) {
					String mailingGroup = QuarterlyBDOUtils.extractValueFromRawString(memberOfValues[i].getString());
					if(mailingGroup.equalsIgnoreCase("ORG-"+currentLdapID+"-ALL")) {
						return true;
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
		return false;
	}

	@Override
	public String getEmployeeDesignation(String userID) {
		
		String designation = QuartelyBDOConstants.EMPTY_STRING;
		Session adminSession = null;
		
		try {
			adminSession = getAdminSession();
			UserManager userManager = getUserManager(adminSession);
		
			final Authorizable authorizable = userManager.getAuthorizable(userID);
			if(authorizable != null && authorizable.hasProperty(QuartelyBDOConstants.PROFILE_DESIGNATION))
				designation =  authorizable.getProperty(QuartelyBDOConstants.PROFILE_DESIGNATION)[0].getString();
		}
		catch(Exception e) {
			log.error("[Exception]", e);
		}
		finally {
			if(adminSession != null && adminSession.isLive()) {
				adminSession.logout();
			}
		}
		return designation;
	}

	@Override
	public String getEmployeeName(String userID) {
		
		String name = QuartelyBDOConstants.EMPTY_STRING;
		Session adminSession = null;
		
		try {
			adminSession = getAdminSession();
		
			UserManager userManager = getUserManager(adminSession);
			
			final Authorizable authorizable = userManager.getAuthorizable(userID);		
			if(authorizable != null && authorizable.hasProperty(QuartelyBDOConstants.PROFILE_GIVENNAME))
				name =  authorizable.getProperty(QuartelyBDOConstants.PROFILE_GIVENNAME)[0].getString();
			if(authorizable != null && authorizable.hasProperty(QuartelyBDOConstants.PROFILE_FAMILYNAME))
				name = name + " " + authorizable.getProperty(QuartelyBDOConstants.PROFILE_FAMILYNAME)[0].getString();
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
	public  boolean isAnonymous(Session session) {

		if(session == null || session.getUserID().equalsIgnoreCase(QuartelyBDOConstants.ANONYMOUS) || !session.isLive() ) {
			return true;
		}

		return false;
    }

	@Override
	public boolean isAdminUser(Session session) {
		
		Session adminSession = null;
		
		try {
			adminSession = getAdminSession();
		
			UserManager userManager = getUserManager(adminSession);
			String userID = getCurrentUser(session);
	
			final Authorizable authorizable = userManager.getAuthorizable(userID);
			
			Iterator<Group> userGroups = authorizable.memberOf();
			
			while(userGroups != null && userGroups.hasNext()) {
				if(userGroups.next().getID().equals(QuartelyBDOConstants.ADMIN_USER_GROUP)) {
					return true;
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
		return false;		
	}

	private UserManager getUserManager(Session session) throws UnsupportedRepositoryOperationException, RepositoryException {
		 UserManager userManager = ((JackrabbitSession) session).getUserManager();	
		 return userManager;
	}

	private Session getAdminSession() throws RepositoryException {
		return repository.loginAdministrative(repository.getDefaultWorkspace());
	}

}