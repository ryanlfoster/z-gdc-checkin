<%-- 
    Include all html head tag related information here.
 --%>

<%@ page contentType="text/html; charset=utf-8" %>
<%@include file="/libs/foundation/global.jsp" %>
<%@page session="false" %>

<%@ page import="com.day.cq.commons.Doctype" %><%
    String xs = Doctype.isXHTML(request) ? "/" : "";
    String favIcon = currentDesign.getPath() + "/favicon.ico";
    if (resourceResolver.getResource(favIcon) == null) {
        favIcon = null;
    }
%>
 
<head>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8"<%=xs%>>
    <meta name="keywords" content="<%= xssAPI.encodeForHTMLAttr(WCMUtils.getKeywords(currentPage, false)) %>"<%=xs%>>
    <meta name="description" content="<%= xssAPI.encodeForHTMLAttr(properties.get("jcr:description", "")) %>"<%=xs%>>
    <meta http-equiv=X-UA-Compatible content="IE=edge,chrome=1"/>
    <cq:include script="headlibs.jsp"/>
    <link href="/etc/designs/gdc-checkin/clientlibs-external/css/jqueryCalendar.css" type="text/css" rel="stylesheet"/>
    <cq:include script="/libs/wcm/core/components/init/init.jsp"/>
     <% if (favIcon != null) { %>
    <link rel="icon" type="image/vnd.microsoft.icon" href="<%= xssAPI.getValidHref(favIcon) %>"<%=xs%>>
    <link rel="shortcut icon" type="image/vnd.microsoft.icon" href="<%= xssAPI.getValidHref(favIcon) %>"<%=xs%>>
    <% } %>


<title>GDC BDO</title>


</head>