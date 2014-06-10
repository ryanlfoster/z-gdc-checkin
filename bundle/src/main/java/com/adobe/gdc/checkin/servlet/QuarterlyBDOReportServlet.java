package com.adobe.gdc.checkin.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.apache.commons.lang.StringUtils;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Properties;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.commons.json.JSONArray;
import org.apache.sling.commons.json.JSONException;
import org.apache.sling.commons.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.gdc.checkin.QuarterlyBDORepositoryClient;
import com.adobe.gdc.checkin.constants.QuartelyBDOConstants;

@Component(label = "GDC Check-in Quarterly BDO Report Servlet", description = "GDC Check-in Quarterly BDO Report Servlet")
@Service(Servlet.class)
@Properties({
		@Property(name = "sling.servlet.resourceTypes", value = { "cq/Page" }),
		@Property(name = "sling.servlet.methods", value = { "GET" }),
		@Property(name = "sling.servlet.selectors", value = { "bdoreport" }) })
public class QuarterlyBDOReportServlet extends SlingAllMethodsServlet{

	private static final long serialVersionUID = 1L;

	private static final Logger log = LoggerFactory.getLogger(QuarterlyBDOReportServlet.class);
	
	@Reference
	QuarterlyBDORepositoryClient quarterlyBDORepositoryClient;
	
	private List<String> directReporteesList = new LinkedList<String>();
	
	@Override
	protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException, IOException {
	
		// Read the request parameters
		String managersID = request.getParameter(QuartelyBDOConstants.MANAGERS_ID);
		int quarterNumber = Integer.parseInt(request.getParameter(QuartelyBDOConstants.QUARTER_NUMBER));
		int annualYear = Integer.parseInt(request.getParameter(QuartelyBDOConstants.ANNUAL_YEAR));
		
		log.info("Processing [QuarterlyBDOReportServlet] with request params-> managersID="+ managersID+
										",quarterNumber="+ quarterNumber+ ",annualYear=" + annualYear);
		
		JSONObject responseObject = new JSONObject();
		
		try {
			
			//Get All Reportees of this manager	
			generateDirectReporteesList(managersID);
			
			//Remove self from the list of reportees
			directReporteesList.remove(managersID);
			
			String[] directReportees = directReporteesList.toArray(new String[directReporteesList.size()]);
			
			//Now invalidate the global directReporteesList
			directReporteesList = new ArrayList<String>();
			
			//Get the list of immediate direct Reportees of this manager
			String[] managersDirectReportees = quarterlyBDORepositoryClient.getDirectReportees(managersID);
			
			JSONArray directReportResultArray = new JSONArray();
			for(int i=0; i< directReportees.length; i++) {
				
				Map<String, String[]> employeeProfileDataMap = quarterlyBDORepositoryClient.getEmployeeProfileData(directReportees[i]);
				
				//If employee exists in the repository
				if(employeeProfileDataMap != null && employeeProfileDataMap.size() > 0 ) {
					
					Map<String, String[]> employeeBDODataMap = quarterlyBDORepositoryClient.getQuarterlyBDOData(quarterNumber, annualYear, directReportees[i], false);					
					int index = directReportResultArray.length() + 1;
					JSONObject employeeBDODataJson = getEmployeeBDOJSON(index,employeeProfileDataMap,employeeBDODataMap,directReportees[i],managersDirectReportees);
					directReportResultArray.put(employeeBDODataJson);
					
				}
				
			}
			
			responseObject.put(QuartelyBDOConstants.TOTAL_RECORDS, directReportResultArray.length());
			responseObject.put(QuartelyBDOConstants.AA_DATA, directReportResultArray);
			
		} catch (JSONException e) {
			log.error("[JSONException]",e);
		} catch(Exception e) {
			log.error("[Exception]",e);
		}
		
		log.info("BDO Report result:"+responseObject.toString());
		
		response.setContentType(QuartelyBDOConstants.RESPONSE_TYPE);
		response.setCharacterEncoding(QuartelyBDOConstants.CHARACTER_ENCODING);
		PrintWriter writer = response.getWriter();
		writer.write(responseObject.toString());

	}
	
	
	private JSONObject getEmployeeBDOJSON(int index, Map<String, String[]> employeeProfileDataMap, Map<String, String[]> employeeBDODataMap, String userID, String[] managersDirectReportees) throws Exception {
		
		JSONObject employeeBDODataJson = new JSONObject();
		employeeBDODataJson.put(QuartelyBDOConstants.INDEX, index);
		employeeBDODataJson.put(QuartelyBDOConstants.NAME, employeeProfileDataMap.get(QuartelyBDOConstants.NAME) != null 
															? employeeProfileDataMap.get(QuartelyBDOConstants.NAME)[0]
															: QuartelyBDOConstants.EMPTY_STRING);
		employeeBDODataJson.put(QuartelyBDOConstants.DESIGNATION, employeeBDODataMap.get(QuartelyBDOConstants.DESIGNATION) != null
															? employeeBDODataMap.get(QuartelyBDOConstants.DESIGNATION)[0]
															: employeeProfileDataMap.get(QuartelyBDOConstants.DESIGNATION) != null
															? employeeProfileDataMap.get(QuartelyBDOConstants.DESIGNATION)[0]
															: QuartelyBDOConstants.EMPTY_STRING);
		employeeBDODataJson.put(QuartelyBDOConstants.EMPLOYEE_ID, employeeProfileDataMap.get(QuartelyBDOConstants.EMPLOYEE_ID) != null
															? employeeProfileDataMap.get(QuartelyBDOConstants.EMPLOYEE_ID)[0]
														    : QuartelyBDOConstants.EMPTY_STRING);
		
		employeeBDODataJson.put(QuartelyBDOConstants.MANAGER, employeeProfileDataMap.get(QuartelyBDOConstants.MANAGER) != null
															? quarterlyBDORepositoryClient.getEmployeeName(employeeProfileDataMap.get(QuartelyBDOConstants.MANAGER)[0])
															: QuartelyBDOConstants.EMPTY_STRING);		
		employeeBDODataJson.put(QuartelyBDOConstants.BDO_SCORE, employeeBDODataMap.get(QuartelyBDOConstants.BDO_SCORE) != null 
															? employeeBDODataMap.get(QuartelyBDOConstants.BDO_SCORE)[0]
															: QuartelyBDOConstants.EMPTY_STRING);
		employeeBDODataJson.put(QuartelyBDOConstants.STATUS, employeeBDODataMap.get(QuartelyBDOConstants.STATUS) != null 
															? employeeBDODataMap.get(QuartelyBDOConstants.STATUS)[0]
															: QuartelyBDOConstants.NOT_SUBMITTED);
		employeeBDODataJson.put(QuartelyBDOConstants.USER_ID, userID);
		
		employeeBDODataJson.put(QuartelyBDOConstants.OBJECTIVES, employeeBDODataMap.get(QuartelyBDOConstants.OBJECTIVES)!= null 
																	? new JSONArray(Arrays.asList(employeeBDODataMap.get(QuartelyBDOConstants.OBJECTIVES)))
																	: new JSONArray());
		employeeBDODataJson.put(QuartelyBDOConstants.ACHIEVEMENTS, employeeBDODataMap.get(QuartelyBDOConstants.ACHIEVEMENTS) != null
																	? new JSONArray(Arrays.asList(employeeBDODataMap.get(QuartelyBDOConstants.ACHIEVEMENTS)))
																	: new JSONArray());
			
		String[] reporteesDirectReportees = quarterlyBDORepositoryClient.getDirectReportees(userID);
		
		//Add the manager's Ldap in the class param for the employee
		String className = employeeProfileDataMap.get(QuartelyBDOConstants.MANAGER) != null
											? employeeProfileDataMap.get(QuartelyBDOConstants.MANAGER)[0]
											: QuartelyBDOConstants.EMPTY_STRING;
		
		if(reporteesDirectReportees != null && reporteesDirectReportees.length >= 1) {
			className = className + " " + QuartelyBDOConstants.MANAGER_CLASS;
		}
		
		if(!(Arrays.asList(managersDirectReportees).contains(userID)) || StringUtils.isBlank(className)) {
			className = className + " " + QuartelyBDOConstants.HIDE_ROW; 
		}
		
		employeeBDODataJson.put(QuartelyBDOConstants.CLASS, className);
		return employeeBDODataJson;
	}
	
	private String generateDirectReporteesList(String userID) {
		if(StringUtils.isBlank(userID)) return QuartelyBDOConstants.EMPTY_STRING;
			
		directReporteesList.add(userID);
		String[] reporteesDirectReportees = quarterlyBDORepositoryClient.getDirectReportees(userID);
		
		for(String reportee : reporteesDirectReportees) {
			 generateDirectReporteesList(reportee);
		}
	
		return QuartelyBDOConstants.EMPTY_STRING;
	}
}
