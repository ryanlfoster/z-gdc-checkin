package com.adobe.gdc.checkin;

import java.util.Map;

public interface QuarterlyBDORepositoryClient {

	boolean createOrUpdateQuarterlyBDOData(String action, Map<String, String[]> params); 

	boolean createOrUpdateEmployeeProfileData(Map<String, String[]> params);
	
	boolean createOrUpdateEmployeeProfileOnLogin(String userID);

	boolean createOrUpdateGDCBDOConfiguration(Map<String, String> params);

	Map<String, String[]> getQuarterlyBDOData(int quarterNumber, int annualYear, String userID, boolean escapeNewline);

	public Map<String, String[]> getEmployeeProfileData(String userID);
	
	public String[] getDirectReportees(String userID);
}