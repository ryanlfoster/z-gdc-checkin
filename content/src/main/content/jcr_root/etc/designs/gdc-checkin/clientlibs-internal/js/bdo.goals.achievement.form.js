GDC = {};
GDC.bdo = {};

GDC.bdo.isEmpty = function(value) {
    return (value == undefined || value == null || value == "" || value.length == 0);

};

GDC.bdo.form = function(bdoObjectives,bdoAchievements) {

	GDC.bdo.form.multifield('.bdo-objective-panel','.bdo-objective-active'); 
    GDC.bdo.form.edit(bdoObjectives, 'objective', '.bdo-objective-panel .bdo-objective-active');

    GDC.bdo.form.multifield('.bdo-achievement-panel','.bdo-achievement-active');
    GDC.bdo.form.edit(bdoAchievements, 'achievement', '.bdo-achievement-panel .bdo-achievement-active');


	$(".bdo-form").on("click", ".btn-save",function(){

        var validateForm = validateOnSave();
		var buttonLabel = $(this).html();

        if(validateForm == true) {
            disableForm(this);
        	saveOrSubmitBDO("save");

        }
        enableForm(this, buttonLabel);

    });  

	$(".bdo-form").on("click", ".btn-submit",function(){

        var validateForm = validateOnSubmit();
        var buttonLabel = $(this).html();

        if(validateForm == true) {
			disableForm(this);
            saveOrSubmitBDO("submit");
        }
        enableForm(this, buttonLabel);
    });  

    var disableForm = function(button) {
		$(button).text('Please wait Processing');
		$('#quarterly-bdo-form').find('input, textarea, button, select').attr('disabled',true);
    };

    var enableForm = function(button, buttonLabel) {
		$(button).text(buttonLabel);
        $('#quarterly-bdo-form').find('input, textarea, button, select').attr('disabled',false);
    };

    var saveOrSubmitBDO = function(selector) {

		 var targetURL = $('#quarterly-bdo-form').attr("action")+ "." +selector+ ".html?nocache=1";
         var requestType= $('#quarterly-bdo-form').attr("method");
		 var requestParams = getRequestParams();

         $.ajax({
        	url: targetURL,
            type: requestType,
            data: requestParams,

            success: function (data) {
            	
            	if(data.success == "true") {
            		if(selector == "save") {
            			notifySuccess("Saved Successfully !");
            		}
            		else if(selector == "submit") {
            			notifySuccess("Submitted Successfully !");
            		}
            		
            		//Refresh the BDO Achievement tracker to reflect new values
            		GDC.bdo.achievement.tracker(requestParams.percentageAchieved);
            	}
            	else {

					notifyError("Unable to process your request due to an unknown technical error. Please try after sometime");
                }
               
            },

            error: function (jqXHR, textStatus, errorThrown) {

            }
        });

    };



    var getRequestParams = function() {
		var objectives  = getObjectives().toString();
        var achievements = getAchievements().toString();
        var percentageAchieved = $('.quarterly-bdo-form #percentageAchieved').val();
        var designation = $('.quarterly-bdo-form #designation').val();
		var quarterNumber = $('.quarterly-bdo-form #quarterNumber').val();

        var json = {"objectives" : objectives,
                    "achievements" : achievements,
                    "percentageAchieved" : percentageAchieved,
                    "designation" : designation,
                    "quarterNumber" : quarterNumber
        			};

        return json;
    };

    var getObjectives = function() {
		var objectives = [];
		$('.quarterly-bdo-form .bdo-objective-panel').find('textarea').each(function() {
            var value = $(this).val();
    		if (!GDC.bdo.isEmpty(value.trim())) {	
				objectives.push(value);
            }
		});

        return objectives;
    };


	var getAchievements = function() {
		var achievements = [];
		$('.quarterly-bdo-form .bdo-achievement-panel').find('textarea').each(function() {
            var value = $(this).val();
    		if (!GDC.bdo.isEmpty(value.trim())) {
				achievements.push(value);
            }
		});

        return achievements;
    };


    var notifyError = function(errorMsg) {
		var errDiv = document.getElementById("form-message");
        errDiv.innerHTML = '<div class="error"><span class="error-img">!</span>&nbsp;'+errorMsg+'</div>';
        $("#form-message").show();

    };

     var notifySuccess = function(successMsg) {
		var successDiv = document.getElementById("form-message");
        successDiv.innerHTML = '<div class="success-msg">'+successMsg+'</div>';
        $("#form-message").show();

    };

    var validateOnSave = function() {

		var errorMsg="";

		if(GDC.bdo.isEmpty(getObjectives()) ) {
        	errorMsg += 'Please set your BDO Objective !<br>';
        }

        if(errorMsg != "") {
			notifyError(errorMsg);
            return false;
        }
        else {
			$("#form-message").hide();
            return true;
        }


    };

	var validateOnSubmit = function() {
		var errorMsg="";

        if(GDC.bdo.isEmpty(getObjectives()) ) {
        	errorMsg += 'Please set your BDO Objective !<br>';
        }

        if(GDC.bdo.isEmpty(getAchievements()) ) {
        	errorMsg += 'Please set your BDO Achievement self-inputs !<br>';
        }

        if(GDC.bdo.isEmpty($('.quarterly-bdo-form #percentageAchieved').val()) ) {
        	errorMsg += 'Set BDO Percentage Achieved !<br>';
        }


        if(errorMsg != "") {
			notifyError(errorMsg);            
            return false;
        }
		 else {
			$("#form-message").hide();
             return true;
        }


    };

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

         $(activeSelector).find("textarea").each(function(){            
             UpdateId(this);
         });

     });

    $(panelSelector).on("click", ".btn-remove",function(){
        var panelRow = panelSelector+ "-row";
        $(this).parents(panelRow).remove();

        if($(panelSelector).find(panelRow).length == 1){
            $(panelSelector).find(panelRow).find(".btn-remove").hide();
        }
    });

}

GDC.bdo.form.edit = function(valueString, field, activePanelSelector) {

    var valueArray = valueString.split(",");
	document.getElementById(field+"_1").value=valueArray[0];

	for(var i=1;i<valueArray.length;i++){
		$(activePanelSelector).find(".btn-add").trigger("click");
        document.getElementById(field+"_"+(i+1)).value=valueArray[i];
	}

}

