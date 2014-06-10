package com.adobe.gdc.checkin.servlet;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.servlet.ServletException;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.sling.SlingServlet;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.apache.sling.jcr.api.SlingRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.adobe.gdc.checkin.QuarterlyBDORepositoryClient;
import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.SearchResult;

/**
 * Created by prajesh on 6/10/2014.
 */
@SlingServlet(paths = "/bin/gdcbdo/updateservlet", methods = "{GET}", extensions = "{json}")
public class StoreNodePropertyUpdateServlet extends SlingSafeMethodsServlet {

    private Logger log = LoggerFactory.getLogger(StoreNodePropertyUpdateServlet.class);

    @Reference
    private SlingRepository repository;

    @Reference
    private QuarterlyBDORepositoryClient quarterlyBDORepositoryClient;


    @Reference
    private QueryBuilder queryBuilder;

    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException, IOException {
    	
    	Session adminSession = null;

        try {
            adminSession = repository.loginAdministrative(null);


            HashMap<String,String> predicateMap = new HashMap<String, String>();

            predicateMap.put("path", "/home/users");
            predicateMap.put("type","rep:User");
            predicateMap.put("p.limit","-1");


            Query query = queryBuilder.createQuery(PredicateGroup.create(predicateMap),adminSession);

            SearchResult searchResults = query.getResult();

            Iterator<Node> userNodeIterator = searchResults.getNodes();

            while(userNodeIterator.hasNext())
            {
                Node userNode = userNodeIterator.next();

                String userLDAPID= userNode.getName();

                quarterlyBDORepositoryClient.createOrUpdateEmployeeProfileOnLogin(userLDAPID);

                log.info("Store Node udpated for user = "+userLDAPID);


            }

        } catch (RepositoryException e) {
            e.printStackTrace();
        }

        finally {
        	if(adminSession != null && adminSession.isLive()) {
				adminSession.logout();
			}
        }

    }
}
