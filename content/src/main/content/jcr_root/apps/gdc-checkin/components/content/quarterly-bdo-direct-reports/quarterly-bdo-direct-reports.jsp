<%@ page contentType="text/html; charset=utf-8" %>
<%@include file="/libs/foundation/global.jsp" %>
<%@page session="false" %>

<div class="well">

    <div class="col-xs-6 table-filter">
        <div id="bdoReportList${quarterNumber}_length" class="dataTables_length">
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
                        <th></th>
                        <th>Name</th>
                        <th>Designation</th>
                        <th>Employee ID</th>
                        <th>Manager</th>
                        <th>Status</th>
                        <th>BDO Score(%)</th>
                        <th></th>
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
    var oTableStore = {};

 $(document).ready(function() {
     var managersID = '${managersID}';
     var quarterNumber = '${quarterNumber}';
     var annualYear = '${annualYear}';
     bdoReportServiceSrcPath = '<%=currentPage.getPath()%>.bdoreport.html?managersID='+managersID+'&quarterNumber='+quarterNumber+'&annualYear='+annualYear;

     if( managersID!=undefined && managersID != null && managersID != "" ) {
         var oTable =  GDC.bdo.directReports.initTable("#bdoReportList${quarterNumber}",quarterNumber,annualYear);
         oTableStore[quarterNumber]= oTable;

        //Filter the datatable based on status coloumn- which is the 4th column in the table
        $('.bdo-status-filter${quarterNumber}').change(function () {
            if($(this).val() != "") {
                oTable.fnFilter("^"+$(this).val()+"$", 5, true);
            } else {
                //Reset the filter
                oTable.fnFilter('', 5, true);
            }
        });

        $('.btn-download-report${quarterNumber}').click( function() { 
            var url = "<%=currentPage.getPath()%>.managerreport.html?managersID="+managersID+"&quarterNumber="+quarterNumber+"&annualYear="+annualYear;
            $.fileDownload(url, {  
                            successCallback: function (url) {  
                                alert('Report download successfully'); 
                            },  
                 failCallback: function (html, url) {    
                     location.reload();
                 }
            }); 
         }); 
          
    
        $(".fancybox").fancybox({ 
               'autoDimensions':false,
               'type':'iframe',
               'autoSize':false,
               'width':'750',
               'height':'600',

               helpers   : { 
                   overlay : {closeClick: false} // prevents closing when clicking OUTSIDE fancybox 
               },

               afterClose : function() {
                   //update datatable for all the quarters
                   oTableStore[1].fnReloadAjax();
                   oTableStore[2].fnReloadAjax();
                   oTableStore[3].fnReloadAjax();
                   oTableStore[4].fnReloadAjax();

               }
      });


 $('.table tbody ').off().on('click', 'td.manager', function (event) {

         var row = $(this).closest('tr');
         var table = $(this).closest('tbody');

         var userID = row.children().eq(7).text();

         if(row.hasClass('shown')) { 
            row.removeClass('shown');

             table.find("tr."+userID).each(function() {
                 hideRows(table, this);
             });
         }
        else {
            row.addClass('shown');
									   table.find("tr."+userID).each(function() {
				            $(this).removeClass("hidden");
				            $(this).removeClass("hide-row");
									   });
        }

 });


function hideRows(table, row) {

    if($(row).hasClass("manager")) {
        var userID = $(row).children().eq(7).text();
        $(row).removeClass("shown");
        table.find("tr."+userID).each(function() {
            hideRows(table,this);
        })
    }
    
    $(row).addClass("hidden");

 }

}

});

</script>