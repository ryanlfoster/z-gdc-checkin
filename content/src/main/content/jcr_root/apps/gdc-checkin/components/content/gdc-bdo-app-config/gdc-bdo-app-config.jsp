<%--

  GDC-BDO Application Configuration component.

  This is a Configuration component used to input the default configuration required to work application smoothly.

--%>
<%@include file="/libs/foundation/global.jsp"%>
<%@page session="false" %>
<%@page import="com.adobe.gdc.checkin.QuarterlyBDOCalendarService,
                java.text.SimpleDateFormat,
                java.util.Map,
                java.util.Calendar,
                javax.jcr.Session" %>
<%
 QuarterlyBDOCalendarService quarterlyBDOCalendarService = sling.getService(QuarterlyBDOCalendarService.class);
 Map<String, Calendar> allQuartersDateRangeMap = quarterlyBDOCalendarService.getAllQuartersDateRangeMap();

 SimpleDateFormat date_format = new SimpleDateFormat("MM/dd/yyyy");
%>

<div class="row">
  <div class="col-md-9 col-xs-9">
        <div class="panel panel-default">
            <div class="panel-heading">
                <h3 class="panel-title">Set Fiscal Year Configuration</h3>
            </div>
            <div class="panel-body">
    <div class="row no-margin">
                    <div class="config-form">
                        <div class="row">
                            <div class="col-md-1 col-xs-1"></div>
                            <div class="col-md-8 col-xs-8" id="form-message"></div>
                        </div>
                        <br />
                        <form id="config-form1" class="config-form1" name="config-form1" action="<%= currentPage.getPath()%>" method="POST">
                            <div class="row" style="height:25px"></div>
                            <div class="row">
                                <div class="col-md-1 col-xs-1"></div>
                                <div class="col-md-2 col-xs-2">
                                    <label for="fiscalYear"> Current Fiscal Year </label>
                                </div>
                                <div class="col-md-8 col-xs-8"> 
                                    <input type="text" name="fiscalYear" id="fiscalYear" value="<%=quarterlyBDOCalendarService.getFiscalYearFromCalendarNode() %>" />&nbsp;&nbsp;&nbsp;<span id="errmsg-fiscalYear" style="color:red"></span>
                                </div>
                                <div class="col-md-1 col-xs-1"></div>
                            </div>
                            <div class="row" style="height:25px">
        <div class="col-md-1 col-xs-1"></div>
        <div class="col-md-8 col-xs-8">
                                    <span> Note:: Please Enter the current fiscal year in given example format (example:: 2014)</span>
                                </div>
                            </div>
                            <div class="row" style="height:25px"></div>
                            <div class="row">
                                <div class="col-md-1 col-xs-1"></div>
                                <div class="col-md-2 col-xs-2">
                                    <label for="bufferDays"> Buffer Period Days </label>
                                </div>
                                <div class="col-md-8 col-xs-8">
                                    <input type="text" name="bufferDays" id="bufferDays" value="<%=quarterlyBDOCalendarService.getBufferDaysFromCalendarNode()%>" />&nbsp;&nbsp;&nbsp;<span id="errmsg-bufferDays" style="color:red"></span>
                                </div>
                                <div class="col-md-1 col-xs-1"></div>
                            </div>
                            <div class="row" style="height:25px"></div>
       <div class="row">
                                <div class="col-md-1 col-xs-1"></div>
                                <div class="col-md-2 col-xs-2">
                                    <label for="Q1StartDate"> Quarter1 Start Date </label>
                                </div>
                                <div class="col-md-2 col-xs-2">
                                    <input type="text" readonly="true" placeholder="Choose Date..." name="Q1StartDate" id="Q1StartDate" value="<%=date_format.format(allQuartersDateRangeMap.get("startDateQuarter1").getTime())%>" />
                                </div>
                                <div class="col-md-2 col-xs-2">
                                    <label for="Q1EndDate"> Quarter1 End Date </label>
                                </div>
                                <div class="col-md-2 col-xs-2">
                                    <input type="text" readonly="true" placeholder="Choose Date..." name="Q1EndDate" id="Q1EndDate" value="<%=date_format.format(allQuartersDateRangeMap.get("endDateQuarter1").getTime())%>" />
                                </div>
                                <div class="col-md-1 col-xs-1"></div>
                            </div>
                            <div class="row" style="height:25px">
                                <div class="col-md-1 col-xs-1"></div>
        <div class="col-md-4 col-xs-4">
                                    <span id="errmsg-Q1StartDate" style="color:red"></span>
                                </div>
                                <div class="col-md-4 col-xs-4">
                                    <span id="errmsg-Q1EndDate" style="color:red"></span>
                                </div>
                                <div class="col-md-1 col-xs-1"></div>
                            </div>
                            <div class="row">
                                <div class="col-md-1 col-xs-1"></div>
                                <div class="col-md-2 col-xs-2">
                                    <label for="Q2StartDate"> Quarter2 Start Date </label>
                                </div>
                                <div class="col-md-2 col-xs-2">
                                    <input type="text" readonly="true" placeholder="Choose Date..." name="Q2StartDate" id="Q2StartDate" value="<%=date_format.format(allQuartersDateRangeMap.get("startDateQuarter2").getTime())%>" />
                                </div>
                                <div class="col-md-2 col-xs-2">
                                    <label for="Q2EndDate"> Quarter2 End Date </label>
                                </div>
                                <div class="col-md-2 col-xs-2">
                                    <input type="text" readonly="true" placeholder="Choose Date..." name="Q2EndDate" id="Q2EndDate" value="<%=date_format.format(allQuartersDateRangeMap.get("endDateQuarter2").getTime())%>" />
                                </div>
                                <div class="col-md-1 col-xs-1"></div>
                            </div>
                            <div class="row" style="height:25px">
                                <div class="col-md-1 col-xs-1"></div>
        <div class="col-md-4 col-xs-4">
                                    <span id="errmsg-Q2StartDate" style="color:red"></span>
                                </div>
                                <div class="col-md-4 col-xs-4">
                                    <span id="errmsg-Q2EndDate" style="color:red"></span>
                                </div>
                                <div class="col-md-1 col-xs-1"></div>
                            </div>
                            <div class="row">
                                <div class="col-md-1 col-xs-1"></div>
                                <div class="col-md-2 col-xs-2">
                                    <label for="Q3StartDate"> Quarter3 Start Date </label>
                                </div>
                                <div class="col-md-2 col-xs-2">
                                    <input type="text" readonly="true" placeholder="Choose Date..." name="Q3StartDate" id="Q3StartDate" value="<%=date_format.format(allQuartersDateRangeMap.get("startDateQuarter3").getTime())%>" />
                                </div>
                                <div class="col-md-2 col-xs-2">
                                    <label for="Q3EndDate"> Quarter3 End Date </label>
                                </div>
                                <div class="col-md-2 col-xs-2">
                                    <input type="text" readonly="true" placeholder="Choose Date..." name="Q3EndDate" id="Q3EndDate" value="<%=date_format.format(allQuartersDateRangeMap.get("endDateQuarter3").getTime())%>" />
                                </div>
                                <div class="col-md-1 col-xs-1"></div>
                            </div>
                            <div class="row" style="height:25px">
                                <div class="col-md-1 col-xs-1"></div>
        <div class="col-md-4 col-xs-4">
                                    <span id="errmsg-Q3StartDate" style="color:red"></span>
                                </div>
                                <div class="col-md-4 col-xs-4">
                                    <span id="errmsg-Q3EndDate" style="color:red"></span>
                                </div>
                                <div class="col-md-1 col-xs-1"></div>
                            </div>
                            <div class="row"> 
                                <div class="col-md-1 col-xs-1"></div>
                                <div class="col-md-2 col-xs-2">
                                    <label for="Q4StartDate"> Quarter4 Start Date </label>
                                </div>
                                <div class="col-md-2 col-xs-2">
                                    <input type="text" readonly="true" placeholder="Choose Date..." name="Q4StartDate" id="Q4StartDate" value="<%=date_format.format(allQuartersDateRangeMap.get("startDateQuarter4").getTime())%>" />
                                </div>
                                <div class="col-md-2 col-xs-2">
                                    <label for="Q4EndDate"> Quarter4 End Date </label>
                                </div>
                                <div class="col-md-2 col-xs-2">
                                    <input type="text" readonly="true" placeholder="Choose Date..." name="Q4EndDate" id="Q4EndDate" value="<%=date_format.format(allQuartersDateRangeMap.get("endDateQuarter4").getTime())%>" />
                                </div>
                                <div class="col-md-1 col-xs-1"></div>
                            </div>
                            <div class="row" style="height:25px">
                                <div class="col-md-1 col-xs-1"></div>
        <div class="col-md-4 col-xs-4">
                                    <span id="errmsg-Q4StartDate" style="color:red"></span>
                                </div>
                                <div class="col-md-4 col-xs-4">
                                    <span id="errmsg-Q4EndDate" style="color:red"></span>
                                </div>
                                <div class="col-md-1 col-xs-1"></div>
                            </div>
       <div class="row">
                             <div class="col-md-4 col-xs-4"></div>
                             <div class="col-md-7 col-xs-7 align-right">
                                 <div class="col-sm-10  col-xs-10 col-md-10">
                                  <button type="button" class="btn btn-primary btn-submit">Submit</button>
         </div>
                                 <div class="col-sm-2  col-xs-2 col-md-2  align-left"></div>
                             </div>
                             <div class="col-md-1 col-xs-1"></div>
                         </div>
                            <div class="row" style="height:25px"></div>
                        </form>
                    </div>
                </div>
   </div>
        </div>
    </div>
</div>


<script>
$(document).ready(function() {
    jQuery( "#Q1StartDate" ).datepicker();
    jQuery( "#Q2StartDate" ).datepicker();
    jQuery( "#Q3StartDate" ).datepicker();
    jQuery( "#Q4StartDate" ).datepicker();

    jQuery( "#Q1EndDate" ).datepicker();
    jQuery( "#Q2EndDate" ).datepicker();
    jQuery( "#Q3EndDate" ).datepicker();
    jQuery( "#Q4EndDate" ).datepicker();

    $(".btn-submit").click(function() {
        if(GDC.bdo.appconfig.validateOnSubmit()){
            //alert($('.config-form1 #Q1StartDate').val());
            GDC.bdo.appconfig.submitConfiguration();
        }
    });

    $('#fiscalYear').keydown(function(e) {
  // Allow: backspace, delete, tab, escape and enter
        if ($.inArray(e.keyCode, [46, 8, 9, 27, 13, 110]) !== -1 ||
             // Allow: Ctrl+A
            (e.keyCode == 65 && e.ctrlKey === true) || 
             // Allow: home, end, left, right
            (e.keyCode >= 35 && e.keyCode <= 39)) {
                 // let it happen, don't do anything
                 return;
        }
        // Ensure that it is a number and stop the keypress
        if ((e.shiftKey || (e.keyCode < 48 || e.keyCode > 57)) && (e.keyCode < 96 || e.keyCode > 105)) {
            e.preventDefault();
            $("#errmsg-fiscalYear").html("Not allowed!").show().fadeOut(2000);
        } else {
            $("#errmsg-fiscalYear").hide();
        }
    });

    $('#fiscalYear').bind('copy paste cut',function(e) { 
        e.preventDefault(); //disable cut,copy,paste
        $("#errmsg-fiscalYear").html("Not allowed!").show().fadeOut(2000);
    });

 $('#bufferDays').keydown(function(e) {
  // Allow: backspace, delete, tab, escape and enter
        if ($.inArray(e.keyCode, [46, 8, 9, 27, 13, 110]) !== -1 ||
             // Allow: Ctrl+A
            (e.keyCode == 65 && e.ctrlKey === true) || 
             // Allow: home, end, left, right
            (e.keyCode >= 35 && e.keyCode <= 39)) {
                 // let it happen, don't do anything
                 return;
        }
        // Ensure that it is a number and stop the keypress
        if ((e.shiftKey || (e.keyCode < 48 || e.keyCode > 57)) && (e.keyCode < 96 || e.keyCode > 105)) {
            e.preventDefault();
            $("#errmsg-bufferDays").html("Not allowed!").show().fadeOut(2000);
        } else {
            $("#errmsg-bufferDays").hide();
        }
    });


    $('#bufferDays').bind('copy paste cut',function(e) { 
        e.preventDefault(); //disable cut,copy,paste
        $("#errmsg-bufferDays").html("Not allowed!").show().fadeOut(2000);
    });
});
</script>