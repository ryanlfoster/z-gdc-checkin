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
import com.adobe.gdc.checkin.UserManagementService;
import com.adobe.gdc.checkin.constants.QuartelyBDOConstants;
import com.adobe.gdc.checkin.utils.QuarterlyBDOUtils;

@Component(label = "GDC Check-in Quarterly BDO Form Save or Submit Servlet", description = "GDC Check-in Quarterly BDO Form Save or Submit Servlet")
@Service(Servlet.class)
@Properties({
		@Property(name = "sling.servlet.resourceTypes", value = { "cq/Page" }),
		@Property(name = "sling.servlet.methods", value = { "POST" }),
		@Property(name = "sling.servlet.selectors", value = { "bdo", "save", "submit" }) })
public class QuarterlyBDOSaveOrSubmitServlet extends SlingAllMethodsServlet {

	private static final long serialVersionUID = 1L;

	private static final Logger log = LoggerFactory.getLogger(QuarterlyBDOSaveOrSubmitServlet.class);

	@Reference
	private QuarterlyBDORepositoryClient quarterlyBDORepositoryClient;

	@Reference
	UserManagementService userManagementService;

	@Override
	protected void doPost(SlingHttpServletRequest request,
			SlingHttpServletResponse response) throws ServletException,
			IOException {

		boolean result = true;
		JSONObject responseObject = new JSONObject();

		String[] selectors = request.getRequestPathInfo().getSelectors();
		String action = QuartelyBDOConstants.EMPTY_STRING;
		
		if (selectors != null && selectors.length > 0) {
			action = selectors[1];
		}

		// Read the request parameters
		Map<String, String[]> params = QuarterlyBDOUtils.readFormParams(request);

		log.info("Processing [QuarterlyBDOSaveOrSubmitServlet] for action - {} with request params {}",action, params);

		if (StringUtils.isBlank(action) && !action.equals(QuartelyBDOConstants.SAVE) && !action.equals(QuartelyBDOConstants.SUBMIT)) {
			log.error("Invalid action : {}", action);
			result = false;
		}

		if (result && params.size() <= 0) {
			log.error("Invalid Input Params {}", params);
			result = false;
		}
		try {
			Session session = getSession(request);
			if (result) {
				result = quarterlyBDORepositoryClient.createOrUpdateQuarterlyBDOData(action, params, session);
			}

			if (result && action.equals(QuartelyBDOConstants.SUBMIT)) {
				//If the BDO form is self submitted- send notification to the manager
				if( (userManagementService.getCurrentUser(session)).equals(params.get(QuartelyBDOConstants.USER_ID)[0])) {
					// TO_DO send email to manager
				}
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

	@Override
	protected void doGet(SlingHttpServletRequest request,
			SlingHttpServletResponse response) throws ServletException,
			IOException {
		log.info("QuarterlyBDOSaveOrSubmit Servlet");
		doPost(request, response);

	}

	private Session getSession(SlingHttpServletRequest request) {
		return request.getResourceResolver().adaptTo(Session.class);
	}
}
