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

<c:set var="isLoggedIn" value="<%=!isAnonymous%>" />

<c:if test = "${isLoggedIn eq true}" >
    <div class="user-profile">
        <h3><i class="fa fa-user fa-fw sign-in"></i> 
            <span>Welcome <%=userManagementService.getCurrentUserName(session)%> </span>
        </h3>
        <span class="profile-logout"><a href="javascript:logout();"><i class="icon-signout icon-large"></i> Logout</a></span>
    </div>
</c:if>