<%-- 
 This is the body part, include header, content & footer here. <br>
--%>

<%@ page contentType="text/html; charset=utf-8" %>
<%@include file="/libs/foundation/global.jsp" %>
<%@page session="false" %>

<body data-spy="scroll" data-target="#innerNav" data-offset="40">
<cq:include path="clientcontext" resourceType="cq/personalization/components/clientcontext"/>

<cq:include script="header.jsp" />
    
<cq:include script="content.jsp" />

<cq:include script="footer.jsp" />

<cq:include path="cloudservices" resourceType="cq/cloudserviceconfigs/components/servicecomponents"/>

</body>