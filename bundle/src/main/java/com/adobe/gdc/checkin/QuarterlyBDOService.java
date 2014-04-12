package com.adobe.gdc.checkin;

import java.util.Calendar;
import java.util.Map;

public interface QuarterlyBDOService {
	
	 Map<String, Calendar> getAllQuartersDateRangeMap();
	 String getQuarterStatus(Calendar quarterStartDate, Calendar quarterEndDate);
}
