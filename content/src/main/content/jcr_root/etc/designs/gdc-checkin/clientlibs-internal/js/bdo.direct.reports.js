GDC.bdo.directReports = {};


GDC.bdo.directReports.initTable = function(selector) {

    return $(selector).dataTable({

		"bSort": false,
		"iDisplayLength": 10,
      	"oLanguage": {
   			 "sSearch": ""
		},
        "sAjaxSource": bdoReportServiceSrcPath,
        "aoColumns": [{"mData":"name","sTitle": "Name"},
                      {"mData":"designation","sTitle": "Designation"},
                      {"mData":"employeeID","sTitle": "Employee ID"},
                      {"mData":"percentageAchieved","sTitle": "% Achieved"},
                      {"mData":"status","sTitle": "Status"},
                      {"mData":"userID","sTitle": "", "mRender":function(data,type,full){
                        var html = "";

                        html += '<a href="javascript:void(0);" onclick="viewObjective(\'' + full.title + '\',\'' + full.summary + '\');return false;">View/Edit</a></li>';

                        return html;
                      }}]
    	});

}

GDC.bdo.directReports.stylePagination = function() {

	$('.dataTables_paginate').addClass("pagination green");
    $('.pagination .prev a').html('<i class="icon-caret-left">');
    $('.pagination .next a').html('<i class="icon-caret-right">');

}

GDC.bdo.directReports.addStatusFilterDropdown = function(quarterNumber) {

	$('#bdoReportList'+quarterNumber+'_length').html($('.bdo-status-filter-container'+quarterNumber).html());
}
