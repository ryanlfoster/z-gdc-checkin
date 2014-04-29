GDC = {};
GDC.bdo = {};

GDC.bdo.isEmpty = function(value) {
    return (value == undefined || value == null || value == "" || value.length == 0);

};

GDC.bdo.form = function(bdoObjectives,bdoAchievements) {

	GDC.bdo.form.multifield('.bdo-objective-panel','.bdo-objective-active'); 
	
	if(!GDC.bdo.isEmpty(bdoObjectives)) {
		GDC.bdo.form.edit(bdoObjectives, 'objective', '.bdo-objective-panel .bdo-objective-active');
	}
	
    GDC.bdo.form.multifield('.bdo-achievement-panel','.bdo-achievement-active');
    
    if(!GDC.bdo.isEmpty(bdoAchievements)) {
    	GDC.bdo.form.edit(bdoAchievements, 'achievement', '.bdo-achievement-panel .bdo-achievement-active');
    }

}

GDC.bdo.form.detectAnyFormChange = function(bdoObjectives,bdoAchievements,percentageAchieved) {
	if((GDC.bdo.form.unescapeHtml(bdoObjectives.toString()) === GDC.bdo.form.getObjectives().toString()) 
        && (GDC.bdo.form.unescapeHtml(bdoAchievements.toString()) === GDC.bdo.form.getAchievements().toString()) 
        && (percentageAchieved === GDC.bdo.form.getRequestParams().percentageAchieved)) {
            return false;
        }
    else {
		return true;
    }
}

GDC.bdo.form.disableForm = function(button) {
	$(button).text('Please wait Processing');
	$('#quarterly-bdo-form').find('input, textarea, button, select').attr('disabled',true);
}

GDC.bdo.form.enableForm = function(button, buttonLabel) {
	$(button).text(buttonLabel);
    $('#quarterly-bdo-form').find('input, textarea, button, select').attr('disabled',false);
}

GDC.bdo.form.saveOrSubmitBDO = function(selector,plotGraph) {

	 var targetURL = $('#quarterly-bdo-form').attr("action")+ "." +selector+ ".html?nocache=1";
     var requestType= $('#quarterly-bdo-form').attr("method");
	 var requestParams = GDC.bdo.form.getRequestParams();

     $.ajax({
    	url: targetURL,
        type: requestType,
        data: requestParams,

        success: function (data) {

        	if(data.success == true) {
        		if(selector == "save") {
        			GDC.bdo.form.notifySuccess("Saved Successfully !");
        		}
        		else if(selector == "submit") {
        			GDC.bdo.form.notifySuccess("Submitted Successfully !");
        		}
        		
        		 if(plotGraph == "true") {
             		//Refresh the BDO Achievement tracker to reflect new values
             		GDC.bdo.achievement.tracker(requestParams.percentageAchieved);
                 }
                //Refresh the status message
                GDC.bdo.form.displayUpdatedStatus(selector);

                //Update form values
                GDC.bdo.form.updateFormFieldValues(requestParams, selector);
        	}
        	else {

				GDC.bdo.form.notifyError("Unable to process your request due to an unknown technical error. Please try after sometime");
            }
           
        },

        error: function (jqXHR, textStatus, errorThrown) {

        }
    });

}

GDC.bdo.form.updateFormFieldValues = function(updatedFormValues, newStatus) {
	bdoObjectivesArray = updatedFormValues.objectives;
    bdoAchievementsArray = updatedFormValues.achievements;
    percentageAchieved = updatedFormValues.percentageAchieved;

    if(newStatus == "save") {
        status = 'NOT SUBMITTED';
    }
    else if(newStatus == "submit") {
        status = 'SUBMITTED';
    }
}

GDC.bdo.form.displayUpdatedStatus = function(action) {
    var message = (action == "submit") ? "SUBMITTED" : "NOT SUBMITTED";
    $('.quarterly-bdo-form .status-msg .status').html(message);
}

GDC.bdo.form.getRequestParams = function() {
	var objectives  = GDC.bdo.form.getObjectives();
    var achievements = GDC.bdo.form.getAchievements();
    var percentageAchieved = $('.quarterly-bdo-form #percentageAchieved').val();
    var designation = $('.quarterly-bdo-form #designation').val();
	var quarterNumber = $('.quarterly-bdo-form #quarterNumber').val();
	var userID = $('.quarterly-bdo-form #userID').val();
	var annualYear = $('.quarterly-bdo-form #annualYear').val();
	var name = $('.quarterly-bdo-form #name').val();

    var json = {"objectives" : objectives,
                "achievements" : achievements,
                "percentageAchieved" : percentageAchieved,
                "designation" : designation,
                "quarterNumber" : quarterNumber,
                "userID" : userID,
                "annualYear" : annualYear,
                "name": name
    			};

    return json;
}

GDC.bdo.form.getObjectives = function() {
	var objectives = [];
	$('.quarterly-bdo-form .bdo-objective-panel').find('textarea').each(function() {
        var value = $(this).val();
		if (!GDC.bdo.isEmpty(value.trim())) {	
			objectives.push(value);
        }
	});

    return objectives;
}


GDC.bdo.form.getAchievements = function() {
	var achievements = [];
	$('.quarterly-bdo-form .bdo-achievement-panel').find('textarea').each(function() {
        var value = $(this).val();
		if (!GDC.bdo.isEmpty(value.trim())) {
			achievements.push(value);
        }
	});

    return achievements;
}


GDC.bdo.form.notifyError = function(errorMsg) {
	var errDiv = document.getElementById("form-message");
    errDiv.innerHTML = '<div class="error"><span class="error-img">!</span>&nbsp;'+errorMsg+'</div>';
    $("#form-message").show();

}

GDC.bdo.form.notifySuccess = function(successMsg) {
	var successDiv = document.getElementById("form-message");
    successDiv.innerHTML = '<div class="success-msg">'+successMsg+'</div>';
    $("#form-message").show();

}

GDC.bdo.form.validateOnSave = function() {
	var errorMsg="";

	if(GDC.bdo.isEmpty(GDC.bdo.form.getObjectives()) ) {
    	errorMsg += 'Please set BDO Objective !<br>';
    }

    if(errorMsg != "") {
		GDC.bdo.form.notifyError(errorMsg);
        return false;
    }
    else {
		$("#form-message").hide();
        return true;
    }
}

GDC.bdo.form.validateOnSubmit = function() {
	var errorMsg="";

    if(GDC.bdo.isEmpty(GDC.bdo.form.getObjectives()) ) {
    	errorMsg += 'Please set BDO Objective !<br>';
    }

    if(GDC.bdo.isEmpty(GDC.bdo.form.getAchievements()) ) {
    	errorMsg += 'Please set BDO Achievement self-inputs !<br>';
    }

    if(GDC.bdo.isEmpty($('.quarterly-bdo-form #percentageAchieved').val()) ) {
    	errorMsg += 'Please set BDO Percentage Achieved !<br>';
    }


    if(errorMsg != "") {
		GDC.bdo.form.notifyError(errorMsg);            
        return false;
    }
	 else {
		$("#form-message").hide();
         return true;
    }
}


GDC.bdo.form.multifield = function(panelSelector, activeSelector) {

	var UpdateId = function(elem) {
        var id = $(elem).attr("id");
        var increment = parseInt(id.split("_")[1]) + 1;
        var newId = id.split("_")[0] + "_" + increment;
        
        $(elem).attr("id", newId);
        $(elem).val("");
        
    };

    $(panelSelector).on("click", ".btn-add",function(){

         var clonedRow = $(activeSelector).clone();

         $(activeSelector).find(".btn-remove").show();
         $(activeSelector).find(".btn-add").hide();
         $(activeSelector).removeClass(activeSelector.substring(1)); 

         $(clonedRow).appendTo(panelSelector);

         $(activeSelector).find("textarea").each(function() {            
             UpdateId(this);
         });

     });

    $(panelSelector).on("click", ".btn-remove",function() {
        var panelRow = panelSelector+ "-row";
        $(this).parents(panelRow).remove();

        if($(panelSelector).find(panelRow).length == 1){
            $(panelSelector).find(panelRow).find(".btn-remove").hide();
        }
    });

}

GDC.bdo.form.edit = function(valueArray, field, activePanelSelector) {

	document.getElementById(field+"_1").value=GDC.bdo.form.unescapeHtml(valueArray[0]);

	for(var i=1;i<valueArray.length;i++){
		$(activePanelSelector).find(".btn-add").trigger("click");
        document.getElementById(field+"_"+(i+1)).value=GDC.bdo.form.unescapeHtml(valueArray[i]);
	}

}

GDC.bdo.form.unescapeHtml = function(safe) {
        return safe.replace(/&amp;/g, '&')
        		   .replace(/&lt;/g, '<')
        		   .replace(/&gt;/g, '>')
        		   .replace(/&#034;/g, '"')
                   .replace(/&#039;/g, "'");
 }
