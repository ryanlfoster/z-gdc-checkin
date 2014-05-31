package com.adobe.gdc.checkin.servlet;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import javax.jcr.Session;
import javax.servlet.ServletException;
import org.apache.commons.lang.StringUtils;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.sling.SlingServlet;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.apache.sling.commons.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.adobe.gdc.checkin.QuarterlyBDORepositoryClient;
import com.adobe.gdc.checkin.UserManagementService;
import com.adobe.gdc.checkin.constants.QuartelyBDOConstants;

/**
 * @author prajesh
 *         Date: 27/4/14
 *         Time: 9:50 PM
 */

@SlingServlet(resourceTypes={"sling/servlet/default"}, methods={"GET"},selectors={"managerreport"}, extensions = { "html" })
public class GenerateReportsServlet extends SlingSafeMethodsServlet {

    private Logger log = LoggerFactory.getLogger(GenerateReportsServlet.class);
    
    @Reference
	UserManagementService userManagementService;
	
	@Reference
	QuarterlyBDORepositoryClient quarterlyBDORepositoryClient;

    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException, IOException {

    	// Read the request parameters
		String managersID = request.getParameter(QuartelyBDOConstants.MANAGERS_ID);
		int quarterNumber = Integer.parseInt(request.getParameter(QuartelyBDOConstants.QUARTER_NUMBER));
		int annualYear = Integer.parseInt(request.getParameter(QuartelyBDOConstants.ANNUAL_YEAR));
		
		log.info("Generating BDO Report for-> managersID="+ managersID+",quarterNumber="+ quarterNumber+ ",annualYear=" + annualYear);
		
		Session session = getSession(request);
		
		if(!userManagementService.isAnonymous(session)) {
    			 
			//Generate data to be written in the file
	        Map<String, Object[]> resultData = new TreeMap<String, Object[]>();
	        resultData.put("1", new Object[] {
								        		QuartelyBDOConstants.EMPLOYEE_ID_TITLE,
								        		QuartelyBDOConstants.NAME_TITLE,
								        		QuartelyBDOConstants.MANAGER_NAME_TITLE,
								        		QuartelyBDOConstants.BDO_SCORE_FOR_Q_TITLE + quarterNumber,
								        		QuartelyBDOConstants.NOTES_TITLE
	        								});
	       				
			try {
				//Get All Direct Reportees of the manager
				String[] directReportees = userManagementService.getManagersDirectReportees(managersID);
				
				String managerName = userManagementService.getEmployeeName(managersID);
		    	
				for(int i=0; i<directReportees.length; i++) {
					Map<String, String[]> employeeProfileDataMap = quarterlyBDORepositoryClient.getEmployeeProfileData(directReportees[i]);
					
					//If employee exists in the repository
					if(employeeProfileDataMap != null && employeeProfileDataMap.size() > 0 ) {
						Map<String, String[]> employeeBDODataMap = quarterlyBDORepositoryClient.getQuarterlyBDOData(quarterNumber, annualYear, directReportees[i], false);					
						int index = resultData.size() + 1;
						String[] employeeBDOData = getEmployeeBDOData(employeeProfileDataMap, employeeBDODataMap, managerName);
						resultData.put(index+"",employeeBDOData);
					}
				}
				response.setContentType("application/vnd.ms-excel");
				response.setHeader("Expires:", "0"); // eliminates browser caching
			    response.setHeader("Content-Disposition",  "attachment; filename=bdo_report.xlsx");
				
			    XSSFWorkbook workbook = writeDataMapToFile(resultData);
			    
			    ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
			    workbook.write(outByteStream);
			    byte [] outArray = outByteStream.toByteArray();
			    response.setContentLength(outArray.length);
			    OutputStream outStream = response.getOutputStream();
			    outStream.write(outArray);
			    outStream.flush();
			    outStream.close();
			}
			
			catch(IOException e) {
				log.error("[IOException]",e);
			}
			catch(Exception e) {
				log.error("[Exception]",e);
			}
		} 
		else {
			response.getWriter().write("Session timed out >> Please login");
		}
    }
    
    
    private XSSFWorkbook writeDataMapToFile( Map<String, Object[]> resultData) throws IOException, Exception{
    	//Blank workbook
		XSSFWorkbook workbook = new XSSFWorkbook(); 
		
		//Create a blank sheet
		XSSFSheet sheet = workbook.createSheet("Employee BDO Report");
				
		//Iterate over data and write to sheet
		Set<String> keyset = resultData.keySet();
		int rownum = 0;
		for (String key : keyset)
		{
		    Row row = sheet.createRow(rownum++);
		    Object [] objArr = resultData.get(key);
		    int cellnum = 0;
		    for (Object obj : objArr)
		    {
		       Cell cell = row.createCell(cellnum++);
		       if(obj instanceof String)
		            cell.setCellValue((String)obj);
		        else if(obj instanceof Integer)
		            cell.setCellValue((Integer)obj);
		       
		    }
		}
	
		styleExcelSheet(workbook, sheet);
		
		return workbook;
    	
    }
      
    
    private void styleExcelSheet(XSSFWorkbook workbook, Sheet sheet) throws Exception{
    	
    	//Define style for the report header
    	CellStyle  headerStyle = workbook.createCellStyle();
      
    	headerStyle.setFillForegroundColor(HSSFColor.GREEN.index);
    	headerStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
    	headerStyle.setWrapText(true);
      	
    	//every sheet has rows, iterate over them
        Iterator<Row> rowIterator = sheet.iterator();
        
        //Style only the Report Header
        if (rowIterator.hasNext()) {
        	//Get the row object
            Row row = rowIterator.next();
            //Every row has columns, get the column iterator and iterate over them
            Iterator<Cell> cellIterator = row.cellIterator();        
            while (cellIterator.hasNext()) {
                //Get the Cell object
                Cell cell = cellIterator.next();
                cell.setCellStyle(headerStyle);
            }         
        }
        
       //Style for the Report cells
    	CellStyle  cellStyle = workbook.createCellStyle();
    	cellStyle.setWrapText(true);
        
        while (rowIterator.hasNext()) {
        	//Get the row object
            Row row = rowIterator.next();
            //Every row has columns, get the column iterator and iterate over them
            Iterator<Cell> cellIterator = row.cellIterator();        
            while (cellIterator.hasNext()) {
                //Get the Cell object
                Cell cell = cellIterator.next();
                cell.setCellStyle(cellStyle);
            }         
        }
        
        //Auto-size the columns
        for (int i=0; i<5; i++) {
        	sheet.autoSizeColumn(i);
        }
        
    }
    
    
	private String[] getEmployeeBDOData( Map<String, String[]> employeeProfileDataMap, Map<String, String[]> employeeBDODataMap, String managerName) throws JSONException {			
		
			String[] employeeBDODataArray = new String[5];
			employeeBDODataArray[0] = employeeProfileDataMap.get(QuartelyBDOConstants.EMPLOYEE_ID)!= null 
										? employeeProfileDataMap.get(QuartelyBDOConstants.EMPLOYEE_ID)[0]
										:  QuartelyBDOConstants.EMPTY_STRING;
										
			employeeBDODataArray[1] = employeeProfileDataMap.get(QuartelyBDOConstants.NAME) != null 
										? employeeProfileDataMap.get(QuartelyBDOConstants.NAME)[0]
										: QuartelyBDOConstants.EMPTY_STRING;
										
			employeeBDODataArray[2]	= managerName;
			
			employeeBDODataArray[3]	= employeeBDODataMap.get(QuartelyBDOConstants.BDO_SCORE) != null 
										? employeeBDODataMap.get(QuartelyBDOConstants.BDO_SCORE)[0]
										: QuartelyBDOConstants.EMPTY_STRING;
			
			employeeBDODataArray[4]	= employeeBDODataMap.get(QuartelyBDOConstants.ACHIEVEMENTS) != null
										? formatArrayToString(employeeBDODataMap.get(QuartelyBDOConstants.ACHIEVEMENTS))
										: QuartelyBDOConstants.EMPTY_STRING;
				
			return employeeBDODataArray;
		}
	
	
	private String formatArrayToString(String[] arrayValue) {
		String crLf = Character.toString((char)13) + Character.toString((char)10);
		String stringValue = QuartelyBDOConstants.EMPTY_STRING;
		int numbering = 1;
		
		List<String> achievementList = new ArrayList<String>();
		
		for(int index=0; index<arrayValue.length; index++) {
			if(StringUtils.isNotBlank(arrayValue[index])) {
				achievementList.add(arrayValue[index]);
			}
		}
		
		for(String achievement :  achievementList) {
				stringValue = stringValue + (numbering) +". " + achievement + crLf;
				numbering++;
		}
		return stringValue;
	}
	
    private Session getSession(SlingHttpServletRequest request) {
		return request.getResourceResolver().adaptTo(Session.class);
	}
   
}
