/**
 * Created by carol on 9/04/15.
 */

$(function () {

    $("#randomUser").click(function(){
        $.ajax({
            method: "GET",
            url: "/randomuser"
        })
            .done(function( msg ) {
                $('#userid').val(msg);
            });
        return false;
    });

    dialog = $("#setUser").dialog({
        autoOpen: false,
        width: 350,
        modal: true,
        buttons: {
            "Actualizar": changeUser,
            Cancel: function () {
                dialog.dialog("close");
            }
        },
        close: function () {

        }
    });

    $("#changeUser").on("click", function () {
        dialog.dialog("open");
    });

    function changeUser() {
        $('form#updateuser').submit();
    }
});