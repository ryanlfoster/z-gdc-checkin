<%@ page contentType="text/html; charset=utf-8" %>
<%@include file="/libs/foundation/global.jsp" %>
<%@page session="false" %>

<c:set var="isLoggedIn" value="true" />
<c:set var="isManager" value="true" />

<div id="page-wrap">

     <div id="main-sidebar">
         <!-- sidebar user avatar -->
         <cq:include path="nav-login" resourceType= "gdc-checkin/components/login/nav-login" />
		 <!--  sidebar user avatar -->

         <c:if test = "${isLoggedIn eq true}">
			<div class="checkin_tabs">
				<ul class="nav nav-tabs nav-stacked" id="checkin_tab">
                    <c:choose>
                        <c:when test = "${isManager eq true}">
                            <li class="active">
                                <a class="btn btn-success btn-block tab_title" href="#checkin_tab1" data-toggle="tab"><i class="icon-tasks"></i> BDO REPORT</a>
                            </li> 
                            <li>
								<a class="btn btn-primary btn-block tab_title" href="#checkin_tab2" data-toggle="tab"><i class="icon-tasks"></i> MY QUARTERLY BDO</a>
							</li>
                        </c:when>
                        <c:otherwise>
							<li class="active">
								<div class="accordion-group">
									<div class="accordion-heading">
										<a class="btn btn-success btn-block tab_title" href="#checkin_tab1" data-toggle="tab"><i class="icon-tasks"></i> MY QUARTERLY BDO</a>
									</div>
								</div>
							</li>
                        </c:otherwise>
                    </c:choose>
				</ul>
			</div>   
         </c:if>
     </div>

    <div id="main-content">
        <div id="inner">
			<div id="container-fluid">		
				<div class="tabbable main-tabs">			
					<c:choose>
						<c:when test = "${isLoggedIn eq true}" >
                            <div class="tab-content">
                                <c:choose>
                                    <c:when test = "${isManager eq true}">
                                        <div id="checkin_tab1" class="tab-pane active"> 
                                            <cq:include path="quarterly-bdo-direct-reports" resourceType= "gdc-checkin/components/content/bdo-direct-reports-container" />
                                        </div>
                                        <div id="checkin_tab2" class="tab-pane"> 
                                    		<cq:include path="quarterly-bdo-self" resourceType= "gdc-checkin/components/content/quarterly-bdo-self" />
										</div>
                                    </c:when>
                                    <c:otherwise>
										<div id="checkin_tab1" class="tab-pane active"> 
                                    		<cq:include path="quarterly-bdo-self" resourceType= "gdc-checkin/components/content/quarterly-bdo-self" />
										</div>
                                    </c:otherwise>
                                </c:choose>
							</div>
                        </c:when>
                        <c:otherwise>
                            <cq:include path="fallback-text-image" resourceType="gdc-checkin/components/content/fallback-text-image" />
                        </c:otherwise>
					</c:choose>
				</div>
			</div>
        </div>
    </div>
	
 </div>
