// Call the dataTables jQuery plugin
$(document).ready(function () {
    $('#dataTable #dBtn').on('click', function (event) {
        event.preventDefault();
        var href = $(this).attr('href');
        $('#deleteModal #delRef').attr('href', href);
        $('#deleteModal').modal();
    });
    $('#dataTable #pBtn').on('click', function (event) {
        event.preventDefault();
        $('#pModal #pRef').val($(this).attr('value'));
        $('#pModal').modal();
    });
    $('#dataTable #eBtn').on('click', function (event) {
        event.preventDefault();
        var href = $(this).attr('href');
        $.get(href, function (User, status) {
            $('#iduser').text(User.id)
            $('#Email').text(User.email)
            $('#firstname').val(User.name)
            $('#lastname').val(User.lastName)
            $('#Role').val(User.idRole)
            document.getElementById("editform").action = '/users/updateuser?id=' + User.id;
            $('#EditUserModal').modal();
        })

    });
    window.setTimeout(function () {
        $('.alert').fadeTo(500, 0).slideUp(500, function () {
            $(this).hide();
        });
    }, 7500);
});
