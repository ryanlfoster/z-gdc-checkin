<%@ page contentType="text/html; charset=utf-8" %>

<%@ page import="com.day.cq.i18n.I18n,
            org.apache.sling.api.resource.Resource,
                   com.day.cq.personalization.UserPropertiesUtil,
                   com.day.cq.wcm.api.WCMMode,
       org.apache.commons.lang.StringUtils,
                   com.day.cq.wcm.foundation.forms.FormsHelper,
                   com.day.text.Text" %>

<%@include file="/libs/foundation/global.jsp" %>
<%@page session="false" %>
<cq:defineObjects />

<%
String id = Text.getName(resource.getPath());
response.setContentType("text/html");
response.setCharacterEncoding("UTF-8");

I18n i18n = new I18n(slingRequest);
final String contextPath = slingRequest.getContextPath();
String action = contextPath + resource.getPath() + ".html/j_security_check";

String redirectTo ="/content/gdc-bdo/en/gdc-bdo-home.html";
redirectTo = slingRequest.getResourceResolver().map(request, redirectTo);

final boolean isAnonymous = UserPropertiesUtil.isAnonymous(slingRequest);

if (isAnonymous) {
    String jReason = request.getParameter("j_reason");
    if (jReason == null) {
        jReason = "";
    }
%>

 <div class="container">
  <div class="login-panel">
   <div class="row login-panel-row">
    <div class="col-md-6 col-xs-6 gray-bg">
     <div class="login-bg">
       <div class="login-header">LOGIN</div>
     </div>
    </div>
    <div class="col-md-6 col-xs-6">
     <form id="<%= xssAPI.encodeForHTMLAttr(id) %>" name="<%= xssAPI.encodeForHTMLAttr(id) %>" method="POST" action="<%= xssAPI.getValidHref(action) %>" class="login-form" enctype="multipart/form-data" onsubmit="return validateLogin();">
       <div id="login-error" class="<%= jReason.length() > 0 ? "err-visible" : "err-hidden" %>">
        <%= xssAPI.encodeForHTML(i18n.getVar(jReason)) %>&nbsp;
       </div>
       <input type="hidden" id="resource" name="resource" value="<%= xssAPI.encodeForHTMLAttr(redirectTo) %>">
       <div class="input-group login-input">
        <span class="input-group-addon"><i class="fa fa-user fa-fw"></i></span>
        <input id="<%= xssAPI.encodeForHTMLAttr(id + "_username")%>" class="form-control" type="text" placeholder="Your Username" name="j_username"/>
       </div>
       <p></p>
       <div class="input-group login-input">
        <span class="input-group-addon"><i class="fa fa-key fa-fw"></i></span>
         <input id="<%= xssAPI.encodeForHTMLAttr(id + "_password")%>" class="form-control" type="password" placeholder="Your password" autocomplete="off" oncopy="return false;" onpaste="return false;" oncut="return false;" name="j_password"/>
       </div>
       <div class="pull-right login-action-panel">
        <input class="btn btn-primary login" type="submit" value="Login"/>
       </div>
     </form>
    </div>
   </div>
  </div>
 </div>

 <script type="text/javascript">

 var xmlhttp = null;

 function getXmlHttp() {
  if (xmlhttp) {
   return xmlhttp;
  }

  if (window.XMLHttpRequest) {
   xmlhttp = new XMLHttpRequest();
  } else {
   if (window.ActiveXObject) {
    try {
     xmlhttp = new ActiveXObject('Msxml2.XMLHTTP');
    } catch (ex) {
     try {
      xmlhttp = new ActiveXObject('Microsoft.XMLHTTP');
     } catch (ex) {
     }
    }
   }
  }
  return xmlhttp;
 }

 function sendRequest(/* String */path, /* String */user, /* String */ pass) {
  var xmlhttp = getXmlHttp();
  if (!xmlhttp) {
   return false;
  }

  if (xmlhttp.readyState < 4) {
   xmlhttp.abort();
  }

  // send the authentication request
  var params = "j_validate=true";
  params += "&j_username=" + encodeURIComponent(user);
  params += "&j_password=" + encodeURIComponent(pass);
  params += "&_charset_=utf8";
  xmlhttp.open('POST', path, false);
  xmlhttp.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
  xmlhttp.setRequestHeader("Content-length", params.length);
  xmlhttp.setRequestHeader("Connection", "close");
  xmlhttp.send(params);
  return xmlhttp.status != 403;
 }



 function createCookie(name, value, days, path) {
  if (days) {
   var date = new Date();
   date.setTime(date.getTime() + (days * 24 * 60 * 60 * 1000));
   var expires = "; expires="+date.toGMTString();
  } else {
   var expires = "";
  }
  path = path || "/";
  document.cookie = name + "=" + value + expires + "; path=" + path;
 }

 function eraseCookie(name, path) {
  createCookie(name, "", -1, path);
 }


 function showError() {
  var msg="User name and password do not match";
  try {

   var loginError = document.getElementById("login-error");
   loginError.innerHTML = msg;
   loginError.className = "err-visible";
  } catch (e) {
   alert(msg+"::"+e.message);
  }
 }


 function validateLogin() {
   var user = document.forms['<%=id%>']['j_username'].value;
   var pass = document.forms['<%=id%>']['j_password'].value;
   var path = document.forms['<%=id%>'].action;
   var resource = document.forms['<%=id%>'].resource.value;


 if (!user) {
  return false;
 }


 // send user/id password to check and persist
  if (sendRequest(path, user, pass)) {
   // erase legacy login #37548
   eraseCookie("login-token", "/crx");
  var u = resource;
   if (window.location.hash && u.indexOf('#') < 0) {
    u = u + window.location.hash;
   }

   document.location = u;
   CQ_Analytics.ProfileDataMgr.loadProfile(user);

  } else {

   showError();
  }

 return false;

 }

</script>

<%
  }  
%>

<script>
	$(document).ready(function() {
	
	    if(!<%=isAnonymous%>) {
	        window.location.assign("<%=redirectTo%>")
	    }
	 });
</script>