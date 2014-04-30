package com.adobe.gdc.checkin.servlet;

import com.day.cq.commons.jcr.JcrUtil;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;
import org.apache.felix.scr.annotations.sling.SlingServlet;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.*;
import javax.servlet.ServletException;
import javax.xml.crypto.dsig.spec.XSLTTransformParameterSpec;
import java.io.*;
import java.util.Calendar;

/**
 * @author prajesh
 *         Date: 27/4/14
 *         Time: 9:50 PM
 */


@SlingServlet(paths = {"/bin/checkin/reports/managerreport"}, extensions = {"html"}, methods = {"GET"})


public class GenerateReportsServlet extends SlingSafeMethodsServlet {

    private Logger logger = LoggerFactory.getLogger(GenerateReportsServlet.class);

    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException, IOException {


       logger.info("****GenerateReportsServlets***********");

        response.setContentType("text/html");

        PrintWriter out = response.getWriter();



        out.println("Hello world");

        Session session = request.getResourceResolver().adaptTo(Session.class);


        try {
            getOrGenerateExcel(session);
        } catch (RepositoryException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }


    }

    public void getOrGenerateExcel(Session session) throws IOException, RepositoryException {
        Workbook managerreport = new XSSFWorkbook();

        /*File file = new File("/tmp/managerreport.xlsx");

        FileInputStream fis = new FileInputStream(file);

        Node node = session.getNode("/tmp");
        ValueFactory valueFactory = session.getValueFactory();
        Binary contentValue = valueFactory.createBinary(fis);
        Node fileNode = node.addNode("managerReport", "nt:file");
        fileNode.addMixin("mix:referenceable");
        Node resNode = fileNode.addNode("jcr:content", "nt:resource");
        resNode.setProperty("jcr:mimeType", "application/xls");
        resNode.setProperty("jcr:data", contentValue);
        Calendar lastModified = Calendar.getInstance();
        lastModified.setTimeInMillis(lastModified.getTimeInMillis());
        resNode.setProperty("jcr:lastModified", lastModified);
        session.save();
*/

        FileOutputStream fo = new FileOutputStream("/home/prajesh/Desktop/managerreport1.xlsx");

        Sheet sheet1 = managerreport.createSheet();

        Row headerRow = generateHeaderRow(sheet1);


        managerreport.write(fo);

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bos.writeTo(fo);



    }


    public Row generateHeaderRow(Sheet sheet)
    {
        if(sheet==null) return null;

        Row headerRow = sheet.createRow(1);


        headerRow.createCell(0).setCellValue("EmployeeID");
        headerRow.createCell(1).setCellValue("Name");
        headerRow.createCell(2).setCellValue("Manager Name");
        headerRow.createCell(3).setCellValue("BDO Score for Q1");
        headerRow.createCell(4).setCellValue("Notes");


        Cell cell5 = headerRow.createCell(5);

        CellStyle style1 = headerRow.getSheet().getWorkbook().createCellStyle();
        style1.setFillForegroundColor(IndexedColors.YELLOW.index);

        cell5.setCellStyle(style1);

        SheetConditionalFormatting sheetCF = sheet.getSheetConditionalFormatting();

        //Fill the header row with yellow
        ConditionalFormattingRule rule1 = sheetCF.createConditionalFormattingRule(ComparisonOperator.GT, "70");
        PatternFormatting fill1 = rule1.createPatternFormatting();
        fill1.setFillBackgroundColor(IndexedColors.LIGHT_YELLOW.index);
        fill1.setFillPattern(PatternFormatting.SOLID_FOREGROUND);


        CellRangeAddress[] regions = {
                CellRangeAddress.valueOf("A1:A6")
        };

        sheetCF.addConditionalFormatting(regions, rule1);


        return headerRow;
    }

}
