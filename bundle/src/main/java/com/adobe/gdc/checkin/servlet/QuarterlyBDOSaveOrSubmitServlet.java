package com.adobe.gdc.checkin.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import javax.jcr.Session;
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
import org.apache.sling.commons.json.JSONException;
import org.apache.sling.commons.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.adobe.gdc.checkin.QuarterlyBDORepositoryClient;
import com.adobe.gdc.checkin.utils.QuarterlyBDOUtils;

@Component(label = "GDC Check-in Quarterly BDO Form Save or Submit Servlet", description = "GDC Check-in Quarterly BDO Form Save or Submit Servlet")
@Service(Servlet.class)
@Properties({
	@Property(name = "sling.servlet.resourceTypes", value = { "cq/Page" }),
	@Property(name = "sling.servlet.methods", value = { "POST" }),
	@Property(name = "sling.servlet.selectors", value = { "bdo","save","submit" }) })
public class QuarterlyBDOSaveOrSubmitServlet extends SlingAllMethodsServlet{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final Logger log = LoggerFactory.getLogger(QuarterlyBDOSaveOrSubmitServlet.class);
	
	@Reference
	private QuarterlyBDORepositoryClient quarterlyBDORepositoryClient;
	
	@Override
	protected void doPost(SlingHttpServletRequest request,SlingHttpServletResponse response) throws ServletException,IOException {	
		
		boolean result = true;
		 JSONObject responseObject = new JSONObject();
		  
		String[] selectors = request.getRequestPathInfo().getSelectors();
		String action = "";
		if (selectors != null && selectors.length > 0) {
		    action = selectors[1];
		}

		//Read the request parameters
	    Map<String, String[]> params = QuarterlyBDOUtils.readFormParams(request);
	     
	    log.info("Processing [QuarterlyBDOSaveOrSubmitServlet] for action - {} with request params {}", action, params);
	    
	    if(StringUtils.isBlank(action) && !action.equals("save") && !action.equals("submit")) {
	    	log.error("Invalid action : {}", action);
	    	result = false;
	    }
	    
	    if(result && params.size() <= 0) {
	    	log.error("Invalid Input Params {}", params);
	    	result = false;
	    }
	    try {
	   if(result) {
			result = quarterlyBDORepositoryClient.createOrUpdateQuarterlyBDOData(action, params, getSession(request));
	   }
	     
	   if(result && action.equals("submit")) {
		   //send email to manager
		  
	   }
	   responseObject.put("success", result);
	   
		} catch (JSONException e) {
			log.error("[JSONException]", e);
		} catch (Exception e) {
			log.error("[Exception]", e);
		}
	    
	    response.setContentType("application/json");
	    response.setCharacterEncoding("UTF-8");
	    PrintWriter writer = response.getWriter();
	    writer.write(responseObject.toString()); 
	}
	
	
	@Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException, IOException {
        log.info("QuarterlyBDOSaveOrSubmit Servlet");
        doPost(request, response);

    }
	
	private Session getSession(SlingHttpServletRequest request) {
		return request.getResourceResolver().adaptTo(Session.class);
	}
}
