<%@ page contentType="text/html; charset=utf-8" %>
<%@include file="/libs/foundation/global.jsp" %>
<%@page session="false" %>

<div class="well">
    <div class="row">
        <div class="col-md-3 col-xs-3 bdo-status-filter-container${quarterNumber}" style="display:none">
            <select class="form-control bdo-status-filter${quarterNumber}">
                 <option value="">All</option>
                <option value="SUBMITTED">Submitted</option>
                <option value="NOT SUBMITTED">Not Submitted</option>
                <option value="COMPLETED">Completed</option>
            </select>
        </div>
    </div>

    <div class="row">
            <table id="bdoReportList${quarterNumber}" class="table table-bordered table-striped table-cth green">
                <thead>
                    <tr>
                        <th>Name</th>
                        <th>Designation</th>
                        <th>Employee ID</th>
                        <th>Status</th>
                        <th>BDO Score(%)</th>
                        <th>Action</th>
                    </tr>
                </thead>
                <tbody>
                </tbody>
            </table>
    </div>

    <button class="btn btn-success btn-download-report${quarterNumber}">Download Report</button>
</div>


<script>

	var bdoReportServiceSrcPath = '';

	$(document).ready(function() {
		var managersID = '${managersID}';
   		var quarterNumber = '${quarterNumber}';
   		var annualYear = '${annualYear}';
   		bdoReportServiceSrcPath = '<%=currentPage.getPath()%>.bdoreport.html?managersID='+managersID+'&quarterNumber='+quarterNumber+'&annualYear='+annualYear;

   		var oTable =  GDC.bdo.directReports.initTable("#bdoReportList${quarterNumber}",quarterNumber,annualYear);

	   	GDC.bdo.directReports.addStatusFilterDropdown(quarterNumber);
	   	GDC.bdo.directReports.stylePagination();

	   //Filter the datatable based on status coloumn- which is the 4th column in the table
	   $('.bdo-status-filter${quarterNumber}').change(function () {
	       if($(this).val() != "") {
	           oTable.fnFilter("^"+$(this).val()+"$", 3, true);
	       } else {
	           //Reset the filter
	           oTable.fnFilter('', 3, true);
	       }
	   });

	   $('.btn-download-report${quarterNumber}').click( function() { 
		      var url = "<%=currentPage.getPath()%>.managerreport.html?managersID="+managersID+"&quarterNumber="+quarterNumber+"&annualYear="+annualYear;
        $.fileDownload(url, {  
				         successCallback: function (url) {  
				             alert('Report download successfully'); 
				         },  
             failCallback: function (html, url) {    
                 alert('Your file download just failed for this URL:' + url + '\r\n' +     
                       'Here was the resulting error HTML: \r\n' + html);    
             }
		      }); 
		   }); 
      

	   $(".fancybox").fancybox({ 
           'autoDimensions':false,
           'type':'iframe',
           'autoSize':false,

           helpers   : { 
               overlay : {closeClick: false} // prevents closing when clicking OUTSIDE fancybox 
           },

           beforeLoad : function() {                    
               this.width = $(window).width()*0.40;  
               this.height = $(window).height()*0.60; 
           },
           afterClose : function() {
			        	   if(quarterNumber == 4) {
			        		    location.reload();
			            }
          }
		});


});

</script>