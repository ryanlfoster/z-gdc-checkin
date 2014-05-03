<%@ page contentType="text/html; charset=utf-8" %>
<%@include file="/libs/foundation/global.jsp" %>
<%@page session="false" %>

<c:set var="isLoggedIn" value="true" />

<c:choose>
	<c:when test = "${isLoggedIn eq true}" >
 		<div class="user-profile">
 			<h3><i class="fa fa-user fa-fw sign-in"></i>  John</h3>

    		<a href="#">profile</a> | <a href="#">settings</a>
 			<a href="/content/gdc-bdo/en/gdc-bdo-home.html"><i class="icon-signout icon-large"></i> Logout</a>
		</div>
    </c:when>
    <c:otherwise>
        <div class="nav_login_container">
            Login to access this space ! 
			<form name="f" action="j_spring_security_check" method="post">
                <div class="login_field">
                    <img src="/etc/designs/gdc-checkin/clientlibs-internal/images/user.png"><input name="j_username" id="username" type="text">
                </div>
                <div class="login_field">
                    <img src="/etc/designs/gdc-checkin/clientlibs-internal/images/password.png"><input name="j_password" type="password">
                </div>
                <div class="login_check">
                    <input value="Remember Me" type="checkbox">Remember Me
                </div>
                <div class="login_button_wrapper">
                    <input name="submit" class="login_button" value="Login" type="submit">					
                    <p>
                        <a href="javascript:void(0)" onclick="forgotPwd();" class="forget">Forgot Password</a>
                    </p>
                </div>
			</form>
        </div>
    </c:otherwise>
</c:choose>