GDC.bdo.directReports = {};


GDC.bdo.directReports.initTable = function(selector) {

    return $(selector).dataTable({

		"bSort": false,
		"iDisplayLength": 10,
      	"oLanguage": {
   			 "sSearch": ""
		},
        "sAjaxSource": "http://localhost:4502/etc/designs/gdc-checkin/clientlibs-internal/json/sampleList.json",
        "aoColumns": [{"mData":"name","sTitle": "Name"},
                      {"mData":"designation","sTitle": "Designation"},
                      {"mData":"employeeid","sTitle": "Employee ID"},
                      {"mData":"percent","sTitle": "% Achieved"},
                      {"mData":"status","sTitle": "Status"},
                      {"mData":"ldap","sTitle": "", "mRender":function(data,type,full){
                        var html = "";

                        html += '<a href="javascript:void(0);" onclick="viewObjective(\'' + full.title + '\',\'' + full.summary + '\');return false;">View</a></li>';

                        return html;
                      }}]
    	});

};
