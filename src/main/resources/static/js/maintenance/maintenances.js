// Call the dataTables jQuery plugin
$(document).ready(function () {
    $('#dataTable').DataTable();
    $('#dataTable #dBtn').on('click', function (event) {
        event.preventDefault();
        var href = $(this).attr('href');
        $('#deleteModal #delRef').attr('href', href);
        $('#deleteModal').modal();
    });
    $('#dataTable #vBtn').on('click', function (event) {
        event.preventDefault();
        var description = $(this).val();

        $('#text_description').text(description)
        $('#viewModal').modal();
    });
    $('#dataTable #eBtn').on('click', function (event) {
        event.preventDefault();
        var href = $(this).attr('href');
        $.get(href, function (maintenance, status) {
            $('#idmaintenance').text(maintenance.id)
            $('#Status').val(maintenance.status)
            document.getElementById("editform").action = '/maintenances/updatemaintenance?id=' + maintenance.id;
            $('#EditUserModal').modal();
        })

    });
    window.setTimeout(function () {
        $('.alert').fadeTo(500, 0).slideUp(500, function () {
            $(this).hide();
        });
    }, 7500);
});
