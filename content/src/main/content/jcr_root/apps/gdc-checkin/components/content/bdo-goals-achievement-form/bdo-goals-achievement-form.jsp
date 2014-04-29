<%@ page contentType="text/html; charset=utf-8" %>
<%@include file="/libs/foundation/global.jsp" %>
<%@page session="false" %>
<%@page import="org.apache.commons.lang.StringUtils,com.adobe.gdc.checkin.QuarterlyBDORepositoryClient,com.adobe.gdc.checkin.UserManagementService,java.util.Map,javax.jcr.Session" %>

<c:set var="percentageList" value="${fn:split('10,20,30,40,50,60,70,80,90,100', ',')}" scope="application" />

<c:choose>
	<c:when test="<%=StringUtils.isNotBlank(request.getParameter("quarterNumber"))%>">
		<c:set var="quarterNumber" value="<%=Integer.parseInt(request.getParameter("quarterNumber"))%>" />
	</c:when>
	<c:otherwise>
		<c:set var="quarterNumber" value="${quarterNumber}" />
	</c:otherwise>
</c:choose>

<c:choose>
	<c:when test="<%=StringUtils.isNotBlank(request.getParameter("annualYear"))%>">
		<c:set var="annualYear" value="<%=Integer.parseInt(request.getParameter("annualYear"))%>" />
	</c:when>
	<c:otherwise>
		<c:set var="annualYear" value="${annualYear}" />
	</c:otherwise>
</c:choose>

<c:choose>
	<c:when test="<%=StringUtils.isNotBlank(request.getParameter("userID"))%>">
		<c:set var="userID" value="<%=request.getParameter("userID")%>" />
	</c:when>
	<c:otherwise>
		<c:set var="userID" value="${userID}" />
	</c:otherwise>
</c:choose>

<c:choose>
	<c:when test="<%=StringUtils.isNotBlank(request.getParameter("editForm"))%>">
		<c:set var="editForm" value="<%=request.getParameter("editForm")%>" />
	</c:when>
	<c:otherwise>
		<c:set var="editForm" value="${editForm}" />
	</c:otherwise>
</c:choose>

<%
QuarterlyBDORepositoryClient quarterlyBDORepositoryClient = sling.getService(QuarterlyBDORepositoryClient.class);
UserManagementService userManagementService = sling.getService(UserManagementService.class);

int quarterNumber = (Integer)pageContext.getAttribute("quarterNumber");
int annualYear = (Integer)pageContext.getAttribute("annualYear");

String userID = (String)pageContext.getAttribute("userID");
Session session = resourceResolver.adaptTo(Session.class);
Map<String, String[]> quarterlyBDODataMap = quarterlyBDORepositoryClient.getQuarterlyBDOData(quarterNumber,annualYear,userID,session);
%>

<c:set var="bdoObjectives" value="<%=quarterlyBDODataMap.get("objectives")%>" scope="request"/>
<c:set var="bdoAchievements" value="<%=quarterlyBDODataMap.get("achievements")%>" scope="request"/>
<c:set var="bdoScore" value="<%=quarterlyBDODataMap.get("bdoScore") != null ? quarterlyBDODataMap.get("bdoScore")[0] : ""%>" scope="request"/>
<c:set var="status" value="<%=quarterlyBDODataMap.get("status") != null ? quarterlyBDODataMap.get("status")[0] : "NOT SUBMITTED"%>" scope="request"/>
<c:set var="name" value="<%=userManagementService.getEmployeeName(userID, session)%>" scope="request"/>
<c:set var="designation" value="<%=quarterlyBDODataMap.get("designation")!= null ? quarterlyBDODataMap.get("designation")[0] : userManagementService.getEmployeeDesignation(userID, session)%>" scope="request"/>

<div class="row">

    <div class="col-md-9 col-xs-9">
        <div class="bdo-form">
           
            <c:choose>
                <c:when test = "${editForm eq 'true'}">

					<div class="row">
		                <div class="col-md-4 col-xs-4"></div>               
		                <div class="col-md-7 col-xs-7 align-right">
		                    <div class="col-sm-10  col-xs-10 col-md-10">
		                        ${name},<br/>
		                        ${designation}<br/><br/>
		                    </div>
		                    <div class="col-sm-2  col-xs-2 col-md-2  align-left"></div>
		                </div>
		                <div class="col-md-1 col-xs-1"></div>
            		</div>

                    <div class="row">
                        <div class="col-md-1 col-xs-1"></div>
                        <div class="col-md-8 col-xs-8" id="form-message"></div>
                    </div>
                    <br/>

                    <form id="quarterly-bdo-form" class="quarterly-bdo-form" method="POST" action="<%=currentPage.getPath()%>.bdo">

                        <input type="hidden" name="designation" id="designation" value="${designation}" />
                        <input type="hidden" name="quarterNumber" id="quarterNumber" value="${quarterNumber}" />
                        <input type="hidden" name="userID" id="userID" value="${userID}" />
                        <input type="hidden" name="annualYear" id="annualYear" value="${annualYear}" />
                        <input type="hidden" name="name" id="name" value="${name}" />

                        <div class="row">
                            <div class="col-md-1 col-xs-1"></div>
                            <div class="col-md-2 col-xs-2">
                                <label for="objective">
                                    Set BDO Objectives
                                </label>
                            </div>
                            <div class="col-md-8 col-xs-8  bdo-objective-panel">
                                <div class="row bdo-objective-active bdo-objective-panel-row">
                                    <div class="col-sm-10  col-xs-10 col-md-10">
                                        <textarea id="objective_1" name="objective" placeholder="Objective" class="form-control objective" rows="1" cols="50"></textarea>
                                    </div>
                                    <div class="col-sm-2  col-xs-2 col-md-2 align-left button-wrapper">
                                        <button class="btn btn-danger btn-remove" type="button" style="display:none">
                                            <span class="glyphicon glyphicon-remove"></span>
                                        </button>
                                        <c:choose>
											<c:when test="<%=StringUtils.isNotBlank(request.getParameter("editBdoScore"))%>">
												<button class="btn btn-success btn-add" type="button">
                                            		<span class="glyphicon glyphicon-plus"></span>
                                        		</button>
                                        	</c:when>
                                        	<c:otherwise>
	                                        	<button class="btn btn-primary btn-add" type="button">
	                                            <span class="glyphicon glyphicon-plus"></span>
	                                        	</button>
                                        	</c:otherwise>
                                        </c:choose>
                                    </div>
                                </div>
                            </div>
                            <div class="col-md-1 col-xs-1"></div>
                        </div>
        
                        <br/>
        
                        <div class="row">
                            <div class="col-md-1 col-xs-1"></div>
                            <div class="col-md-2 col-xs-2">
                                <label for="achievement">
                                    BDO Achievements inputs
                                </label>
                            </div>
                            <div class="col-md-8 col-xs-8 bdo-achievement-panel">
                                <div class="row bdo-achievement-active bdo-achievement-panel-row">
                                    <div class="col-sm-10  col-xs-10 col-md-10">
                                        <textarea id="achievement_1" name="achievement" placeholder="Achievement" class="form-control achievement" rows="1" cols="50"></textarea>
                                    </div>
                                    <div class="col-sm-2  col-xs-2 col-md-2  align-left button-wrapper">
                                        <button class="btn btn-danger btn-remove" type="button" style="display:none">
                                            <span class="glyphicon glyphicon-remove"></span>
                                        </button>
                                        <c:choose>
											<c:when test="<%=StringUtils.isNotBlank(request.getParameter("editBdoScore"))%>">
												<button class="btn btn-success btn-add" type="button">
                                            		<span class="glyphicon glyphicon-plus"></span>
                                        		</button>
                                        	</c:when>
                                        	<c:otherwise>
	                                        	<button class="btn btn-primary btn-add" type="button">
	                                            <span class="glyphicon glyphicon-plus"></span>
	                                        	</button>
                                        	</c:otherwise>
                                        </c:choose>
                                    </div>
                                </div>  
                            </div>
                            <div class="col-md-1 col-xs-1"></div>
                        </div>
    
                        <br/>

						<c:choose>
							<c:when test="<%=StringUtils.isNotBlank(request.getParameter("editBdoScore"))%>">

								<div class="row">
									<div class="col-md-1 col-xs-1"></div>
									<div class="col-md-2 col-xs-2">
										<label for="percentageAchieved"> BDO Score </label>
									</div>
									<div class="col-md-8 col-xs-8">
										<select name="bdoScore" id="bdoScore">
											<option value=""selected}>Please select</option>
											<c:forEach items="${percentageList}" var="percent">
												<option value="${percent}" ${percent eq bdoScore ? 'selected' : ''}>${percent}</option>
											</c:forEach>
										</select> %
									</div>
									<div class="col-md-1 col-xs-1"></div>
								</div>

								<br/>

								<div class="row">
									<div class="col-md-4 col-xs-4"></div>
									<div class="col-md-7 col-xs-7 align-right">
										<div class="col-sm-10  col-xs-10 col-md-10">
											<button type="button" class="btn btn-success btn-complete">Complete</button>
											&nbsp;
										</div>
										<div class="col-sm-2  col-xs-2 col-md-2  align-left"></div>
									</div>
									<div class="col-md-1 col-xs-1"></div>
								</div>

								<br/>
							</c:when>
							<c:otherwise>
								<div class="row">
									<div class="col-md-4 col-xs-4"></div>
									<div class="col-md-7 col-xs-7 align-right">
										<div class="col-sm-10  col-xs-10 col-md-10">
											<button type="button" class="btn btn-primary btn-save">Save</button>
											&nbsp;
											<button type="button" class="btn btn-primary btn-submit">Submit</button>
										</div>
										<div class="col-sm-2  col-xs-2 col-md-2  align-left"></div>
									</div>
									<div class="col-md-1 col-xs-1"></div>
								</div>

								<br />
							</c:otherwise>
						</c:choose>
						
						<div class="row">
                            <div class="col-md-1 col-xs-1"></div>
                            <div class="col-md-6 col-xs-6">
                                <div class="status-msg">status : <span class="status">${status}</span></div>
                            </div>
                        </div>

                    </form>

                </c:when>
            
                <c:otherwise>
                	<cq:include path="quarterly-bdo-view" resourceType= "gdc-checkin/components/content/quarterly-bdo-view" />
                </c:otherwise>

            </c:choose>
        </div>

    </div>
    
</div>


<script>

	var bdoObjectivesArray = [];
	var bdoAchievementsArray = [];
	var bdoScore = '';
	var status = '';
    
	$(document).ready(function() {
	
		if(${editForm} == true) {
			
		    bdoObjectivesArray = [];
		    bdoAchievementsArray = [];
		    bdoScore = '${bdoScore}';
		    status = '${status}';
            
			<c:forEach items="${bdoObjectives}" var="objective">
	  			 	bdoObjectivesArray.push('${fn:escapeXml(objective)}'); 
			</c:forEach>
	
			<c:forEach items="${bdoAchievements}" var="achievement">
	  			 	bdoAchievementsArray.push('${fn:escapeXml(achievement)}'); 
			</c:forEach>
	
			GDC.bdo.form(bdoObjectivesArray,bdoAchievementsArray);
	
	
			$(".bdo-form").on("click", ".btn-save",function() {

	           var changeInFormValues = GDC.bdo.form.detectAnyFormChange(bdoObjectivesArray,bdoAchievementsArray);
	
	           if(changeInFormValues) {
	               var validateForm = GDC.bdo.form.validateOnSave();
	               var buttonLabel = $(this).html();
	
	               if(validateForm == true) {
	                   GDC.bdo.form.disableForm(this);
	                   GDC.bdo.form.saveSubmitOrCompleteBDO("save");
	               }
	               GDC.bdo.form.enableForm(this, buttonLabel);
	           } 
	           else {
	               GDC.bdo.form.notifyError("Nothing to save");
	           }
	
	   		});  
	
	        $(".bdo-form").on("click", ".btn-submit",function() {
	
	            var changeInFormValues = GDC.bdo.form.detectAnyFormChange(bdoObjectivesArray,bdoAchievementsArray);
	
	            if(changeInFormValues || (!changeInFormValues && status == 'NOT SUBMITTED')) {
	                var validateForm = GDC.bdo.form.validateOnSubmit();
	                var buttonLabel = $(this).html();
	                
	                if(validateForm == true) {
	                    GDC.bdo.form.disableForm(this);
	                    GDC.bdo.form.saveSubmitOrCompleteBDO("submit");
	                }
	                GDC.bdo.form.enableForm(this, buttonLabel);
	            }  
	            else {
	                    GDC.bdo.form.notifyError("Already Submitted!");
	            }
	        });  
	        
	        $(".bdo-form").on("click", ".btn-complete",function() {
	        	
	            var changeInFormValues = GDC.bdo.form.detectAnyFormChangeOnComplete(bdoObjectivesArray,bdoAchievementsArray,bdoScore);
	
	            if(changeInFormValues) {
	                var validateForm = GDC.bdo.form.validateOnComplete();
	                var buttonLabel = $(this).html();
	                
	                if(validateForm == true) {
	                    GDC.bdo.form.disableForm(this);
	                    GDC.bdo.form.saveSubmitOrCompleteBDO("complete");
	                }
	                GDC.bdo.form.enableForm(this, buttonLabel);
	            }  
	            else {
	                    GDC.bdo.form.notifyError("Please update the form before COMPLETING your action !");
	            }
	        });  
	
		}
	
	});
</script>