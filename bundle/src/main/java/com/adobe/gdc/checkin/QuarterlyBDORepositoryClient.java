package com.adobe.gdc.checkin;

import java.util.Map;
import javax.jcr.Session;

public interface QuarterlyBDORepositoryClient {

	boolean createOrUpdateQuarterlyBDOData(String action, Map<String, String[]> params, Session session) throws Exception; 
	
	boolean createOrUpdateEmployeeProfileData(Map<String, String[]> params,  Session session)  throws Exception;
	
    int getEmployeeProfileBeginYear(String userID, Session session) throws Exception;
	
	Map<String, String[]> getQuarterlyBDOData(int quarterNumber, int annualYear, String userID, Session session, boolean escapeNewline) throws Exception;
	
	public Map<String, String[]> getEmployeeProfileData(String userID, Session session) throws Exception;
}
