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
                        <th>% Achieved</th>
                        <th>Status</th>
                        <th>Action</th>
                    </tr>
                </thead>
                <tbody>
                </tbody>
            </table>

    </div>
</div>

<script>
 $(document).ready(function() {
 	var oTable =  GDC.bdo.directReports.initTable("#bdoReportList${quarterNumber}");

	$('#bdoReportList${quarterNumber}_length').html($('.bdo-status-filter-container${quarterNumber}').html());

    $('.dataTables_paginate').addClass("pagination green");

     $('.pagination .prev a').html('<i class="icon-caret-left">');
      $('.pagination .next a').html('<i class="icon-caret-right">');

    $('.bdo-status-filter${quarterNumber}').change(function () {

        if($(this).val() != "") {
            oTable.fnFilter("^"+$(this).val()+"$", 4, true);
        } else {
            oTable.fnFilter('', 4, true);
        }
    });
});
</script>