$(document).on("click", "#btnAgregarEdificio", function () {
    LimpiarFormulario();
    $("#modalEdificio").modal("show");
});

$(document).on("click", ".btnActualizarEdificio", function () {
    $("#hddid").val($(this).attr("data-id"));
    $("#txtnpisos").val($(this).attr("data-npisos"));
    $("#txtdireccion").val($(this).attr("data-direccion"));
    $("#imagen").val("");
    $("#modalEdificio").modal("show");
})

$(document).on("click", "#btnCancelar", function () {
    toastr.error("Canceló la operación");
});

$(document).on("click", "#btnGuardarCambios", function (event) {
    event.preventDefault();
    if (validarFormulario() == true) {

        var form = $("#formularioEdificio")[0];
        var data = new FormData(form);

        $.ajax({
            type: "POST",
            enctype: 'multipart/form-data',
            url: "/api/edificio/",
            data: data,
            processData: false,
            contentType: false,
            cache: false,
            success: function (resultado) {
                if (resultado.respuesta == "registrado") {
                    toastr.success(resultado.mensaje);
                    $("#modalEdificio").modal("hide");
                    ListarEdificios();
                } else if (resultado.respuesta == "actualizado") {
                    toastr.info(resultado.mensaje);
                    $("#modalEdificio").modal("hide");
                    ListarEdificios();
                } else if (resultado.respuesta == "repetido") {
                    toastr.warning(resultado.mensaje);
                    LimpiarFormulario();
                } else if (resultado.respuesta == "no actualizado") {
                    toastr.warning(resultado.mensaje);
                    $("#modalEdificio").modal("hide");
                } else if (resultado.respuesta == "error") {
                    toastr.error(resultado.mensaje);
                    $("#modalEdificio").modal("hide");
                } else {
                    toastr.error(resultado.mensaje);
                    $("#modalEdificio").modal("hide");
                }
            }
        });
    }
});

$(document).on("click", ".btnEliminarEdificio", function () {
    Swal.fire({
        title: 'Se eliminará el edificio',
        text: "¿Está seguro que desea eliminar el registro?",
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#d33',
        confirmButtonText: 'Sí, eliminar',
        cancelButtonText: 'Cancelar'
    }).then((result) => {
        if (result.isConfirmed) {
            let id = $(this).attr("data-id");
            console.log(id);
            $.ajax({
                type: 'DELETE',
                contentType: 'application/json',
                url: '/api/edificio/' + id,
                success: function (resultado) {
                    Swal.fire({
                        title: resultado.titulo,
                        text: resultado.mensaje,
                        icon: resultado.icon
                    });
                    ListarEdificios();
                }
            });
        } else {
            Swal.fire("Cancelado", "Canceló la operación", "info");
        }
    });
});

function ListarEdificios() {
    $.ajax({
        type: "GET",
        url: "/api/edificios",
        dataType: "json",
        success: function (datos) {
            $("#tblEdificio > tbody").html("");
            $.each(datos, function (index, value) {
                $("#tblEdificio > tbody").append("<tr>" +
                    "<td>" + value.id + "</td>" +
                    "<td>" + value.n_pisos + "</td>" +
                    "<td>" + value.direccion + "</td>" +
                    "<td>" + value.imagen + "</td>" +
                    "<td><button type='button' class='btn btn-primary btn-xs fa fa-pencil mx-1 icon-edit btnActualizarEdificio'" +
                    " data-id='" + value.id + "'" +
                    " data-npisos='" + value.n_pisos + "'" +
                    " data-direccion='" + value.direccion + "'" +
                    " data-imagen='" + value.imagen + "'></button><button type='button' class='btn btn-danger btn-xs fa fa-trash mx-1 icon-delete btnEliminarEdificio'" +
                    " data-id='" + value.id + "'></button></td>" +
                    "</tr>");
            });
        }
    });
}

function LimpiarFormulario() {
    $("#hddid").val("");
    $("#txtnpisos").val("");
    $("#txtdireccion").val("");
    $("#imagen").val("");
}

function validarFile(all) {
    console.log(all.value);
    if(all.value == ""){
        return true;
    } else {
        //EXTENSIONES Y TAMANO PERMITIDO.
        var extensiones_permitidas = [".png", ".jpg", ".jpeg"];
        var tamano = 8; // EXPRESADO EN MB.
        var rutayarchivo = all.value;
        var ultimo_punto = all.value.lastIndexOf(".");
        var extension = rutayarchivo.slice(ultimo_punto, rutayarchivo.length);

        if (extensiones_permitidas.indexOf(extension) == -1) {
            document.getElementById(all.id).value = "";
            return false; // Si la extension es no válida ya no chequeo lo de abajo.
        }
        if ((all.files[0].size / 1048576) > tamano) {
            toastr.warning("El archivo no puede superar los " + tamano + "MB");
            document.getElementById(all.id).value = "";
            return false;
        }
        return true;
    }
}

function validarFormulario(){
    if(document.getElementById("txtnpisos").value == "" ||
        document.getElementById("txtdireccion").value == "") {
        toastr.error("Falta completar campos");
        return false;
    } else if (validarFile(document.getElementById("imagen")) == false){
        toastr.error("Imagen no válida");
        return false;
    } else {
        return true;
    }
}