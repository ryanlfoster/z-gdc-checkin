<%@ page contentType="text/html; charset=utf-8" %>
<%@include file="/libs/foundation/global.jsp" %>
<%@page session="false" %>
<%@page import="org.apache.commons.lang.StringUtils,
                com.adobe.gdc.checkin.QuarterlyBDORepositoryClient,
                com.adobe.gdc.checkin.UserManagementService,
                java.util.Map, javax.jcr.Session" %>

<c:choose>
 <c:when test="<%=StringUtils.isNotBlank(request.getParameter("name"))%>">
  <c:set var="name" value="<%=request.getParameter("name")%>" />
 </c:when>
 <c:otherwise>
  <c:set var="name" value="${name}" />
 </c:otherwise>
</c:choose>

<c:choose>
 <c:when test="<%=StringUtils.isNotBlank(request.getParameter("designation"))%>">
  <c:set var="designation" value="<%=request.getParameter("designation")%>" />
 </c:when>
 <c:otherwise>
  <c:set var="designation" value="${designation}" />
 </c:otherwise>
</c:choose>

<c:choose>
 <c:when test="<%=StringUtils.isNotBlank(request.getParameter("bdoObjectives"))%>">
  <c:set var="bdoObjectives" value="<%=request.getParameterValues("bdoObjectives")%>" />
 </c:when>
 <c:otherwise>
  <c:set var="bdoObjectives" value="${bdoObjectivesValues}" />
 </c:otherwise>
</c:choose>

<c:choose>
 <c:when test="<%=StringUtils.isNotBlank(request.getParameter("bdoAchievements"))%>">
  <c:set var="bdoAchievements" value="<%=request.getParameterValues("bdoAchievements")%>" />
 </c:when>
 <c:otherwise>
        <c:set var="bdoAchievements" value="${bdoAchievementsValues}" />
 </c:otherwise>
</c:choose>

<c:choose>
 <c:when test="<%=StringUtils.isNotBlank(request.getParameter("bdoScore"))%>">
  <c:set var="bdoScore" value="<%=request.getParameter("bdoScore")%>" />
 </c:when>
</c:choose>

<c:choose>
 <c:when test="<%=StringUtils.isNotBlank(request.getParameter("status"))%>">
  <c:set var="status" value="<%=request.getParameter("status")%>" />
 </c:when>
 <c:otherwise>
  <c:set var="status" value="${status}" />
 </c:otherwise>
</c:choose>

<c:choose>
 <c:when test="<%=StringUtils.isNotBlank(request.getParameter("editForm"))%>">
  <c:set var="numbering" value="numbering-green"/> 
 </c:when>
 <c:otherwise>
  <c:set var="numbering" value="numbering-blue"/> 
 </c:otherwise>
</c:choose>


<!-- Set the below variables to display edit form -->
<c:if test="<%=StringUtils.isNotBlank(request.getParameter("quarterNumber"))%>">
 <c:set var="quarterNumber" value="<%=request.getParameter("quarterNumber")%>" scope="request"/>
</c:if>
<c:if test="<%=StringUtils.isNotBlank(request.getParameter("annualYear"))%>">
 <c:set var="annualYear" value="<%=request.getParameter("annualYear")%>" scope="request"/>
</c:if>
<c:if test="<%=StringUtils.isNotBlank(request.getParameter("userID"))%>">
 <c:set var="userID" value="<%=request.getParameter("userID")%>" scope="request"/>
</c:if>


<c:choose>
 <c:when test="<%=StringUtils.isNotBlank(request.getParameter("editForm"))%>">
  <div class="row no-margin ">
            <div class="col-md-12 col-xs-12 white-panel-large">
 </c:when>
 <c:otherwise>
  <div class="row">
            <div class="col-md-12 col-xs-12 white-panel-small">
 </c:otherwise>
</c:choose>



        <div class="row">
            <div class="col-md-1 col-xs-1"></div>
            <div class="col-md-10 col-xs-10 margin">
             ${name},<br/>
             ${designation}<br/><br/>
            </div>               

            <div class="col-md-1 col-xs-1"></div>
        </div>
        <br/>

        <div class="row">
            <div class="col-md-1 col-xs-1"></div>
            <div class="col-md-3 col-xs-3">
                <label for="objective">
                    BDO Objectives
                </label>
            </div>
            <c:choose>
                <c:when test="${not empty bdoObjectives}">
                    <div class="col-md-8 col-xs-8">
      <c:forEach items="${bdoObjectives}" varStatus="objectives">  
                            <span class="${numbering}"> ${objectives.index +1}. </span> &nbsp; ${bdoObjectives[objectives.index]}<br/>  
           </c:forEach>  
                    </div>
                </c:when>
                <c:otherwise>
                    <div class="col-md-8 col-xs-8">Unavailable</div>
                </c:otherwise>
            </c:choose>

         </div>
   <br/>

   <div class="row">
            <div class="col-md-1 col-xs-1"></div>
            <div class="col-md-3 col-xs-3">
                <label for="achievements">
                   BDO Achievements
                </label>
            </div>
             <c:choose>
                <c:when test="${not empty bdoAchievements}">
                    <div class="col-md-8 col-xs-8">
                        <c:forEach items="${bdoAchievements}" varStatus="achievement">  
                            <span class="${numbering}"> ${achievement.index +1}. </span> &nbsp; ${bdoAchievements[achievement.index]}<br/>  
           </c:forEach>  

                    </div>
                </c:when>
                <c:otherwise>
                    <div class="col-md-8 col-xs-8">Unavailable</div>
                </c:otherwise>
            </c:choose>
         </div>
   <br/>

         <div class="row">
            <div class="col-md-1 col-xs-1"></div>
            <div class="col-md-3 col-xs-3">
                <label for="bdoScore">
                   BDO Score
                </label>
            </div>
             <c:choose>
                <c:when test="${not empty bdoScore}">
                    <div class="col-md-8 col-xs-8">${bdoScore} %</div>
                </c:when>
                <c:otherwise>
                    <div class="col-md-8 col-xs-8">Unavailable</div>
                </c:otherwise>
            </c:choose>

         </div>
   <br/>

        <div class="row">
            <div class="col-md-1 col-xs-1"></div>
            <div class="col-md-3 col-xs-3">
                <label for="status">
                   Status
                </label>
            </div>
            <c:choose>
                <c:when test="${not empty status}">
                    <div class="col-md-8 col-xs-8">${status}</div>
                </c:when>
                <c:otherwise>
                    <div class="col-md-8 col-xs-8">Unavailable</div>
                </c:otherwise>
            </c:choose>

         </div>
   <br/>

        <c:if test="<%=StringUtils.isNotBlank(request.getParameter("editForm"))%>">
            <div class="row">
                <div class="col-md-4 col-xs-4"></div>
                <div class="col-md-8 col-xs-8">
        
                    <a class="btn btn-success btn-edit fancybox fancybox.iframe" href="/content/gdc-bdo/en/dynamic-pages/edit-quartery-bdo.html?quarterNumber=${quarterNumber}&annualYear=${annualYear}&userID=${userID}&editForm=true&editBdoScore=true">Edit</a>
                    &nbsp;
        
                </div>
        
            </div>
        
        </c:if>

   </div>
</div>