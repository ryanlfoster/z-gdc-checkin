<%@ page contentType="text/html; charset=utf-8" %>
<%@include file="/libs/foundation/global.jsp" %>
<%@page session="false" %>

<c:set var="previous" value="previous" />
<c:set var="current" value="current" />

<c:set var="startDateQuarter1" value="<%=properties.get("startDateQuarter1", "01-12-2013")%>" />
<c:set var="endDateQuarter1" value="<%=properties.get("endDateQuarter1", "28-02-2014")%>" />

<c:set var="startDateQuarter2" value="<%=properties.get("startDateQuarter2", "01-03-2014")%>" />
<c:set var="endDateQuarter2" value="<%=properties.get("endDateQuarter2", "31-05-2014")%>" />

<c:set var="startDateQuarter3" value="<%=properties.get("startDateQuarter3", "01-06-2014")%>" />
<c:set var="endDateQuarter3" value="<%=properties.get("endDateQuarter3", "31-08-2014")%>" />

<c:set var="startDateQuarter4" value="<%=properties.get("startDateQuarter4", "01-09-2014")%>" />
<c:set var="endDateQuarter4" value="<%=properties.get("endDateQuarter4", "30-11-2014")%>" />


<div class="row">
	<div class="col-md-9 col-xs-9">
        <div class="panel panel-default">
            <div class="panel-heading">
                <h3 class="panel-title">Quaterly BDO</h3>
            </div>
            <div class="panel-body">
                <div class="quarter-steps">
                    <ul class="wizard-steps">
                        <c:forEach var="i" begin="1" end="4">

							<c:choose>
								<c:when test="${i eq 1}"> 
                                    <c:set var="quarterStartDate" value="${startDateQuarter1}" />
                                    <c:set var="quarterEndDate" value="${endDateQuarter1}" />
								</c:when>
                                <c:when test="${i eq 2}"> 
                                    <c:set var="quarterStartDate" value="${startDateQuarter2}" />
                                    <c:set var="quarterEndDate" value="${endDateQuarter2}" />
								</c:when>
                                <c:when test="${i eq 3}"> 
                                    <c:set var="quarterStartDate" value="${startDateQuarter3}" />
                                    <c:set var="quarterEndDate" value="${endDateQuarter3}" />
								</c:when>
                                <c:otherwise> 
                                    <c:set var="quarterStartDate" value="${startDateQuarter4}" />
                                    <c:set var="quarterEndDate" value="${endDateQuarter4}" />
								</c:otherwise>
                            </c:choose>

							<c:set var="quarterStatus" value="previous" />

                            <c:choose>
                                <c:when test="${quarterStatus eq previous}"> 
                                    <li class="done">
                                        <a class="step" href="#bdo_quarter${i}" data-toggle="tab">${i}</a>
                                    </li>
                                </c:when>
                                
                                <c:when test="${quarterStatus eq current}"> 
                                    <li class="done active">
                                        <a class="step" href="#bdo_quarter${i}" data-toggle="tab">${i}</a>
                                    </li>
                                </c:when>
                                
                                <c:otherwise> 
                                    <li class="">
                                        <a class="step" >${i}</a>
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



<div class="tab-content">
    <c:forEach var="i" begin="1" end="4">
        <c:choose>
            <c:when test="${i eq 1}"> 
                <c:set var="quarterStartDate" value="${startDateQuarter1}" />
                <c:set var="quarterEndDate" value="${endDateQuarter1}" />
            </c:when>
            <c:when test="${i eq 2}"> 
                <c:set var="quarterStartDate" value="${startDateQuarter2}" />
                <c:set var="quarterEndDate" value="${endDateQuarter2}" />
            </c:when>
            <c:when test="${i eq 3}"> 
                <c:set var="quarterStartDate" value="${startDateQuarter3}" />
                <c:set var="quarterEndDate" value="${endDateQuarter3}" />
            </c:when>
            <c:otherwise> 
                <c:set var="quarterStartDate" value="${startDateQuarter4}" />
                <c:set var="quarterEndDate" value="${endDateQuarter4}" />
            </c:otherwise>
        </c:choose>

        <c:set var="quarterStatus" value="previous" />

        <c:choose>
			<c:when test="${quarterStatus eq current}"> 
                <div id="bdo_quarter${i}" class="tab-pane active">  
                    <c:set var="quarterStartDate" value="${quarterStartDate}" scope="request"/>
					<c:set var="quarterEndDate" value="${quarterEndDate}" scope="request"/>
					<c:set var="currentQuarter" value="true" scope="request"/>
                    <cq:include path="nav-login" resourceType= "gdc-checkin/components/content/bdo-goals-achievement-form" />
                </div>
            </c:when>

            <c:otherwise>
                <div id="bdo_quarter${i}" class="tab-pane"> 
                    <c:set var="quarterStartDate" value="${quarterStartDate}" scope="request"/>
					<c:set var="quarterEndDate" value="${quarterEndDate}" scope="request"/>
                    <cq:include path="nav-login" resourceType= "gdc-checkin/components/content/bdo-goals-achievement-form" />
        		</div>
            </c:otherwise>
        </c:choose>
    </c:forEach>
</div>
