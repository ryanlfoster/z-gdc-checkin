<%@ page import="com.adobe.gdc.checkin.UserManagementService,
													    javax.jcr.Session,
													    com.day.cq.commons.inherit.HierarchyNodeInheritanceValueMap" %>
<%@include file="/libs/foundation/global.jsp" %>

<%
    final String logoutPath = request.getContextPath() + "/system/sling/logout.html";
%>

<script type="text/javascript">
    function logout() {
    	 CQ.shared.Util.load("<%= logoutPath %>");
    }  
</script>

<%
  UserManagementService userManagementService = sling.getService(UserManagementService.class);
  Session session = resourceResolver.adaptTo(Session.class);
  final boolean isAnonymous = userManagementService.isAnonymous(session);
  
  if(isAnonymous){
	    HierarchyNodeInheritanceValueMap hnivm = new HierarchyNodeInheritanceValueMap(resource);   
     String loginPage = hnivm.getInherited("cq:loginPage","/login-page")+".html";
     response.sendRedirect(loginPage);
  }
  

%>
<c:set var="currentUser" value="<%=userManagementService.getCurrentUser(session)%>"  />

<c:set var="isLoggedIn" value="<%=!isAnonymous%>" />

<c:if test = "${isLoggedIn eq true}" >
    <div class="user-profile">
            <img src="http://img-prod.corp.adobe.com:8080/is/image/${currentUser}.00.jpg?fm-jpg&wid=180&hei=241&op_sharpen=1" onerror="this.src='/etc/designs/gdc-checkin/clientlibs-internal/images/admin-profile.jpg'" width="90" height="95">
            <span class="profile-name"> <%=userManagementService.getCurrentUserName(session)%> </span>
        
        <span class="profile-logout"><a href="javascript:logout();"><i class="glyphicon glyphicon-log-out"></i> Logout</a></span>
    </div>
</c:if>