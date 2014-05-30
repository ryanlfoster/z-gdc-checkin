package com.adobe.gdc.checkin.impl;

import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.Property;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import org.apache.commons.lang.StringUtils;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.apache.jackrabbit.commons.JcrUtils;
import org.apache.sling.jcr.api.SlingRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.adobe.gdc.checkin.QuarterlyBDOCalendarService;
import com.adobe.gdc.checkin.constants.QuartelyBDOConstants;
import com.adobe.gdc.checkin.utils.QuarterlyBDOUtils;

@Component (metatype = true)
@Service(QuarterlyBDOCalendarService.class)
public class QuarterlyBDOCalendarServiceImpl implements QuarterlyBDOCalendarService {

	private static final Logger log = LoggerFactory.getLogger(QuarterlyBDOCalendarServiceImpl.class);

	@Reference
	SlingRepository repository;

	@Override
	public Map<String, Calendar> getAllQuartersDateRangeMap() {

		Map<String, Calendar> quarterlyDateRangeMap = new LinkedHashMap<String, Calendar>();
		Calendar q1StartDate, q1EndDate, q2StartDate, q2EndDate, q3StartDate, q3EndDate, q4StartDate, q4EndDate;
		Session adminSession = null;
 
		try {

			adminSession = getAdminSession();
			//Set a default calendar object - with an obsolete time
			Calendar obseleteDate = Calendar.getInstance(); 			
			obseleteDate.set(1990, Calendar.JANUARY, 01); 

			q1StartDate = q1EndDate = q2StartDate = q2EndDate = q3StartDate = q3EndDate = q4StartDate = q4EndDate = obseleteDate;


			//First Get BDO Calendar Node from the JCR repository
			Node gdcBdoCalendarNode = JcrUtils.getNodeIfExists(QuartelyBDOConstants.GDC_CHECKIN_REPOSITORY_CALENDAR_BASE_PATH, adminSession);

			if(gdcBdoCalendarNode != null) {

				Map<String, Calendar> calendarProperties = QuarterlyBDOUtils.readCalendarProperties(gdcBdoCalendarNode);

				q1StartDate = calendarProperties.get(QuartelyBDOConstants.START_DATE_QUARTER1) != null 
											? calendarProperties.get(QuartelyBDOConstants.START_DATE_QUARTER1)
											: obseleteDate;

				q1EndDate = calendarProperties.get(QuartelyBDOConstants.END_DATE_QUARTER1) != null 
											? calendarProperties.get(QuartelyBDOConstants.END_DATE_QUARTER1) 
											: obseleteDate;							

				q2StartDate = calendarProperties.get(QuartelyBDOConstants.START_DATE_QUARTER2) != null 
											? calendarProperties.get(QuartelyBDOConstants.START_DATE_QUARTER2)
											: obseleteDate;

				q2EndDate = calendarProperties.get(QuartelyBDOConstants.END_DATE_QUARTER2) != null 
											? calendarProperties.get(QuartelyBDOConstants.END_DATE_QUARTER2)
											: obseleteDate;

				q3StartDate = calendarProperties.get(QuartelyBDOConstants.START_DATE_QUARTER3) != null 
											? calendarProperties.get(QuartelyBDOConstants.START_DATE_QUARTER3)
											: obseleteDate;

				q3EndDate = calendarProperties.get(QuartelyBDOConstants.END_DATE_QUARTER3) != null 
											? calendarProperties.get(QuartelyBDOConstants.END_DATE_QUARTER3)
											: obseleteDate;

				q4StartDate = calendarProperties.get(QuartelyBDOConstants.START_DATE_QUARTER4) != null 
											? calendarProperties.get(QuartelyBDOConstants.START_DATE_QUARTER4)
											: obseleteDate;

				q4EndDate = calendarProperties.get(QuartelyBDOConstants.END_DATE_QUARTER4) != null 
											? calendarProperties.get(QuartelyBDOConstants.END_DATE_QUARTER4)
											: obseleteDate;	
			}

			quarterlyDateRangeMap.put(QuartelyBDOConstants.START_DATE_QUARTER1, q1StartDate);
			quarterlyDateRangeMap.put(QuartelyBDOConstants.END_DATE_QUARTER1, q1EndDate);
			quarterlyDateRangeMap.put(QuartelyBDOConstants.START_DATE_QUARTER2, q2StartDate);
			quarterlyDateRangeMap.put(QuartelyBDOConstants.END_DATE_QUARTER2, q2EndDate);
			quarterlyDateRangeMap.put(QuartelyBDOConstants.START_DATE_QUARTER3, q3StartDate);
			quarterlyDateRangeMap.put(QuartelyBDOConstants.END_DATE_QUARTER3, q3EndDate);
			quarterlyDateRangeMap.put(QuartelyBDOConstants.START_DATE_QUARTER4, q4StartDate);
			quarterlyDateRangeMap.put(QuartelyBDOConstants.END_DATE_QUARTER4, q4EndDate);	

		}
		catch(Exception e) {
			log.error("[Exception]", e);
		}
		finally {
			if(adminSession != null && adminSession.isLive()) {
				adminSession.logout();
			}
		}


		return quarterlyDateRangeMap;
	}


	@Override
	public String getQuarterStatus(Calendar quarterStartDate, Calendar quarterEndDate) {

		String quarterStatus = QuartelyBDOConstants.EMPTY_STRING;

		Calendar today = Calendar.getInstance();
		
		discardCalendarTimeStamp(today);
		discardCalendarTimeStamp(quarterStartDate);
		discardCalendarTimeStamp(quarterEndDate);

		if( (quarterStartDate.compareTo(today) <= 0) && (quarterEndDate.compareTo(today) >= 0) ) {
			quarterStatus = QuartelyBDOConstants.CURRENT_QUARTER;
		} 
		else if(quarterEndDate.compareTo(today) < 0) {
			quarterStatus = QuartelyBDOConstants.PREVIOUS_QUARTER;
		} 
		else {
			quarterStatus = QuartelyBDOConstants.FUTURE_QUARTER;
		}

		return quarterStatus;
	}


	@Override
	public boolean isOpenToEdit(Calendar quarterStartDate, Calendar quarterEndDate) {

		Session adminSession = null;

		try {
			int bufferDays = 15;
			adminSession = getAdminSession();
			//Get BDO Calendar Node from the JCR repository
			Node gdcBdoCalendarNode = JcrUtils.getNodeIfExists(QuartelyBDOConstants.GDC_CHECKIN_REPOSITORY_CALENDAR_BASE_PATH, adminSession);	
			if(gdcBdoCalendarNode != null && gdcBdoCalendarNode.hasProperty(QuartelyBDOConstants.BUFFER_DAYS)) {
				Property bufferDaysProperty = gdcBdoCalendarNode.getProperty(QuartelyBDOConstants.BUFFER_DAYS);
				if(StringUtils.isNotBlank(bufferDaysProperty.getValue().getString())) {
					bufferDays = Integer.parseInt(bufferDaysProperty.getValue().getString());
				}
			}

		    Calendar extendedQuarterEndDate = Calendar.getInstance();
		    extendedQuarterEndDate.setTime(quarterEndDate.getTime());
		    extendedQuarterEndDate.add(Calendar.DAY_OF_MONTH, bufferDays);

			Calendar today = Calendar.getInstance();
			
			discardCalendarTimeStamp(today);
			discardCalendarTimeStamp(quarterStartDate);
			discardCalendarTimeStamp(extendedQuarterEndDate);

			if( (quarterStartDate.compareTo(today) <= 0) && (extendedQuarterEndDate.compareTo(today) >= 0) ) {
				return true;
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
	public int getcurrentQuarterAnnualYear() {

		Calendar calendar = Calendar.getInstance();
		int currentYear = calendar.get(Calendar.YEAR);
		Session adminSession = null;

		try {

			adminSession = getAdminSession();

			String repositoryPath = QuartelyBDOConstants.GDC_CHECKIN_REPOSITORY_CALENDAR_BASE_PATH;

			//Get BDO Calendar Node from the JCR repository
			Node gdcBdoCalendarNode = JcrUtils.getNodeIfExists(repositoryPath, adminSession);	

			if(gdcBdoCalendarNode != null && gdcBdoCalendarNode.hasProperty(QuartelyBDOConstants.CURRENT_FISCAL_YEAR)) {
				Property currentFiscalYearProperty = gdcBdoCalendarNode.getProperty(QuartelyBDOConstants.CURRENT_FISCAL_YEAR);
				if(StringUtils.isNotBlank(currentFiscalYearProperty.getValue().getString())) {
					currentYear = Integer.parseInt(currentFiscalYearProperty.getValue().getString());
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

		return currentYear;
	}


	/**
	 * This API gets a start year from when the Employee's data is available in the
	 * system. To formulate a generic solution for both employee and manager,
	 * first it tries to get the min year from the Employees repository.
	 * If thats unavailable, it gets a default begin-year from the GDC Calendar node.
	 * If none of the above is available, it returns the current year.
	 */
	@Override
	public int getEmployeeProfileBeginYear(String userID)  {

		int minYear = Calendar.getInstance().get(Calendar.YEAR);
		boolean beginYearExists = false;

		String repositoryPath = QuarterlyBDOUtils.getEmployeeProfileBasePath(userID); 

		Session adminSession = null;

		try {

			adminSession = getAdminSession();
			//Get Employee Node
			Node employeeNode = JcrUtils.getNodeIfExists(repositoryPath, adminSession);

			if(employeeNode != null) {

				NodeIterator years = employeeNode.getNodes();

				while(years != null && years.hasNext()) {

					Node yearNode = (Node) years.next();

					int year = Integer.parseInt(yearNode.getName());

					minYear = Math.min(minYear,year);

					beginYearExists = true;
				}		
			}

			if(!beginYearExists) {
				//Get BDO Calendar Node from the JCR repository
				Node gdcBdoCalendarNode = JcrUtils.getNodeIfExists(QuartelyBDOConstants.GDC_CHECKIN_REPOSITORY_CALENDAR_BASE_PATH, adminSession);	

				if(gdcBdoCalendarNode != null && gdcBdoCalendarNode.hasProperty(QuartelyBDOConstants.BEGIN_YEAR)) {
					Property currentFiscalYearProperty = gdcBdoCalendarNode.getProperty(QuartelyBDOConstants.BEGIN_YEAR);
					if(StringUtils.isNotBlank(currentFiscalYearProperty.getValue().getString())) {
						minYear = Integer.parseInt(currentFiscalYearProperty.getValue().getString());
						beginYearExists = true;
					}
				}

			}

			if(!beginYearExists) {
				minYear = getcurrentQuarterAnnualYear();
			}

		}
		catch(Exception e) {
			log.error("[Exception]", e);
		}
		finally{
			if(adminSession != null && adminSession.isLive()) {
				adminSession.logout();
			}
		}

		return minYear;
	}


	private Session getAdminSession() throws RepositoryException {
		return repository.loginAdministrative(repository.getDefaultWorkspace());
	}
	
	private void discardCalendarTimeStamp(Calendar calendar) {
		calendar.set(Calendar.HOUR, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		
	}

}