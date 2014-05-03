<%@ page contentType="text/html; charset=utf-8" %>
<%@include file="/libs/foundation/global.jsp" %>
<%@page session="false" %>
<%@page import="com.adobe.gdc.checkin.QuarterlyBDOCalendarService,com.adobe.gdc.checkin.UserManagementService,java.util.Calendar, java.util.Map, javax.jcr.Session" %>

<c:set var="previous" value="previous" />
<c:set var="current" value="current" />

<% QuarterlyBDOCalendarService quarterlyBDOService = sling.getService(QuarterlyBDOCalendarService.class);
   UserManagementService userManagementService = sling.getService(UserManagementService.class);
   Map<String, Calendar> allQuartersDateRangeMap = quarterlyBDOService.getAllQuartersDateRangeMap();
   Session session = resourceResolver.adaptTo(Session.class);
   String managersID = userManagementService.getCurrentUser(session);
%>
<c:set var="annualYear" value="<%=quarterlyBDOService.getcurrentQuarterAnnualYear()%>" scope="request" />
<c:set var="managersID" value="<%=managersID%>" scope="request" />

<div class="row">
	<div class="col-md-9 col-xs-9">
        <div class="panel panel-default">
            <div class="panel-heading">
                <h3 class="panel-title">Quarterly BDO Report of your Direct Reportees</h3>
            </div>
            <div class="panel-body">
                <div class="quarter-steps">
                    <ul class="wizard-steps">
                        <c:forEach var="i" begin="1" end="4">

							<% 
                            Calendar quarterStartCalendarDate = allQuartersDateRangeMap.get("startDateQuarter"+pageContext.getAttribute("i"));
                       	    Calendar quarterEndCalendarDate = allQuartersDateRangeMap.get("endDateQuarter"+pageContext.getAttribute("i"));
    						%>

                            <c:set var="quarterStatus" value="<%=quarterlyBDOService.getQuarterStatus(quarterStartCalendarDate, quarterEndCalendarDate)%>" />

                            <c:choose>
                                <c:when test="${quarterStatus eq previous}"> 
                                    <li class="done">
                                        <a class="step" href="#bdo_reports_quarter${i}" data-toggle="tab">Q<sub>${i}</sub></a>
                                    </li>
                                </c:when>

                                <c:when test="${quarterStatus eq current}"> 
                                    <li class="done active">
                                        <a class="step" href="#bdo_reports_quarter${i}" data-toggle="tab">Q<sub>${i}</sub></a>
                                    </li>
                                </c:when>

                                <c:otherwise> 
                                    <li class="">
                                        <a class="step" >Q<sub>${i}</sub></a>
                                    </li>
                                </c:otherwise>
                            </c:choose>

						</c:forEach>
                    </ul>
                </div>
            </div>
        </div>
    </div>
</div>

<div class="row">
	<div class="col-md-9 col-xs-9">
		<div class="tab-content bdo-direct-reports">
            <c:forEach var="i" begin="1" end="4">
        
               <% 
                Calendar quarterStartCalendarDate = allQuartersDateRangeMap.get("startDateQuarter"+pageContext.getAttribute("i"));
                Calendar quarterEndCalendarDate = allQuartersDateRangeMap.get("endDateQuarter"+pageContext.getAttribute("i"));
                %>
        
                <c:set var="quarterStatus" value="<%=quarterlyBDOService.getQuarterStatus(quarterStartCalendarDate, quarterEndCalendarDate)%>" />
        
                <c:choose>
                    <c:when test="${quarterStatus eq current}"> 
                        <div id="bdo_reports_quarter${i}" class="tab-pane active">  
                            <c:set var="quarterNumber" value="${i}" scope="request"/>
                            <c:set var="currentQuarter" value="true" scope="request"/>
                            <cq:include path="quarterly-bdo-direct-reports" resourceType= "gdc-checkin/components/content/quarterly-bdo-direct-reports" />
                        </div>
                    </c:when>
        
                    <c:otherwise>
                        <div id="bdo_reports_quarter${i}" class="tab-pane"> 
                            <c:set var="quarterNumber" value="${i}" scope="request"/>
                            <c:set var="currentQuarter" value="false" scope="request"/>
                            <cq:include path="quarterly-bdo-direct-reports" resourceType= "gdc-checkin/components/content/quarterly-bdo-direct-reports" />
                        </div>
                    </c:otherwise>
                </c:choose>
            </c:forEach>
        </div>
	</div>
	<div class="col-md-3 col-xs-3">
        <!--  <cq:include path="bdo-achievement-tracker" resourceType= "gdc-checkin/components/content/bdo-achievement-tracker" /> -->
    </div>
</div>