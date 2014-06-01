package com.adobe.gdc.checkin.impl;

import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.Map;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.adobe.gdc.checkin.QuarterlyBDOCalendarService;
import com.adobe.gdc.checkin.constants.QuartelyBDOConstants;

@Component (metatype = true)
@Service(QuarterlyBDOCalendarService.class)
public class QuarterlyBDOCalendarServiceImpl implements QuarterlyBDOCalendarService{

	private static final Logger log = LoggerFactory.getLogger(QuarterlyBDOCalendarServiceImpl.class);
	
	@Override
	public Map<String, Calendar> getAllQuartersDateRangeMap(int annualYear) {
		
		Map<String, Calendar> quarterlyDateRangeMap = new LinkedHashMap<String, Calendar>();
		
		Calendar calendar = Calendar.getInstance(); 			
		calendar.set(annualYear-1, Calendar.DECEMBER, 01); 	
		quarterlyDateRangeMap.put(QuartelyBDOConstants.START_DATE_QUARTER1, calendar);
		
		calendar = Calendar.getInstance();
		calendar.set(annualYear, Calendar.FEBRUARY, (annualYear)%4 == 0 ? 29 : 28); 	
		quarterlyDateRangeMap.put(QuartelyBDOConstants.END_DATE_QUARTER1, calendar);
		
		calendar = Calendar.getInstance();
		calendar.set(annualYear, Calendar.MARCH, 01); 	
		quarterlyDateRangeMap.put(QuartelyBDOConstants.START_DATE_QUARTER2, calendar);
	
		calendar = Calendar.getInstance();
		calendar.set(annualYear, Calendar.MAY, 31); 	
		quarterlyDateRangeMap.put(QuartelyBDOConstants.END_DATE_QUARTER2, calendar);
		
		calendar = Calendar.getInstance();
		calendar.set(annualYear, Calendar.JUNE, 01); 	
		quarterlyDateRangeMap.put(QuartelyBDOConstants.START_DATE_QUARTER3, calendar);
		
		calendar = Calendar.getInstance();
		calendar.set(annualYear, Calendar.AUGUST, 31); 	
		quarterlyDateRangeMap.put(QuartelyBDOConstants.END_DATE_QUARTER3, calendar);
		
		calendar = Calendar.getInstance();
		calendar.set(annualYear, Calendar.SEPTEMBER, 01); 	
		quarterlyDateRangeMap.put(QuartelyBDOConstants.START_DATE_QUARTER4, calendar);
		
		calendar = Calendar.getInstance();
		calendar.set(annualYear, Calendar.NOVEMBER, 30); 	
		quarterlyDateRangeMap.put(QuartelyBDOConstants.END_DATE_QUARTER4, calendar);
		
		return quarterlyDateRangeMap;
	}
	
	@Override
	public String getQuarterStatus(Calendar quarterStartDate, Calendar quarterEndDate) {
		
		String quarterStatus = QuartelyBDOConstants.EMPTY_STRING;

		Calendar today = Calendar.getInstance();
	
		today.set(Calendar.MILLISECOND, 0);
		quarterStartDate.set(Calendar.MILLISECOND, 0);
		quarterEndDate.set(Calendar.MILLISECOND, 0);
		today.set(Calendar.SECOND, 0);
		quarterStartDate.set(Calendar.SECOND, 0);
		quarterEndDate.set(Calendar.SECOND, 0);
		
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
		
	    int bufferDays = 15;
	    Calendar extendedQuarterEndDate = quarterEndDate;
	    extendedQuarterEndDate.add(Calendar.DAY_OF_MONTH, bufferDays);
	    
		Calendar today = Calendar.getInstance();
		
		today.set(Calendar.MILLISECOND, 0);
		quarterStartDate.set(Calendar.MILLISECOND, 0);
		extendedQuarterEndDate.set(Calendar.MILLISECOND, 0);
		today.set(Calendar.SECOND, 0);
		quarterStartDate.set(Calendar.SECOND, 0);
		extendedQuarterEndDate.set(Calendar.SECOND, 0);
		
		
		if( (quarterStartDate.compareTo(today) <= 0) && (extendedQuarterEndDate.compareTo(today) >= 0) ) {
			return true;
		} 
		
		return false;
	}
	
	@Override
	public int getcurrentQuarterAnnualYear() {
		Calendar calendar = Calendar.getInstance();
		int currentYear = calendar.get(Calendar.YEAR);
		if(calendar.get(Calendar.MONTH) == Calendar.DECEMBER) {
			currentYear ++;
		}
		return currentYear;
	}

}
