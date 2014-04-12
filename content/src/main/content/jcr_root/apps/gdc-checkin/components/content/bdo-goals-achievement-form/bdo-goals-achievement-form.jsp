<%@ page contentType="text/html; charset=utf-8" %>
<%@include file="/libs/foundation/global.jsp" %>
<%@page session="false" %>

<c:out value="${quarterNumber}"/>

<c:if test = "${currentQuarter eq 'true'}">
    <div class="row">
        <div class="col-md-3 col-xs-3">
            <div class="panel panel-default">
                <div class="panel-heading">
                    <h3 class="panel-title">Achievement (%)</h3>
                </div>
                <div class="panel-body">
                    <div id="achievementChart"></div>
                </div>
            </div>
        </div>
    </div>
</c:if>