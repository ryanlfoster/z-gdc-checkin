<%@ page contentType="text/html; charset=utf-8" %>
<%@include file="/libs/foundation/global.jsp" %>
<%@page session="false" %>
<%@page import="org.apache.commons.lang.StringUtils, com.adobe.gdc.checkin.QuarterlyBDORepositoryClient,com.adobe.gdc.checkin.UserManagementService, java.util.Map, javax.jcr.Session" %>

<c:choose>
	<c:when test="<%=StringUtils.isNotBlank(request.getParameter("name"))%>">
		<c:set var="name" value="<%=request.getParameter("name")%>" />
	</c:when>
	<c:otherwise>
		<c:set var="name" value="${name}" />
	</c:otherwise>
</c:choose>

<c:choose>
	<c:when test="<%=StringUtils.isNotBlank(request.getParameter("designation"))%>">
		<c:set var="designation" value="<%=request.getParameter("designation")%>" />
	</c:when>
	<c:otherwise>
		<c:set var="designation" value="${designation}" />
	</c:otherwise>
</c:choose>

<c:choose>
	<c:when test="<%=StringUtils.isNotBlank(request.getParameter("bdoObjectives"))%>">
		<c:set var="bdoObjectives" value="<%=request.getParameter("bdoObjectives")%>" />
	</c:when>
	<c:otherwise>
		<c:set var="bdoObjectives" value="${bdoObjectives}" />
	</c:otherwise>
</c:choose>

<c:choose>
	<c:when test="<%=StringUtils.isNotBlank(request.getParameter("bdoAchievements"))%>">
		<c:set var="bdoAchievements" value="<%=request.getParameter("bdoAchievements")%>" />
	</c:when>
	<c:otherwise>
	</c:otherwise>
</c:choose>

<c:choose>
	<c:when test="<%=StringUtils.isNotBlank(request.getParameter("percentageAchieved"))%>">
		<c:set var="percentageAchieved" value="<%=request.getParameter("percentageAchieved")%>" />
	</c:when>
	<c:otherwise>
		<c:set var="percentageAchieved" value="${percentageAchieved}" />
	</c:otherwise>
</c:choose>

<c:choose>
	<c:when test="<%=StringUtils.isNotBlank(request.getParameter("status"))%>">
		<c:set var="status" value="<%=request.getParameter("status")%>" />
	</c:when>
	<c:otherwise>
		<c:set var="status" value="${status}" />
	</c:otherwise>
</c:choose>

<!-- Set the below variables to display edit form -->
<c:if test="<%=StringUtils.isNotBlank(request.getParameter("quarterNumber"))%>">
	<c:set var="quarterNumber" value="<%=request.getParameter("quarterNumber")%>" scope="request"/>
</c:if>
<c:if test="<%=StringUtils.isNotBlank(request.getParameter("annualYear"))%>">
	<c:set var="annualYear" value="<%=request.getParameter("annualYear")%>" scope="request"/>
</c:if>
<c:if test="<%=StringUtils.isNotBlank(request.getParameter("userID"))%>">
	<c:set var="userID" value="<%=request.getParameter("userID")%>" scope="request"/>
</c:if>

<c:set var="editBdoScore" value="true" scope="request"/>

${name}
${designation}

${bdoObjectives}
${bdoAchievements}
${percentageAchieved}
${status}

<c:if test="<%=StringUtils.isNotBlank(request.getParameter("editForm"))%>">
	<a class="btn btn-primary btn-edit fancybox fancybox.iframe" href="/content/gdc-check-in/en/dynamic-pages/edit-quartery-bdo.html?quarterNumber=${quarterNumber}&annualYear=${annualYear}&userID=${userID}&editForm=true&editBdoScore=true">Edit</a>
</c:if>
