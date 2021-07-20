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
        var Address = $(this).val();
        $('#text_Address').text(Address);
        $('#viewModal').modal();
    });
    $('#dataTable #eBtn').on('click', function (event) {
        event.preventDefault();
        var href = $(this).attr('href');
        $.get(href, function (Employee, status) {
            $('#idemployee').text(Employee.id)
            $('#firstName').val(Employee.firstName)
            $('#lastName').val(Employee.lastName)
            $('#salary').val(Employee.salary)
            $('#Position').val(Employee.positions.id)
            $('#aphone').val(Employee.phone)
            $('#address').val(Employee.address)
            document.getElementById("editform").action = '/employees/updateemployee?id=' + Employee.id;
            $('#EditUserModal').modal();
        })

    });

    window.setTimeout(function () {
        $('.alert').fadeTo(500, 0).slideUp(500, function () {
            $(this).hide();
        });
    }, 7500);
});
