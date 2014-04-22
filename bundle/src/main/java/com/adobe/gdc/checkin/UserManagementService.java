package com.adobe.gdc.checkin;

import javax.jcr.Session;

public interface UserManagementService {

	String getCurrentUser(Session session);
	
	String getEmployeeID(Session session) throws Exception;
	
	String getManagersEmailId(Session session) throws Exception;
	
	String getEmployeeDesignation(Session session) throws Exception;
}
