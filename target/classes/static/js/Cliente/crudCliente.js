$(window).on("load", function () {
    window.setTimeout(function () {
        $.getJSON("/crud/clientes", function (lista) {
            generarTabla(lista);
        })
    }, 100);
});

$(document).on("click", "#btnRegistrarCliente", function () {
    $("#select-usuarios").removeAttr("disabled");
    $("#select-usuarios").html("<option value='' disabled>[Seleccione]</option>");
    $.getJSON("/crud/usuariosPendientes", function (listaUsuarios) {
        $.each(listaUsuarios, function (u, usuario) {
            $("#select-usuarios").append("<option value='" + usuario.id + "'>" + usuario.email + "</option>");
        })
    })
    LimpiarFormulario();
    document.getElementById("titulo").innerHTML = "Registrar Cliente";
    $("#mdlCliente").modal("show");
});

$(document).on("submit", "#frmCliente", function (event) {
    event.preventDefault();
    if (validarFormulario() == true) {
        $("#select-usuarios").removeAttr("disabled");
        let form = $("#frmCliente");
        let data = form.serialize();
        $.ajax({
            type: "POST",
            url: "/crud/cliente",
            data: data,
            success: function (resultado) {
                if (resultado.respuesta == "registrado") {
                    toastr.success(resultado.mensaje);
                    $("#mdlCliente").modal("hide");
                    generarTabla(resultado.listaClientes);
                } else if (resultado.respuesta == "repetido") {
                    toastr.warning(resultado.mensaje);
                } else if (resultado.respuesta == "actualizado") {
                    toastr.info(resultado.mensaje);
                    $("#mdlCliente").modal("hide");
                    generarTabla(resultado.listaClientes);
                } else {
                    toastr.error(resultado.mensaje);
                    $("#mdlCliente").modal("hide");
                }
            },
            error: function () {
                toastr.error("Ocurrió un error");
            }
        });
    }
});

function actualizarCliente(id) {
    $("#select-usuarios").html("");
    $("#select-usuarios").attr("disabled", "");
    $.getJSON("/crud/cliente/" + id, function (cliente) {
        $("#select-usuarios").append("<option value='" + cliente.usuario.id + "' selected>" + cliente.usuario.email + "</option>");
        $("#hddid").val(cliente.id);
        $("#txtnombres").val(cliente.nombres);
        $("#txta_materno").val(cliente.a_materno);
        $("#txta_paterno").val(cliente.a_paterno);
        $("#txtdni").val(cliente.dni);
        $("#txttelefono").val(cliente.telefono);
        $("#select-usuarios").val(cliente.usuario.id);
        $("#select-estado").val(cliente.estado);
    });
    document.getElementById("titulo").innerHTML = "Actualizar Cliente";
    $("#mdlCliente").modal("show");
}

function eliminarCliente(id) {
    Swal.fire({
        title: 'Se eliminará el cliente',
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
                url: '/crud/cliente/' + id,
                success: function (resultado) {
                    if (resultado.respuesta == "eliminado") {
                        Swal.fire({
                            title: resultado.titulo,
                            text: resultado.mensaje,
                            icon: resultado.icon
                        });
                        generarTabla(resultado.listaClientes);
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
    tblClientes = $('#tabla-cliente');
    tblClientes.DataTable().clear();
    tblClientes.DataTable().destroy();
    tblClientes.DataTable({
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
            {data: "usuario.id"},
            {data: "estado"},
            {
                data: function (row, type, val, meta) {
                    var salida = "<button type='button' class='btn btn-primary btn-xs fa fa-pencil mx-1 icon-edit' onclick='actualizarCliente(" + row.id + ")'></button>" +
                        "<button type='button' class='btn btn-danger btn-xs fa fa-trash mx-1 icon-delete' onclick='eliminarCliente(" + row.id + ")'></button>"
                    return salida;
                }
            }
        ],
    });
}

function LimpiarFormulario() {
    $("#hddid").val("");
    $("#txtnombres").val("");
    $("#txta_paterno").val("");
    $("#txta_materno").val("");
    $("#txtdni").val("");
    $("#txttelefono").val("");
    $("#select-usuarios").val("");
    $("#select-estado").val("");
}

function validarFormulario() {
    if ($("#txtnombres").val() == "" || $("#txta_paterno").val() == "" ||
        $("#txta_materno").val() == "" || $("#txtdni").val() == "" || $("#txttelefono").val() == "" ||
        $("#select-usuarios").val() == "" || $("#select-estado").val() == "") {
        toastr.error("Falta completar campos");
        return false;
    } else {
        return true;
    }
}
