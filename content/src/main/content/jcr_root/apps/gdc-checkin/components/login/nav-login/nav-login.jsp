<%@ page import="com.adobe.gdc.checkin.UserManagementService,
    javax.jcr.Session,
    com.day.cq.commons.inherit.HierarchyNodeInheritanceValueMap" %>
<%@include file="/libs/foundation/global.jsp" %>

<script type="text/javascript">
    function logout() {
        document.cookie = "gdc-user=; expires=Thu, 01 Jan 1970 00:00:00 GMT; path=/";
        location.reload();
    }  
</script>

<%
  UserManagementService userManagementService = sling.getService(UserManagementService.class);
  final boolean isAnonymous = userManagementService.isAnonymous(request);

  Session session = resourceResolver.adaptTo(Session.class);
  HierarchyNodeInheritanceValueMap hnivm = new HierarchyNodeInheritanceValueMap(resource);    
  if(isAnonymous){
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