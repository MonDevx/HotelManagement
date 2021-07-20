// Call the dataTables jQuery plugin
$(document).ready(function () {
    $('#dataTable').DataTable();
    $('#dataTable #dBtn').on('click', function (event) {
        event.preventDefault();
        var href = $(this).attr('href');
        $('#deleteModal #delRef').attr('href', href);
        $('#deleteModal').modal();
    });

    $('#dataTable #eBtn').on('click', function (event) {
        event.preventDefault();
        var href = $(this).attr('href');
        $.get(href, function (product, status) {
            $('#id').text(product.id)
            $('#productname').val(product.productname)
            $('#price').val(product.price)
            $('#amount').val(product.amount)
            $('#stores').val(product.idStore)
            document.getElementById("editform").action = '/products/updateproduct?id=' + product.id;
            $('#EditUserModal').modal();
        })

    });
    window.setTimeout(function () {
        $('.alert').fadeTo(500, 0).slideUp(500, function () {
            $(this).hide();
        });
    }, 7500);
});
