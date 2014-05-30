

GDC.bdo.appconfig.isEmpty = function(value) {
    return (value == undefined || value == null || value == "" || value.length == 0);

};

GDC.bdo.appconfig.validateOnSubmit = function() {
    var errorMsg = false;

    if(GDC.bdo.appconfig.isEmpty($('.config-form1 #fiscalYear').val())){
        $("#errmsg-fiscalYear").html("Please Enter Fiscal Year!").show();
        errorMsg = true;
    } else {
        $("#errmsg-fiscalYear").hide();
    }

    if(GDC.bdo.appconfig.isEmpty($('.config-form1 #bufferDays').val())){
        $("#errmsg-bufferDays").html("Please Enter Number of Buffer Period Days!").show();
        errorMsg = true;
    } else {
        $("#errmsg-bufferDays").hide();
    }

    if(GDC.bdo.appconfig.isEmpty($('.config-form1 #Q1StartDate').val())){
        $("#errmsg-Q1StartDate").html("Please Choose Quarter1 Start Date!").show();
        errorMsg = true;
    } else {
        $("#errmsg-Q1StartDate").hide();
    }

    if(GDC.bdo.appconfig.isEmpty($('.config-form1 #Q1EndDate').val())){
        $("#errmsg-Q1EndDate").html("Please Choose Quarter1 End Date!").show();
        errorMsg = true;
    } else {
        $("#errmsg-Q1EndDate").hide();
    }

    if(GDC.bdo.appconfig.isEmpty($('.config-form1 #Q2StartDate').val())){
        $("#errmsg-Q2StartDate").html("Please Choose Quarter2 Start End!").show();
        errorMsg = true;
    } else {
        $("#errmsg-Q2StartDate").hide();
    }

    if(GDC.bdo.appconfig.isEmpty($('.config-form1 #Q2EndDate').val())){
        $("#errmsg-Q2EndDate").html("Please Choose Quarter2 End Date!").show();
        errorMsg = true;
    } else {
        $("#errmsg-Q2EndDate").hide();
    }

    if(GDC.bdo.appconfig.isEmpty($('.config-form1 #Q3StartDate').val())){
        $("#errmsg-Q3StartDate").html("Please Choose Quarter3 Start Date!").show();
        errorMsg = true;
    } else {
        $("#errmsg-Q3StartDate").hide();
    }

    if(GDC.bdo.appconfig.isEmpty($('.config-form1 #Q3EndDate').val())){
        $("#errmsg-Q3EndDate").html("Please Choose Quarter3 End Date!").show();
        errorMsg = true;
    } else {
        $("#errmsg-Q3EndDate").hide();
    }

    if(GDC.bdo.appconfig.isEmpty($('.config-form1 #Q4StartDate').val())){
        $("#errmsg-Q4StartDate").html("Please Choose Quarter4 Start Date!").show();
        errorMsg = true;
    } else {
        $("#errmsg-Q4StartDate").hide();
    }

    if(GDC.bdo.appconfig.isEmpty($('.config-form1 #Q4EndDate').val())){
        $("#errmsg-Q4EndDate").html("Please Choose Quarter4 End Date!").show();
        errorMsg = true;
    } else {
        $("#errmsg-Q4EndDate").hide();
    }

    if(errorMsg) {
        return false;
    } else {
        return true;
    }
};

GDC.bdo.appconfig.submitConfiguration = function () {

    var actionURL = $('.config-form1').attr("action")+ ".bdoconfig.html";
    var actionType = $('.config-form1').attr("method");
    var requestParams = GDC.bdo.appconfig.getRequestParams();;

	$.ajax({
    	url: actionURL,
        type: actionType,
        data: requestParams,

        success: function (data) {

        	if(data.success == true) {
        		GDC.bdo.appconfig.notifySuccess("Submitted Successfully !");
        	} else {
				GDC.bdo.appconfig.notifyError("Unable to process your request due to an unknown technical error. Please try after sometime");
            }

        },

        error: function (jqXHR, textStatus, errorThrown) {

        }
    });

};

GDC.bdo.appconfig.getRequestParams = function() {
	var fiscalYear  = $('.config-form1 #fiscalYear').val();
    var bufferDays  = $('.config-form1 #bufferDays').val();
    var satrtDateQ1 = $('.config-form1 #Q1StartDate').val();
    var endDateQ1 = $('.config-form1 #Q1EndDate').val();
    var startDateQ2 = $('.config-form1 #Q2StartDate').val();
	var endDateQ2 = $('.config-form1 #Q2EndDate').val();
	var startDateQ3 = $('.config-form1 #Q3StartDate').val();
	var endDateQ3 = $('.config-form1 #Q3EndDate').val();
	var startDateQ4 = $('.config-form1 #Q4StartDate').val();
	var endDateQ4 = $('.config-form1 #Q4EndDate').val();

    var json = {"fiscalYear" : fiscalYear,
                "bufferDays" : bufferDays,
                "startDateQuarter1" : satrtDateQ1,
                "endDateQuarter1" : endDateQ1,
                "startDateQuarter2" : startDateQ2,
                "endDateQuarter2" : endDateQ2,
                "startDateQuarter3" : startDateQ3,
                "endDateQuarter3" : endDateQ3,
                "startDateQuarter4": startDateQ4,
                "endDateQuarter4": endDateQ4
    			};

    return json;
};

GDC.bdo.appconfig.notifySuccess = function(successMsg) {
	var successDiv = document.getElementById("form-message");
    successDiv.innerHTML = '<div class="success-msg">'+successMsg+'</div>';
    $("#form-message").show();
};

GDC.bdo.appconfig.notifyError = function(errorMsg) {
	var errDiv = document.getElementById("form-message");
    errDiv.innerHTML = '<div class="error"><span class="error-img">!</span>&nbsp;'+errorMsg+'</div>';
    $("#form-message").show();
};