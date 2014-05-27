package com.adobe.gdc.checkin;

import java.util.Map;

public interface QuarterlyBDORepositoryClient {

	boolean createOrUpdateQuarterlyBDOData(String action, Map<String, String[]> params) throws Exception; 
	
	boolean createOrUpdateEmployeeProfileData(Map<String, String[]> params)  throws Exception;
	
    int getEmployeeProfileBeginYear(String userID) throws Exception;
	
	Map<String, String[]> getQuarterlyBDOData(int quarterNumber, int annualYear, String userID, boolean escapeNewline) throws Exception;
	
	public Map<String, String[]> getEmployeeProfileData(String userID) throws Exception;
}
