package com.adobe.gdc.checkin;

import javax.jcr.Session;

public interface UserManagementService {

	String getCurrentUser(Session session);
	
	String getEmployeeID(String userID, Session session) throws Exception;
	
	String  getEmployeeName(String userID, Session session) throws Exception;
	
	String getManagersEmailId(Session session) throws Exception;
	
	String getEmployeeDesignation(String userID, Session session) throws Exception;
}
