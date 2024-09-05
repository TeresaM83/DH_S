$(window).on("load", function () {
    window.setTimeout(function () {
        $.getJSON("/crud/cargos", function (lista) {
            generarTabla(lista);
        })
    }, 100);
});

$(document).on("click", "#btnRegistrarCargo", function () {
    LimpiarFormulario();
    document.getElementById("titulo").innerHTML = "Registrar Cargo";
    $("#mdlCargo").modal("show");
});

$(document).on("submit", "#frmCargo", function (event) {
    event.preventDefault();
    if (validarFormulario() == true) {
        var form = $("#frmCargo").serializeJSON();
        var data = JSON.stringify(form);
        $.ajax({
            type: "POST",
            url: "/crud/cargo",
            contentType: 'application/json',
            dataType: "json",
            data: data,
            success: function (resultado) {
                if (resultado.respuesta == "registrado") {
                    toastr.success(resultado.mensaje);
                    $("#mdlCargo").modal("hide");
                    generarTabla(resultado.listaCargos);
                } else if (resultado.respuesta == "repetido") {
                    toastr.warning(resultado.mensaje);
                } else if (resultado.respuesta == "actualizado") {
                    toastr.info(resultado.mensaje);
                    $("#mdlCargo").modal("hide");
                    generarTabla(resultado.listaCargos);
                } else {
                    toastr.error(resultado.mensaje);
                    $("#mdlCargo").modal("hide");
                }
            },
            error: function () {
                toastr.error("Ocurrió un error");
            }
        });
    }
});

function actualizarCargo(id) {
    $.getJSON("/crud/cargo/" + id, function (cargo) {
        $("#hddid").val(cargo.id);
        $("#txtnombre").val(cargo.nombre);
    });
    document.getElementById("titulo").innerHTML = "Actualizar Cargo";
    $("#mdlCargo").modal("show");
}

function eliminarCargo(id) {
    Swal.fire({
        title: 'Se eliminará el cargo',
        text: "¿Está seguro que desea eliminar el registro?",
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#435ebe',
        cancelButtonColor: '#dc3545',
        confirmButtonText: 'Sí, eliminar',
        cancelButtonText: 'Cancelar'
    }).then((result) => {
        if (result.isConfirmed) {
            $.ajax({
                type: 'DELETE',
                contentType: 'application/json',
                url: '/crud/cargo/' + id,
                success: function (resultado) {
                    if (resultado.respuesta == "eliminado") {
                        Swal.fire({
                            title: resultado.titulo,
                            text: resultado.mensaje,
                            icon: resultado.icon
                        });
                        generarTabla(resultado.listaCargos);
                    } else if (resultado.respuesta == "error") {
                        Swal.fire({
                            title: resultado.titulo,
                            text: resultado.mensaje,
                            icon: resultado.icon
                        });
                    }
                },
                error: function () {
                    toastr.error("Ocurrió un error");
                }
            });
        } else {
            Swal.fire("Cancelado", "Canceló la operación", "info");
        }
    });
}

function generarTabla(lista) {
    tblCargos = $('#tabla-cargo');
    tblCargos.DataTable().clear();
    tblCargos.DataTable().destroy();
    tblCargos.DataTable({
        data: lista,
        searching: false,
        language: {url: '//cdn.datatables.net/plug-ins/1.10.15/i18n/Spanish.json'},
        responsive: true,
        columnDefs: [
            {responsivePriority: 1, targets: 0},
            {responsivePriority: 2, targets: -1}
        ],
        order: [[0, "asc"]],
        columns: [
            {data: "id"},
            {data: "nombre"},
            {
                data: function (row, type, val, meta) {
                    var salida = "<button type='button' class='btn btn-primary btn-xs fa fa-pencil mx-1 icon-edit' onclick='actualizarCargo(" + row.id + ")'></button>" +
                        "<button type='button' class='btn btn-danger btn-xs fa fa-trash mx-1 icon-delete' onclick='eliminarCargo(" + row.id + ")'></button>"
                    return salida;
                }
            }
        ],
    });
}

function LimpiarFormulario() {
    $("#hddid").val("");
    $("#txtnombre").val("");
}

function validarFormulario() {
    if ($("#txtnombre").val() == "") {
        toastr.error("Falta completar campos");
        return false;
    } else {
        return true;
    }
}
