GDC.bdo.directReports = {};


GDC.bdo.directReports.initTable = function(selector,quarterNumber,annualYear) {

    return $(selector).dataTable({

        "bFilter": true, 
		"bSort": false,     
		"bPaginate": false,
      	"oLanguage": { 
   			 "sSearch": ""
		},
		// "sAjaxSource": "/etc/designs/gdc-checkin/clientlibs-internal/json/sampleList.json",
        "sAjaxSource": bdoReportServiceSrcPath,
        "aoColumns": [
					  {"mData":"class","sWidth":"5px","sClass": ""},
            		  {"mData":"name","sTitle": "Name","sWidth":"198px"},
                      {"mData":"designation","sTitle": "Designation","sWidth":"178px"},
                      {"mData":"employeeID","sTitle": "Employee ID","sWidth":"103px","sClass":"center-aligned"},
            		  {"mData":"manager","sTitle": "Manager","sWidth":"80px"},
                      {"mData":"status","sTitle": "Status","sWidth":"111px"},
                      {"mData":"bdoScore","sTitle": "BDO Score(%)","sWidth":"102px","sClass":"center-aligned"},
            		  {"mData":"userID","sTitle": "","sWidth":"111px","sClass":"hide-td"},
                      {"mData":"userID","sTitle": "", "sWidth":"18px", "mRender":function(data,type,full){

					  var html = "";

                      var fancyboxSrc="/content/gdc-bdo/en/dynamic-pages/view-quartery-bdo.html?"+
                         			    "userID="+ full.userID +
                      					 GDC.bdo.directReports.getParamArrayForQueryString('bdoObjectives',full.objectives) +
                                         GDC.bdo.directReports.getParamArrayForQueryString('bdoAchievements',full.achievements)+
                      					"&amp;name="+ encodeURIComponent(full.name) +
                      					"&amp;designation="+ encodeURIComponent(full.designation) +
                      					"&amp;quarterNumber="+ quarterNumber +
                      					"&amp;annualYear="+ annualYear + 
                      					"&amp;bdoScore="+ full.bdoScore +
                      					"&amp;status="+ encodeURIComponent(full.status) +
                      					"&amp;editForm=true";

                      html += '<a class="fancybox fancybox.iframe" href='+fancyboxSrc+'>View/Edit</a>';

                        return html;
                      }}],

		 "fnRowCallback": function (nRow, aData, iDisplayIndex) {
             var className = $('td:eq(0)', nRow).text();
            if(className == "") {
                className = $('td:eq(0)', nRow).data('buffer');
            }

            $(nRow).addClass(className);

             if($(nRow).hasClass("shown")) {
				$(nRow).removeClass("shown")
             }

             if(className.indexOf("hide-row") >= 0) {
                 $(nRow).addClass("hidden");
             }

             $('td:eq(0)', nRow).attr('class',className);
        	 $('td:eq(0)', nRow).data('buffer',className);

             if( (($("select.bdo-status-filter"+quarterNumber).val()) != "") || ($('#bdoReportList'+quarterNumber+'_filter input[type="text"]').val()) != "") {
				 $(nRow).removeClass("hidden");

                 if($('td:eq(0)', nRow).hasClass("manager")) {

                     $('td:eq(0)',nRow).removeClass("manager");
                     
                 }

             }

             $('td:eq(0)', nRow).html("");
     	}
    	});

}

GDC.bdo.directReports.getParamArrayForQueryString = function(paramName, paramArrayValue) {
	var paramString="";
    for(var i=0;i<paramArrayValue.length; i++){
        paramString=paramString+"&amp;"+paramName+"="+encodeURIComponent(paramArrayValue[i]);
    }
    return paramString;
}

$.fn.dataTableExt.oApi.fnReloadAjax = function ( oSettings, sNewSource, fnCallback, bStandingRedraw )
{
    if ( typeof sNewSource != 'undefined' && sNewSource != null )
    {
        oSettings.sAjaxSource = sNewSource;
    }
    this.oApi._fnProcessingDisplay( oSettings, true );
    var that = this;
    var iStart = oSettings._iDisplayStart;
     
    oSettings.fnServerData( oSettings.sAjaxSource, [], function(json) {
        /* Clear the old information from the table */
        that.oApi._fnClearTable( oSettings );
         
        /* Got the data - add it to the table */
        for ( var i=0 ; i<json.aaData.length ; i++ )
        {
            that.oApi._fnAddData( oSettings, json.aaData[i] );
        }
         
        oSettings.aiDisplay = oSettings.aiDisplayMaster.slice();
        that.fnDraw();
         
        if ( typeof bStandingRedraw != 'undefined' && bStandingRedraw === true )
        {
            oSettings._iDisplayStart = iStart;
            that.fnDraw( false );
        }
         
        that.oApi._fnProcessingDisplay( oSettings, false );
         
        /* Callback user function - for event handlers etc */
        if ( typeof fnCallback == 'function' && fnCallback != null )
        {
            fnCallback( oSettings );
        }
    }, oSettings );
}

