GDC = {};
GDC.bdo = {};

GDC.bdo.isEmpty = function(value) {
    return (value == undefined || value == null || value == "" || value.length == 0);

};

GDC.bdo.isEmptyArray = function(array) {

    if(GDC.bdo.isEmpty(array))  return true;

    for(var i=0; i<array.length; i++) {
		if(array[i] != '') return false; 
    }

    return true;
}

GDC.bdo.form = function(bdoObjectives,bdoAchievements) {

	GDC.bdo.form.multifield('.bdo-panel','.bdo-active'); 

   if(!GDC.bdo.isEmpty(bdoObjectives)  || !GDC.bdo.isEmpty(bdoAchievements)) {
		GDC.bdo.form.addBdoRows(bdoObjectives, bdoAchievements, '.bdo-panel .bdo-active' );
   }
	
	if(!GDC.bdo.isEmpty(bdoObjectives) ) {
		GDC.bdo.form.addFieldValue(bdoObjectives, 'objective');
	}

    if(!GDC.bdo.isEmpty(bdoAchievements)) {
    	GDC.bdo.form.addFieldValue(bdoAchievements, 'achievement');
    }

}

GDC.bdo.form.compareArrays = function(array1, array2) {

	var array1Length, array2Length, maxLength;
	array1Length = array2Length = maxLength = 0;

     if(!GDC.bdo.isEmpty(array1)) {
		array1Length = array1.length;
     }
     if(!GDC.bdo.isEmpty(array2)) {
		array2Length = array2.length;
     }

    if( array1Length == 0 && array2Length == 0 ) {
        return true;
    }

    maxLength = Math.max(array1Length,array2Length);

    for(var i=0; i< maxLength; i++) {

        if( (i < array1Length)  && (i < array2Length) ) {

            if(GDC.bdo.form.unescapeHtml(array1[i]) !== array2[i]) return false;

        }
        else if(i < array1Length) {
            if(GDC.bdo.form.unescapeHtml(array1[i]) != '') return false;
        } 
        else {
			if(array2[i] != '') return false;
        }
    }
    return true;
}


GDC.bdo.form.detectAnyFormChange = function(bdoObjectives,bdoAchievements,employeeID) {

	if((GDC.bdo.form.compareArrays(bdoObjectives, GDC.bdo.form.getObjectives())) 
        && (GDC.bdo.form.compareArrays(bdoAchievements, GDC.bdo.form.getAchievements()))
        && (employeeID == GDC.bdo.form.getRequestParams().employeeID)) {
            return false;
        }
    else {
		return true;
    }
}


GDC.bdo.form.detectAnyFormChangeOnComplete = function(bdoObjectives,bdoAchievements,bdoScore) {

	if((GDC.bdo.form.compareArrays(bdoObjectives, GDC.bdo.form.getObjectives())) 
        && (GDC.bdo.form.compareArrays(bdoAchievements, GDC.bdo.form.getAchievements())) 
        && (bdoScore === GDC.bdo.form.getRequestParams().bdoScore)) {
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

GDC.bdo.form.saveSubmitOrCompleteBDO = function(selector) {

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
        		else if(selector == "complete") {
        			GDC.bdo.form.notifySuccess("Completed Successfully !");
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
    bdoScore = updatedFormValues.bdoScore;
    employeeID = updatedFormValues.employeeID;

    if(newStatus == "save") {
        status = 'NOT SUBMITTED';
    }
    else if(newStatus == "submit") {
        status = 'SUBMITTED';
    } 
    else if(newStatus == "complete") {
        status = 'COMPLETED';
    }
}

GDC.bdo.form.displayUpdatedStatus = function(action) {
    var message = (action == "submit") ? "SUBMITTED" : (action == "complete") ? "COMPLETED" : "NOT SUBMITTED";
    $('.quarterly-bdo-form .status-msg .status').html(message);
}

GDC.bdo.form.getRequestParams = function() {
	var objectives  = GDC.bdo.form.getObjectives();
    var achievements = GDC.bdo.form.getAchievements();
    var bdoScore = $('.quarterly-bdo-form #bdoScore').val();
    var designation = $('.quarterly-bdo-form #designation').val();
	var quarterNumber = $('.quarterly-bdo-form #quarterNumber').val();
	var userID = $('.quarterly-bdo-form #userID').val();
	var annualYear = $('.quarterly-bdo-form #annualYear').val();
	var name = $('.quarterly-bdo-form #name').val();
	var employeeID = $('.quarterly-bdo-form #employeeID').val();

    var json = {"objectives" : objectives,
                "achievements" : achievements,
                "bdoScore" : bdoScore,
                "designation" : designation,
                "quarterNumber" : quarterNumber,
                "userID" : userID,
                "annualYear" : annualYear,
                "name": name,
                "employeeID": employeeID
    			};

    return json;
}

GDC.bdo.form.getObjectives = function() {
	var objectives = [];
	$('.quarterly-bdo-form .bdo-panel .objective').each(function() {
        var value = $(this).val();

        if (!GDC.bdo.isEmpty(value)) {
			value = value.trim();
        }

        objectives.push(value);

	});

    return objectives;
}


GDC.bdo.form.getAchievements = function() {
	var achievements = [];
	$('.quarterly-bdo-form .bdo-panel .achievement').each(function() {
        var value = $(this).val();


		if (!GDC.bdo.isEmpty(value)) {
			value = value.trim();
        }
        achievements.push(value);

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

	if(GDC.bdo.isEmptyArray(GDC.bdo.form.getObjectives()) ) {
    	errorMsg += 'Please set BDO Objective !<br>';
    }

    if(GDC.bdo.isEmpty($('.quarterly-bdo-form #employeeID').val()) ) {
    	errorMsg += 'Please enter your employeeID !<br>';
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

    if(GDC.bdo.isEmptyArray(GDC.bdo.form.getObjectives()) ) {
    	errorMsg += 'Please set BDO Objective !<br>';
    }

    if(GDC.bdo.isEmptyArray(GDC.bdo.form.getAchievements()) ) {
    	errorMsg += 'Please set BDO Achievement self-inputs !<br>';
    }

	if(GDC.bdo.isEmpty($('.quarterly-bdo-form #employeeID').val()) ) {
    	errorMsg += 'Please enter your employeeID !<br>';
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


GDC.bdo.form.validateOnComplete = function() {
	var errorMsg="";

    if(GDC.bdo.isEmptyArray(GDC.bdo.form.getObjectives()) ) {
    	errorMsg += 'Please set BDO Objective !<br>';
    }

    if(GDC.bdo.isEmptyArray(GDC.bdo.form.getAchievements()) ) {
    	errorMsg += 'Please set BDO Achievement self-inputs !<br>';
    }

    if(GDC.bdo.isEmpty($('.quarterly-bdo-form #bdoScore').val()) ) {
    	errorMsg += 'Please provide your score !<br>';
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


GDC.bdo.form.addBdoRows = function(bdoObjectives, bdoAchievements, activePanelSelector) {

    var numberOfBdoRows, objectiveArraySize, achievementArraySize;
	numberOfBdoRows = objectiveArraySize = achievementArraySize = 0;

    if(!GDC.bdo.isEmpty(bdoObjectives)) {
		objectiveArraySize = bdoObjectives.length;
    }
	if(!GDC.bdo.isEmpty(bdoAchievements)) {
		achievementArraySize = bdoAchievements.length;
    }

	numberOfBdoRows = Math.max(objectiveArraySize, achievementArraySize);

    for(var i=1;i<numberOfBdoRows;i++){
		$(activePanelSelector).find(".btn-add").trigger("click");
    }
}


GDC.bdo.form.addFieldValue = function(valueArray, field) {

	document.getElementById(field+"_1").value=GDC.bdo.form.unescapeHtml(valueArray[0]);

	for(var i=1;i<valueArray.length;i++) {
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
