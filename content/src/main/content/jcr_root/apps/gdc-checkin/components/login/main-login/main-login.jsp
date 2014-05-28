<%@ page contentType="text/html; charset=utf-8" %>

<%@ page import="com.day.cq.i18n.I18n,
                 org.apache.sling.api.resource.Resource,
                 com.adobe.gdc.checkin.UserManagementService,
                 org.apache.commons.lang.StringUtils,
                 com.day.text.Text" %>

<%@include file="/libs/foundation/global.jsp" %>
<%@page session="false" %>
<cq:defineObjects />

<%
String id = Text.getName(resource.getPath());

I18n i18n = new I18n(slingRequest);
final String contextPath = slingRequest.getContextPath();
String action = contextPath + resource.getPath() + ".html/j_security_check";

String redirectTo ="/gdc-bdo-home.html";
redirectTo = slingRequest.getResourceResolver().map(request, redirectTo);

UserManagementService userManagementService = sling.getService(UserManagementService.class);
Session session = resourceResolver.adaptTo(Session.class);
final boolean isAnonymous = userManagementService.isAnonymous(session);

if (isAnonymous) {
    String jReason = request.getParameter("j_reason");
    if (jReason == null) {
        jReason = "";
    }
%>

<img src="/etc/designs/gdc-checkin/clientlibs-internal/images/logo.png" alt="" class="adobe-logo">
   <div class="container-page">
     <div class="container-logo">
       <img src="/etc/designs/gdc-checkin/clientlibs-internal/images/logo-gdc.png" alt="" class="logo">

       <div class="container-form">
         <h4 class="form-title">Log In</h4>
         <p class="form-info">Please log-in using your Adobe LDAP credential</p>
									<div id="login-error" class="<%= jReason.length() > 0 ? "err-visible" : "err-hidden" %>">
								  <%= xssAPI.encodeForHTML(i18n.getVar(jReason)) %>
									</div>
         <div class="login-form">
            <fieldset>
             <form id="<%= xssAPI.encodeForHTMLAttr(id) %>" name="<%= xssAPI.encodeForHTMLAttr(id) %>" method="POST" action="<%= xssAPI.getValidHref(action) %>" class="login-form" enctype="multipart/form-data" onsubmit="return validateLogin();">
<input type="hidden" id="resource" name="resource" value="<%= xssAPI.encodeForHTMLAttr(redirectTo) %>">
              <input id="<%= xssAPI.encodeForHTMLAttr(id + "_username")%>" class="form-control" type="text" placeholder="Your Username" name="j_username"/>
              <input id="<%= xssAPI.encodeForHTMLAttr(id + "_password")%>" class="form-control" type="password" placeholder="Your password" autocomplete="off" oncopy="return false;" onpaste="return false;" oncut="return false;" name="j_password"/>
              <input type="submit" value="Login"/>
          </form>
        </fieldset>
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
				} 
				else {
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
					} 
				 else {
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