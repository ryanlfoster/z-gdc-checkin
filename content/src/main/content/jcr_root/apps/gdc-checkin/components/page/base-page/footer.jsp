<%@ page contentType="text/html; charset=utf-8" %>
<%@include file="/libs/foundation/global.jsp" %>
<%@ page import="java.util.Calendar" %>
<%@page session="false" %>

<% 
Calendar todayscalendar = Calendar.getInstance();
int footerYear = todayscalendar.get(Calendar.YEAR);
%>
<div id="footer">
		<div class="container-fluid">
			<div class="row-fluid">
				<div class="span12">
					<div class="navbar navbar-blue">
						<div class="navbar-inner" style="text-align:center">
							<div class="copyright">© <%=footerYear%> Adobe Systems Incorporated. All Rights Reserved.</div>
						</div>
					</div>
				</div>
			</div>
		</div>	
</div>