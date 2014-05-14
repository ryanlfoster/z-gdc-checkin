<%@ page import="com.day.cq.commons.Doctype, 
       com.day.cq.i18n.I18n, com.day.text.Text,
    com.day.cq.wcm.api.WCMMode,
    com.day.cq.personalization.UserPropertiesUtil,
    com.day.cq.commons.inherit.HierarchyNodeInheritanceValueMap" %>
<%@include file="/libs/foundation/global.jsp" %>
<%@taglib prefix="personalization" uri="http://www.day.com/taglibs/cq/personalization/1.0" %>
<%
    final boolean isAnonymous = UserPropertiesUtil.isAnonymous(slingRequest);
    final boolean isDisabled = WCMMode.DISABLED.equals(WCMMode.fromRequest(request));
    final String logoutPath = request.getContextPath() + "/system/sling/logout.html";
%>
<script type="text/javascript">function logout() {

    $(".user-profile").hide();
    if (_g && _g.shared && _g.shared.ClientSidePersistence) {
        _g.shared.ClientSidePersistence.clearAllMaps();
    }

<% if( !isDisabled ) { %>
    if (CQ_Analytics && CQ_Analytics.CCM) {
        CQ_Analytics.ProfileDataMgr.loadProfile("anonymous");
        CQ.shared.Util.reload();
    }
<% } else { %>
    if (CQ_Analytics && CQ_Analytics.CCM) {
        CQ_Analytics.ProfileDataMgr.clear();
        CQ_Analytics.CCM.reset();
    }
    CQ.shared.Util.load("<%= logoutPath %>");
    <% }
%>}</script>

<%
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
            <span>Welcome <personalization:contextProfileProperty propertyName="formattedName"/> </span>
        </h3>
        <span class="profile-logout"><a href="javascript:logout();"><i class="icon-signout icon-large"></i> Logout</a></span>
    </div>
</c:if>