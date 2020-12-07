var ctx;

function updateFilteredTable() {
    $.ajax({
        type: "GET",
        url: "profile/meals/filter",
        data: $("#filter").serialize()
    }).done(updateTableByData);
}

$(function (){
    ctx = {
        ajaxUrl: "profile/meals/",
        datatableApi: $("#dataTable").DataTable({
            "paging": false,
            "info": true,
            "columns": [
                {
                    "data":"dateTime"
                },
                {
                    "data":"description"
                },
                {
                    "data":"calories"
                },
                {
                    "defaultContent":"Edit",
                    "orderable": false
                },
                {
                    "defaultContent":"Delete",
                    "orderable": false
                }

            ],
            "order":[
                [
                    0,
                    "desc"
                ]
            ]
        }),
        updateTable: updateFilteredTable()
    };
    makeEditable();
});

function clearFilter(){

}