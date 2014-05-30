package com.adobe.gdc.checkin;

import java.util.Calendar;
import java.util.Map;

public interface QuarterlyBDOCalendarService {

	 Map<String, Calendar> getAllQuartersDateRangeMap();
	 String getQuarterStatus(Calendar quarterStartDate, Calendar quarterEndDate);
	 int getcurrentQuarterAnnualYear();
	 public int getEmployeeProfileBeginYear(String userID);
	 boolean isOpenToEdit(Calendar quarterStartCalendarDate, Calendar quarterEndCalendarDate);
}