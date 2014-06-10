package com.adobe.gdc.checkin.servlet;

import com.adobe.gdc.checkin.QuarterlyBDORepositoryClient;
import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.SearchResult;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.sling.SlingServlet;
import org.apache.jackrabbit.api.security.user.UserManager;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.*;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.apache.sling.jcr.api.SlingRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Node;
import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.servlet.ServletException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by prajesh on 6/10/2014.
 */
@SlingServlet(paths = "/bin/gdcbdo/updateservlet", methods = "{GET}", extensions = "{json}")
public class StoreNodePropertyUpdateServlet extends SlingSafeMethodsServlet {

    private Logger log = LoggerFactory.getLogger(StoreNodePropertyUpdateServlet.class);

    @Reference
    private SlingRepository repository;

    @Reference
    private ResourceResolverFactory resourceResolverFactory;


    @Reference
    private QuarterlyBDORepositoryClient quarterlyBDORepositoryClient;


    @Reference
    private QueryBuilder queryBuilder;

    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException, IOException {


        try {
            Session adminSession = repository.loginAdministrative(null);


            HashMap<String,String> predicateMap = new HashMap<String, String>();

            predicateMap.put("path", "/home/users");
            predicateMap.put("type","rep:User");


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


    }
}
