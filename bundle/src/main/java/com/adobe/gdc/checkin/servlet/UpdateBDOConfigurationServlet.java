package com.adobe.gdc.checkin.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.jcr.Session;
import javax.servlet.Servlet;
import javax.servlet.ServletException;

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
import com.adobe.gdc.checkin.UserManagementService;
import com.adobe.gdc.checkin.constants.QuartelyBDOConstants;
import com.adobe.gdc.checkin.utils.QuarterlyBDOUtils;

@Component(label = "GDC BDO Application Configuration Servlet", description = "GDC BDO Application Configuration Servlet")
@Service(Servlet.class)
@Properties({
		@Property(name = "sling.servlet.resourceTypes", value = { "cq/Page" }),
		@Property(name = "sling.servlet.methods", value = { "POST" }),
		@Property(name = "sling.servlet.selectors", value = { "bdoconfig" }) })
public class UpdateBDOConfigurationServlet extends SlingAllMethodsServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final Logger log = LoggerFactory.getLogger(UpdateBDOConfigurationServlet.class);

	@Reference
	private QuarterlyBDORepositoryClient quarterlyBDORepositoryClient;

	@Reference
	UserManagementService userManagementService;

	@Override
	protected void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException, IOException {

		boolean result = true;
		JSONObject responseObject = new JSONObject();

		// Read the request parameters
		Map<String, String> params = QuarterlyBDOUtils.readFormParamStrings(request);

		log.info("Processing [UpdateBDOConfigurationServlet] with request params {}", params);

		if (result && params.size() <= 0) {
			log.error("Invalid Input Params {}", params);
			result = false;
		}

		try {
			Session session = getSession(request);

			if(userManagementService.isAnonymous(session)) { 
				result = false;
				responseObject.put(QuartelyBDOConstants.ERROR, QuartelyBDOConstants.INVALID_SESSION);
				log.error("Invalid session >> Terminating.");
			}

			if (result) {
				result = quarterlyBDORepositoryClient.createOrUpdateGDCBDOConfiguration(params);				
			}

			responseObject.put(QuartelyBDOConstants.SUCCESS, result);
		} catch (JSONException e) {
			log.error("[JSONException]", e);
		} catch (Exception e) {
			log.error("[Exception]", e);
		}

		response.setContentType(QuartelyBDOConstants.RESPONSE_TYPE);
		response.setCharacterEncoding(QuartelyBDOConstants.CHARACTER_ENCODING);
		PrintWriter writer = response.getWriter();
		writer.write(responseObject.toString());		
	}

	private Session getSession(SlingHttpServletRequest request) {
		return request.getResourceResolver().adaptTo(Session.class);
	}

}