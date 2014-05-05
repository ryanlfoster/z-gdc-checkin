GDC.bdo.directReports = {};


GDC.bdo.directReports.initTable = function(selector,quarterNumber,annualYear) {

    return $(selector).dataTable({

		"bSort": false,
		"iDisplayLength": 10,
      	"oLanguage": {
   			 "sSearch": ""
		},
        "sAjaxSource": bdoReportServiceSrcPath,
        "aoColumns": [{"mData":"name","sTitle": "Name","sWidth":"25%"},
                      {"mData":"designation","sTitle": "Designation","sWidth":"25%"},
                      {"mData":"employeeID","sTitle": "Employee ID","sWidth":"15%","sClass":"center-aligned"},
                      {"mData":"status","sTitle": "Status","sWidth":"16%","sClass":"center-aligned"},
                      {"mData":"bdoScore","sTitle": "BDO Score(%)","sWidth":"10%","sClass":"center-aligned"},
                      {"mData":"userID","sTitle": "", "sWidth":"10%", "mRender":function(data,type,full){

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
                      }}]
    	});

}

GDC.bdo.directReports.getParamArrayForQueryString = function(paramName, paramArrayValue) {
	var paramString="";
    for(var i=0;i<paramArrayValue.length; i++){
        paramString=paramString+"&amp;"+paramName+"="+encodeURIComponent(paramArrayValue[i]);
    }
    return paramString;
}

GDC.bdo.directReports.stylePagination = function() {

	$('.dataTables_paginate').addClass("pagination green");
    $('.pagination .prev a').html('<i class="icon-caret-left">');
    $('.pagination .next a').html('<i class="icon-caret-right">');

}

GDC.bdo.directReports.addStatusFilterDropdown = function(quarterNumber) {

	$('#bdoReportList'+quarterNumber+'_length').html($('.bdo-status-filter-container'+quarterNumber).html());
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

