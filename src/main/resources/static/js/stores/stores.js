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
        var Products = $(this).val();
        Products = Products.replace("[", "");
        Products = Products.replace("]", "");
        Products = Products.split(",");
        if (Products != "") {
            $('#text_Products').text(Products);
        }
        $('#viewModal').modal();
    });
    $('#dataTable #eBtn').on('click', function (event) {
        event.preventDefault();
        var href = $(this).attr('href');
        $.get(href, function (store, status) {
            $('#idstore').text(store.id)
            $('#storeName').val(store.storeName)
            $('#Status').val(store.status)
            document.getElementById("editform").action = '/stores/updatestore?id=' + store.id;
            $('#EditUserModal').modal();
        })

    });

    window.setTimeout(function () {
            $('.alert').fadeTo(500, 0).slideUp(500, function () {
                $(this).hide();
            });
        }, 7500);
});

