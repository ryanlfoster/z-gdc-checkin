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

      //TO-DO
       $('.btn-download-report${quarterNumber}').click( function() { 
			var sData = oTable.$('input').serialize();
			console.log( "The following data would have been submitted to the server: \n\n"+sData );
			return false;
		}); 
      
       $(".fancybox").fancybox({ 
           afterClose : function() {
           oTable.fnReloadAjax();

          }
		});


});

</script>