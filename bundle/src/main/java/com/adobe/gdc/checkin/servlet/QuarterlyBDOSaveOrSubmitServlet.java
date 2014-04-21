package com.adobe.gdc.checkin.servlet;

import java.io.IOException;
import java.util.Map;

import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Properties;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Service;
import org.apache.felix.scr.annotations.sling.SlingServlet;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
	
	@Override
	protected void doPost(SlingHttpServletRequest request,SlingHttpServletResponse response) throws ServletException,IOException {	
		
		String[] selectors = request.getRequestPathInfo().getSelectors();
		String action = "";
		if (selectors != null && selectors.length > 0) {
		    action = selectors[1];
		}
		
		log.info("************action="+action);
		//Wrap request params in a map
	    Map<String, String> params = QuarterlyBDOUtils.readFormParams(request);
	    
	    for(Map.Entry<String, String> entry : params.entrySet()) {
	    	log.info("Key:"+entry.getKey());
	    	log.info(", Value:"+entry.getValue());
	    }
	    
	}
	
	
	@Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException, IOException {
        log.info("QuarterlyBDOSaveOrSubmit Servlet");
        doPost(request, response);

    }
}
