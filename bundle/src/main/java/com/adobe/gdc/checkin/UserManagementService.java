package com.adobe.gdc.checkin;

import javax.jcr.Session;

public interface UserManagementService {

	String getCurrentUser(Session session);
	
	String  getEmployeeName(String userID, Session session) throws Exception;
	
	String getManagersEmailId(Session session) throws Exception;
	
	String getEmployeeDesignation(String userID, Session session) throws Exception;
	
	String[] getManagersDirectReportees(String managersID, Session session) throws Exception;
	
	boolean isManager(Session session);
}
