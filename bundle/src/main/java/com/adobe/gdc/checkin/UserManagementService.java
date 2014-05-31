package com.adobe.gdc.checkin;

import javax.jcr.Session;

public interface UserManagementService {

	String getCurrentUser(Session session);

	String getCurrentUserName(Session session);

	String  getEmployeeName(String userID);

	String getManagersEmailId(Session session);

	String getEmployeeDesignation(String userID);

	String[] getManagersDirectReportees(String managersID);

	boolean isManager(Session session);

	boolean isAdminUser(Session session);

	boolean isAnonymous(Session session);
}