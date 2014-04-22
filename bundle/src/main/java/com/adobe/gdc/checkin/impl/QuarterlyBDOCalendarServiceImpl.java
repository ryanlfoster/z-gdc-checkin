package com.adobe.gdc.checkin.impl;

import java.util.LinkedHashMap;
import java.util.Map;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Calendar;
import com.adobe.gdc.checkin.constants.QuartelyBDOConstants;

import com.adobe.gdc.checkin.QuarterlyBDOCalendarService;

@Component (metatype = true)
@Service(QuarterlyBDOCalendarService.class)
public class QuarterlyBDOCalendarServiceImpl implements QuarterlyBDOCalendarService{

	private static final Logger log = LoggerFactory.getLogger(QuarterlyBDOCalendarServiceImpl.class);
	
	@Override
	public Map<String, Calendar> getAllQuartersDateRangeMap() {
		
		Map<String, Calendar> quarterlyDateRangeMap = new LinkedHashMap<String, Calendar>();
		
		Integer firstQuarterYear = null;
		Calendar calendar = Calendar.getInstance(); 
		
		if(calendar.get(Calendar.MONTH) == Calendar.DECEMBER) {
			firstQuarterYear = calendar.get(Calendar.YEAR);
		} 
		else {
			firstQuarterYear = calendar.get(Calendar.YEAR) - 1;
		}
			
		calendar.set(firstQuarterYear, Calendar.DECEMBER, 01); 	
		quarterlyDateRangeMap.put(QuartelyBDOConstants.START_DATE_QUARTER1, calendar);
		
		calendar = Calendar.getInstance();
		calendar.set(firstQuarterYear+1, Calendar.FEBRUARY, (firstQuarterYear+1)%4 == 0 ? 29 : 28); 	
		quarterlyDateRangeMap.put(QuartelyBDOConstants.END_DATE_QUARTER1, calendar);
		
		calendar = Calendar.getInstance();
		calendar.set(firstQuarterYear+1, Calendar.MARCH, 01); 	
		quarterlyDateRangeMap.put(QuartelyBDOConstants.START_DATE_QUARTER2, calendar);
	
		calendar = Calendar.getInstance();
		calendar.set(firstQuarterYear+1, Calendar.MAY, 31); 	
		quarterlyDateRangeMap.put(QuartelyBDOConstants.END_DATE_QUARTER2, calendar);
		
		calendar = Calendar.getInstance();
		calendar.set(firstQuarterYear+1, Calendar.JUNE, 01); 	
		quarterlyDateRangeMap.put(QuartelyBDOConstants.START_DATE_QUARTER3, calendar);
		
		calendar = Calendar.getInstance();
		calendar.set(firstQuarterYear+1, Calendar.AUGUST, 31); 	
		quarterlyDateRangeMap.put(QuartelyBDOConstants.END_DATE_QUARTER3, calendar);
		
		calendar = Calendar.getInstance();
		calendar.set(firstQuarterYear+1, Calendar.SEPTEMBER, 01); 	
		quarterlyDateRangeMap.put(QuartelyBDOConstants.START_DATE_QUARTER4, calendar);
		
		calendar = Calendar.getInstance();
		calendar.set(firstQuarterYear+1, Calendar.NOVEMBER, 30); 	
		quarterlyDateRangeMap.put(QuartelyBDOConstants.END_DATE_QUARTER4, calendar);
		
		return quarterlyDateRangeMap;
	}
	
	@Override
	public String getQuarterStatus(Calendar quarterStartDate, Calendar quarterEndDate) {
		
		String quarterStatus = null;
		
		Calendar today = Calendar.getInstance();
		
		if( (quarterStartDate.compareTo(today) < 0) && (quarterEndDate.compareTo(today) > 0) ) {
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
	public int getcurrentQuarterAnnualYear() {
		Calendar calendar = Calendar.getInstance();
		int currentYear = calendar.get(Calendar.YEAR);
		if(calendar.get(Calendar.MONTH) == Calendar.DECEMBER) {
			currentYear ++;
		}
		return currentYear;
	}

}
