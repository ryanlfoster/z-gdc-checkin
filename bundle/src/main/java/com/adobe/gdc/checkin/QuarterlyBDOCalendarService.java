package com.adobe.gdc.checkin;

import java.util.Calendar;
import java.util.Map;

public interface QuarterlyBDOCalendarService {
	
	 Map<String, Calendar> getAllQuartersDateRangeMap(int annualYear);
	 String getQuarterStatus(Calendar quarterStartDate, Calendar quarterEndDate);
	 int getcurrentQuarterAnnualYear();
	 boolean isOpenToEdit(Calendar quarterStartCalendarDate, Calendar quarterEndCalendarDate);
}
