
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

GDC.bdo.form = function(bdoObjectives,bdoAchievements,quarterNumber) {

	GDC.bdo.form.multifield('.'+quarterNumber +'-bdo-panel','.'+quarterNumber+'-bdo-active',quarterNumber); 

   if(!GDC.bdo.isEmpty(bdoObjectives)  || !GDC.bdo.isEmpty(bdoAchievements)) {
		GDC.bdo.form.addBdoRows(bdoObjectives, bdoAchievements, '.'+quarterNumber +'-bdo-panel .'+quarterNumber+'-bdo-active' );
   }

	if(!GDC.bdo.isEmpty(bdoObjectives) ) {
		GDC.bdo.form.addFieldValue(bdoObjectives, quarterNumber+'-objective');
	}

    if(!GDC.bdo.isEmpty(bdoAchievements)) {
    	GDC.bdo.form.addFieldValue(bdoAchievements, quarterNumber+'-achievement');
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


GDC.bdo.form.detectAnyFormChange = function(bdoObjectives,bdoAchievements,employeeID,quarterNumber) {


	if((GDC.bdo.form.compareArrays(bdoObjectives, GDC.bdo.form.getObjectives(quarterNumber))) 
        && (GDC.bdo.form.compareArrays(bdoAchievements, GDC.bdo.form.getAchievements(quarterNumber)))
        && (employeeID == GDC.bdo.form.getRequestParams(quarterNumber).employeeID)) {
            return false;
        }
    else {
		return true;
    }
}


GDC.bdo.form.detectAnyFormChangeOnComplete = function(bdoObjectives,bdoAchievements,bdoScore,quarterNumber) {

	if((GDC.bdo.form.compareArrays(bdoObjectives, GDC.bdo.form.getObjectives(quarterNumber))) 
        && (GDC.bdo.form.compareArrays(bdoAchievements, GDC.bdo.form.getAchievements(quarterNumber))) 
        && (bdoScore === GDC.bdo.form.getRequestParams(quarterNumber).bdoScore)) {
            return false;
        }
    else {
		return true;
    }
}

GDC.bdo.form.disableForm = function(button,quarterNumber) {
	$(button).text('Please wait Processing');
	$('#quarterly-bdo-form-'+quarterNumber).find('input, textarea, button, select').attr('disabled',true);
}

GDC.bdo.form.enableForm = function(button, buttonLabel,quarterNumber) {
	$(button).text(buttonLabel);
    $('#quarterly-bdo-form-'+quarterNumber).find('input, textarea, button, select').attr('disabled',false);
}

GDC.bdo.form.saveSubmitOrCompleteBDO = function(selector,quarterNumber) {

	 var targetURL = $('#quarterly-bdo-form-'+quarterNumber).attr("action")+ "." +selector+ ".html?nocache=1";
     var requestType= $('#quarterly-bdo-form-'+quarterNumber).attr("method");
	 var requestParams = GDC.bdo.form.getRequestParams(quarterNumber);

     $.ajax({
    	url: targetURL,
        type: requestType,
        data: requestParams,

        success: function (data) {

        	if(data.success == true) {
        		if(selector == "save") {
        			GDC.bdo.form.notifySuccess("Saved Successfully !",quarterNumber);
        		}
        		else if(selector == "submit") {
        			GDC.bdo.form.notifySuccess("Submitted Successfully !",quarterNumber);
        		}
        		else if(selector == "complete") {
        			GDC.bdo.form.notifySuccess("Completed Successfully !",quarterNumber);
        		}
        		
                //Refresh the status message
                GDC.bdo.form.displayUpdatedStatus(selector,quarterNumber);

                //Update form values
                GDC.bdo.form.updateFormFieldValues(requestParams, selector, quarterNumber);
        	}
        	else {
        		
        		if(data.error == "invalid_session") {
        			location.reload();
        		}
        		
				GDC.bdo.form.notifyError("Unable to process your request due to an unknown technical error. Please try after sometime",quarterNumber);
            }
           
        },

        error: function (jqXHR, textStatus, errorThrown) {

        }
    });

}

GDC.bdo.form.updateFormFieldValues = function(updatedFormValues, newStatus, quarterNumber) {
	objectiveStorage[quarterNumber] = updatedFormValues.objectives;
    achievementStorage[quarterNumber] = updatedFormValues.achievements;
    bdoScoreStorage[quarterNumber] = updatedFormValues.bdoScore;
    employeeID = updatedFormValues.employeeID;

    if(newStatus == "save") {
        statusStorage[quarterNumber] = 'NOT SUBMITTED';
    }
    else if(newStatus == "submit") {
        statusStorage[quarterNumber] = 'SUBMITTED';
    } 
    else if(newStatus == "complete") {
        statusStorage[quarterNumber] = 'COMPLETED';
    }
}

GDC.bdo.form.displayUpdatedStatus = function(action,quarterNumber) {
    var message = (action == "submit") ? "SUBMITTED" : (action == "complete") ? "COMPLETED" : "NOT SUBMITTED";
    $('.quarterly-bdo-form-'+quarterNumber+' .status-msg .status').html(message);
}

GDC.bdo.form.getRequestParams = function(quarterNumber) {
	var objectives  = GDC.bdo.form.getObjectives(quarterNumber);
    var achievements = GDC.bdo.form.getAchievements(quarterNumber);
    var bdoScore = $('.quarterly-bdo-form-'+quarterNumber+' #bdoScore').val();
    var designation = $('.quarterly-bdo-form-'+quarterNumber+' #designation').val();
	var userID = $('.quarterly-bdo-form-'+quarterNumber+' #userID').val();
	var annualYear = $('.quarterly-bdo-form-'+quarterNumber+' #annualYear').val();
	var name = $('.quarterly-bdo-form-'+quarterNumber+' #name').val();
	var employeeID = $('.quarterly-bdo-form-'+quarterNumber+' #employeeID').val();

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

GDC.bdo.form.getObjectives = function(quarterNumber) {
	var objectives = [];
	$('.quarterly-bdo-form-'+quarterNumber +' .'+quarterNumber+'-bdo-panel .objective').each(function() {
        var value = $(this).val();

        if (!GDC.bdo.isEmpty(value)) {
			value = value.trim();
        }

        objectives.push(value);

	});

    return objectives;
}


GDC.bdo.form.getAchievements = function(quarterNumber) {
	var achievements = [];
	$('.quarterly-bdo-form-'+quarterNumber +' .'+quarterNumber+'-bdo-panel .achievement').each(function() {
        var value = $(this).val();


		if (!GDC.bdo.isEmpty(value)) {
			value = value.trim();
        }
        achievements.push(value);

	});

    return achievements;
}


GDC.bdo.form.notifyError = function(errorMsg,quarterNumber) {
	var errDiv = document.getElementById("form-message-"+quarterNumber);
    errDiv.innerHTML = '<div class="error"><span class="error-img">!</span>&nbsp;'+errorMsg+'</div>';
    $("#form-message-"+quarterNumber).show();

}

GDC.bdo.form.notifySuccess = function(successMsg,quarterNumber) {
	var successDiv = document.getElementById("form-message-"+quarterNumber);
    successDiv.innerHTML = '<div class="success-msg">'+successMsg+'</div>';
    $("#form-message-"+quarterNumber).show();

}


GDC.bdo.form.clearMessages = function(quarterNumber) {
	$("#form-message-"+quarterNumber).hide();
}


GDC.bdo.form.validateOnSave = function(quarterNumber) {
	var errorMsg="";

	if(GDC.bdo.isEmptyArray(GDC.bdo.form.getObjectives(quarterNumber)) ) {
    	errorMsg += 'Please set BDO Objective !<br>';
    }

    if(GDC.bdo.isEmpty($('.quarterly-bdo-form-'+quarterNumber+' #employeeID').val()) ) {
    	errorMsg += 'Please enter your employeeID !<br>';
    }

    if(errorMsg != "") {
		GDC.bdo.form.notifyError(errorMsg,quarterNumber);
        return false;
    }
    else {
		$("#form-message-"+quarterNumber).hide();
        return true;
    }
}

GDC.bdo.form.validateOnSubmit = function(quarterNumber) {
	var errorMsg="";

    if(GDC.bdo.isEmptyArray(GDC.bdo.form.getObjectives(quarterNumber)) ) {
    	errorMsg += 'Please set BDO Objective !<br>';
    }

    if(GDC.bdo.isEmptyArray(GDC.bdo.form.getAchievements(quarterNumber)) ) {
    	errorMsg += 'Please set BDO Achievement self-inputs !<br>';
    }

	if(GDC.bdo.isEmpty($('.quarterly-bdo-form-'+quarterNumber+' #employeeID').val()) ) {
    	errorMsg += 'Please enter your employeeID !<br>';
    }
    if(errorMsg != "") {
		GDC.bdo.form.notifyError(errorMsg,quarterNumber);            
        return false;
    }
	 else {
		$("#form-message-"+quarterNumber).hide();
         return true;
    }
}


GDC.bdo.form.validateOnComplete = function(quarterNumber) {
	var errorMsg="";

    if(GDC.bdo.isEmpty($('.quarterly-bdo-form-'+quarterNumber+' #bdoScore').val()) ) {
    	errorMsg += 'Please provide your score !<br>';
    }
    
    if(($('.quarterly-bdo-form-'+quarterNumber+' #bdoScore').val() < 0 ) ||  ($('.quarterly-bdo-form-'+quarterNumber+' #bdoScore').val() >100)) {
    	errorMsg += 'Please enter a valid BDO Score (Between 0 to 100)<br>';
    }
    
    if(errorMsg != "") {
		GDC.bdo.form.notifyError(errorMsg,quarterNumber);            
        return false;
    }
	 else {
		$("#form-message-"+quarterNumber).hide();
         return true;
    }
}


GDC.bdo.form.multifield = function(panelSelector, activeSelector, quarterNumber) {

		var UpdateId = function(elem) {
        var id = $(elem).attr("id");
        var increment = parseInt(id.split("_")[1]) + 1;
        var newId = id.split("_")[0] + "_" + increment;
        
        $(elem).attr("id", newId);
        $(elem).val("");
        
    };

    $(panelSelector).on("click", ".btn-add",function(){

    	 GDC.bdo.form.clearMessages(quarterNumber);
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
    	
    	GDC.bdo.form.clearMessages(quarterNumber);
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
                   .replace(/&#039;/g, "'")
                   .replace(/<br>/g,"\n");
 }
