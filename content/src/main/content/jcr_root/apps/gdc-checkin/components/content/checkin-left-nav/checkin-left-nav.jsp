<%@ page contentType="text/html; charset=utf-8" %>
<%@include file="/libs/foundation/global.jsp" %>
<%@page session="false" %>

<c:set var="isLoggedIn" value="true" />

<div id="page-wrap">

     <div id="main-sidebar">
         <!-- sidebar user avatar -->
         <cq:include path="nav-login" resourceType= "gdc-checkin/components/login/nav-login" />
		 <!--  sidebar user avatar -->

         <c:if test = "${isLoggedIn eq true}" >
			<div class="accordion" id="sbAccordion">
				<!-- accordion content -->
				<ul class="nav nav-tabs nav-stacked" id="checkin_tab">
					<li  class="active">
						<div class="accordion-group">
							<div class="accordion-heading">
								<a class="btn btn-success btn-block" href="#checkin_tab1" data-toggle="tab"><i class="icon-tasks"></i> QUARTERLY BDO</a>
							</div>
						</div><!-- accordion content -->
					</li>
					<li>
						<div class="accordion-group">
							<div class="accordion-heading">
								<a class="btn btn-primary btn-block" href="#checkin_tab2" data-toggle="tab"><i class="icon-tasks"></i> FEEDBACK</a>
							</div>
						</div>
					</li> 
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
								<div id="checkin_tab1" class="tab-pane active"> 
                                    <cq:include path="quarterly-bdo-tracker" resourceType= "gdc-checkin/components/content/quarterly-bdo-tracker" />
								</div>

								<div id="checkin_tab2" class="tab-pane"> 
									This space is under construction
								</div>
							</div>
                        </c:when>
						    <c:otherwise>
								<cq:include path="checkin-left-nav" resourceType="gdc-checkin/components/content/fallback-text-image" />
							</c:otherwise>
					</c:choose>
				</div>
			</div>
        </div>
    </div>
	
 </div>
