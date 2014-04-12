<%@ page contentType="text/html; charset=utf-8" %>
<%@include file="/libs/foundation/global.jsp" %>
<%@page session="false" %>

<div class="container">
        <div class="login-panel">
            <div class="row login-panel-row">
                <div class="col-md-6 col-xs-6 gray-bg">
                    <div class="login-bg">
                         <div class="login-header">
                            LOGIN
                        </div>
                    </div>
                   
                </div>
                 <div class="col-md-6 col-xs-6">
                     <form class="login-form">
                         <div class="input-group login-input">
                          <span class="input-group-addon"><i class="fa fa-user fa-fw"></i></span>
                            <input class="form-control" type="text" placeholder="Your Username">
                        </div>
                         <p></p>
                        <div class="input-group login-input">
                          <span class="input-group-addon"><i class="fa fa-key fa-fw"></i></span>
                            <input class="form-control" type="password" placeholder="Your password">
                        </div>
                         <div class="pull-right login-action-panel"><a class="btn btn-primary login" href="/content/gdc-check-in/en/check-in-home.html">Log in</a></div>
                        
                     </form>
                </div>
            </div>
        </div>
</div>