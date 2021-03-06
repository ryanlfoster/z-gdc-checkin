<%@ page contentType="text/html; charset=utf-8" %>
<%@include file="/libs/foundation/global.jsp" %>
<%@page session="false" %>
<%@page import="com.adobe.gdc.checkin.QuarterlyBDOCalendarService,
                com.adobe.gdc.checkin.UserManagementService,
                java.util.Calendar,
                java.util.Map, 
                javax.jcr.Session" %>

<c:set var="previous" value="previous" />
<c:set var="current" value="current" />

<%
QuarterlyBDOCalendarService quarterlyBDOService = sling.getService(QuarterlyBDOCalendarService.class);
UserManagementService userManagementService = sling.getService(UserManagementService.class);
int currentFiscalYear = quarterlyBDOService.getcurrentQuarterAnnualYear();
Map<String, Calendar> allQuartersDateRangeMap = quarterlyBDOService.getAllQuartersDateRangeMap();
Session session = resourceResolver.adaptTo(Session.class);
String userID = userManagementService.getCurrentUser(session);
int employeeBeginYear = quarterlyBDOService.getEmployeeProfileBeginYear(userID);

int selectedYear = currentFiscalYear;

String selectedYearString = request.getParameter("selectedYear");
   if(selectedYearString != null){
 selectedYear = Integer.parseInt(selectedYearString);
   }
%>
<script>
    $(document).ready(function() {
  $('#selectedYear-self').change(function(){
            window.location.href = "/gdc-bdo-home.html?selectedYear=" + $('#selectedYear-self').val();
        });
    });
</script>

<c:set var="userID" value="<%=userID%>" scope="request" />
<c:set var="annualYear" value="<%=selectedYear%>" scope="request" />
<c:set var="fiscalYear" value="<%=currentFiscalYear%>" scope="request" />

<div class="row">
 <div class="col-md-12 col-xs-12">
        <div class="panel panel-default">
            <div class="panel-heading">
                <h3 class="panel-title">Set & Review your Quaterly BDO
                    <span>
                        <select name="selectedYear-self" id="selectedYear-self">
                            <% for(int i = employeeBeginYear; i <= currentFiscalYear; i++) { %>
                            <option value="<%= i%>" <% if(selectedYear == i) {%> selected="selected" <% } %>> <%= i%> </option>
                            <% } %>
                        </select>
                    </span>
                </h3>
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
                                <c:when test="${annualYear eq fiscalYear}">
                                    <c:choose>
                                        <c:when test="${quarterStatus eq previous}"> 
                                            <li class="done">
                                                <a class="step" href="#bdo_quarter${i}" data-toggle="tab">Q<sub>${i}</sub></a>
                                            </li>
                                        </c:when>
        
                                        <c:when test="${quarterStatus eq current}"> 
                                            <li class="done active">
                                                <a class="step" href="#bdo_quarter${i}" data-toggle="tab">Q<sub>${i}</sub></a>
                                            </li>
                                        </c:when>

                                        <c:otherwise> 
                                            <li class="">
                                                <a class="step" >Q<sub>${i}</sub></a>
                                            </li>
                                        </c:otherwise>
                                    </c:choose>
                                </c:when>
                                <c:otherwise>
                                    <c:choose>
                                        <c:when test="${i eq '1'}"> 
                                            <li class="done active">
                                                <a class="step" href="#bdo_quarter${i}" data-toggle="tab">Q<sub>${i}</sub></a>
                                            </li>
                                        </c:when>

                                        <c:otherwise> 
                                            <li class="done">
                                                <a class="step" href="#bdo_quarter${i}" data-toggle="tab">Q<sub>${i}</sub></a>
                                            </li>
                                        </c:otherwise>
                                    </c:choose>
                                </c:otherwise>
                            </c:choose>
                          </c:forEach>
                    </ul>
                </div>
            </div>
        </div>
    </div>
</div>


<div class="tab-content bdo-self-content">
    <c:forEach var="i" begin="1" end="4">
       <% 
        Calendar quarterStartCalendarDate = allQuartersDateRangeMap.get("startDateQuarter"+pageContext.getAttribute("i"));
        Calendar quarterEndCalendarDate = allQuartersDateRangeMap.get("endDateQuarter"+pageContext.getAttribute("i"));
       %>

        <c:set var="quarterStatus" value="<%=quarterlyBDOService.getQuarterStatus(quarterStartCalendarDate, quarterEndCalendarDate)%>" />
        <c:set var="quarterOpenToEdit" value="<%=quarterlyBDOService.isOpenToEdit(quarterStartCalendarDate, quarterEndCalendarDate)%>" />

        <c:choose>
         <c:when test="${annualYear eq fiscalYear}">
                <c:choose>
                    <c:when test="${quarterStatus eq current}"> 
                        <div id="bdo_quarter${i}" class="tab-pane active">  
                            <c:set var="quarterNumber" value="${i}" scope="request"/>
                            <c:set var="editForm" value="true" scope="request"/>
                            <cq:include path="bdo-goals-achievement-form" resourceType= "gdc-checkin/components/content/bdo-goals-achievement-form" />
                        </div>
                    </c:when>
        
                    <c:otherwise>
                        <div id="bdo_quarter${i}" class="tab-pane"> 
                            <c:set var="quarterNumber" value="${i}" scope="request"/>
                            
                            <c:choose>
                              <c:when test="${quarterOpenToEdit eq true}" >
                                <c:set var="editForm" value="true" scope="request"/>
                              </c:when>
                              <c:otherwise>
                               <c:set var="editForm" value="false" scope="request"/>
                              </c:otherwise>
                            </c:choose>
        
                            <cq:include path="bdo-goals-achievement-form" resourceType= "gdc-checkin/components/content/bdo-goals-achievement-form" />
                        </div>
                    </c:otherwise>
                </c:choose>
            </c:when>
            <c:otherwise>
                <c:choose>
                    <c:when test="${i eq '1'}"> 
                        <div id="bdo_quarter${i}" class="tab-pane active">  
                            <c:set var="quarterNumber" value="${i}" scope="request"/>
                            <c:set var="editForm" value="false" scope="request"/>
                            <cq:include path="bdo-goals-achievement-form" resourceType= "gdc-checkin/components/content/bdo-goals-achievement-form" />
                        </div>
                    </c:when>

                    <c:otherwise>
                        <div id="bdo_quarter${i}" class="tab-pane"> 
                            <c:set var="quarterNumber" value="${i}" scope="request"/>
                            <c:set var="editForm" value="false" scope="request"/>
                            <cq:include path="bdo-goals-achievement-form" resourceType= "gdc-checkin/components/content/bdo-goals-achievement-form" />
                        </div>
                    </c:otherwise>
                </c:choose>
            </c:otherwise>
        </c:choose>
    </c:forEach>
</div>


