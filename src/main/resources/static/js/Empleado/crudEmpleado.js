$(document).ready(function () {
    $.getJSON("/crud/cargos", function (listaCargos) {
        $.each(listaCargos, function (c, cargo) {
            $("#select-cargos").append("<option value='" + cargo.id + "'>" + cargo.nombre + "</option>");
        })
    })
});

$(window).on("load", function () {
    window.setTimeout(function () {
        $.getJSON("/crud/empleados", function (lista) {
            generarTabla(lista);
        })
    }, 100);
});

$(document).on("click", "#btnRegistrarEmpleado", function () {
    $("#select-usuarios").removeAttr("disabled");
    $("#select-usuarios").html("<option value='' disabled>[Seleccione]</option>");
    $.getJSON("/crud/usuariosPendientes", function (listaUsuarios) {
        $.each(listaUsuarios, function (u, usuario) {
            $("#select-usuarios").append("<option value='" + usuario.id + "'>" + usuario.email + "</option>");
        })
    })
    LimpiarFormulario();
    document.getElementById("titulo").innerHTML = "Registrar Empleado";
    $("#mdlEmpleado").modal("show");
});

$(document).on("submit", "#frmEmpleado", function (event) {
    event.preventDefault();
    if (validarFormulario() == true) {
        $("#select-usuarios").removeAttr("disabled");
        var form = $("#frmEmpleado");
        var data = form.serialize();
        $.ajax({
            type: "POST",
            url: "/crud/empleado",
            data: data,
            success: function (resultado) {
                if (resultado.respuesta == "registrado") {
                    toastr.success(resultado.mensaje);
                    $("#mdlEmpleado").modal("hide");
                    generarTabla(resultado.listaEmpleados);
                } else if (resultado.respuesta == "repetido") {
                    toastr.warning(resultado.mensaje);
                } else if (resultado.respuesta == "actualizado") {
                    toastr.info(resultado.mensaje);
                    $("#mdlEmpleado").modal("hide");
                    generarTabla(resultado.listaEmpleados);
                } else {
                    toastr.error(resultado.mensaje);
                    $("#mdlEmpleado").modal("hide");
                }
            },
            error: function () {
                toastr.error("Ocurrió un error");
            }
        });
    }
});

function actualizarEmpleado(id) {
    $("#select-usuarios").html("");
    $("#select-usuarios").attr("disabled", "");
    $.getJSON("/crud/empleado/" + id, function (empleado) {
        $("#select-usuarios").append("<option value='" + empleado.usuario.id + "' selected>" + empleado.usuario.email + "</option>");
        $("#hddid").val(empleado.id);
        $("#select-cargos").val(empleado.cargo.id);
        $("#txtnombres").val(empleado.nombres);
        $("#txta_materno").val(empleado.a_materno);
        $("#txta_paterno").val(empleado.a_paterno);
        $("#txtdni").val(empleado.dni);
        $("#txttelefono").val(empleado.telefono);
        $("#select-usuarios").val(empleado.usuario.id);
        $("#select-estado").val(empleado.estado);
    });
    document.getElementById("titulo").innerHTML = "Actualizar Empleado";
    $("#mdlEmpleado").modal("show");
}

function eliminarEmpleado(id) {
    Swal.fire({
        title: 'Se eliminará el empleado',
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
                url: '/crud/empleado/' + id,
                success: function (resultado) {
                    if (resultado.respuesta == "eliminado") {
                        Swal.fire({
                            title: resultado.titulo,
                            text: resultado.mensaje,
                            icon: resultado.icon
                        });
                        generarTabla(resultado.listaEmpleados);
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
    tblEmpleados = $('#tabla-empleado');
    tblEmpleados.DataTable().clear();
    tblEmpleados.DataTable().destroy();
    tblEmpleados.DataTable({
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
            {data: "nombres"},
            {
                data: function (row) {
                    let apellidos = row.a_paterno + " " + row.a_materno;
                    return apellidos;
                }
            },
            {data: "dni"},
            {data: "telefono"},
            {data: "cargo.nombre"},
            {data: "usuario.id"},
            {data: "estado"},
            {
                data: function (row, type, val, meta) {
                    var salida = "<button type='button' class='btn btn-primary btn-xs fa fa-pencil mx-1 icon-edit' onclick='actualizarEmpleado(" + row.id + ")'></button>" +
                        "<button type='button' class='btn btn-danger btn-xs fa fa-trash mx-1 icon-delete' onclick='eliminarEmpleado(" + row.id + ")'></button>"
                    return salida;
                }
            }
        ],
    });
}

function LimpiarFormulario() {
    $("#hddid").val("");
    $("#select-cargos").val("");
    $("#txtnombres").val("");
    $("#txta_paterno").val("");
    $("#txta_materno").val("");
    $("#txtdni").val("");
    $("#txttelefono").val("");
    $("#select-usuarios").val("");
    $("#select-estado").val("");
}

function validarFormulario() {
    if ($("#select-cargos").val() == "" || $("#txtnombres").val() == "" || $("#txta_paterno").val() == "" ||
        $("#txta_materno").val() == "" || $("#txtdni").val() == "" || $("#txttelefono").val() == "" ||
        $("#select-usuarios").val() == "" || $("#select-estado").val() == "") {
        toastr.error("Falta completar campos");
        return false;
    } else {
        return true;
    }
}
