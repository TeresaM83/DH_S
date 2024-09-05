$(document).ready(function () {
    $.getJSON("/crud/edificios/", function (listaEdificios) {
        $.each(listaEdificios, function (e, edificio) {
            $("#select-edificios").append("<option value='" + edificio.id + "'>" + edificio.direccion + "</option>");
        })
    });
    $.getJSON("/crud/clientes/", function (listaClientes) {
        $.each(listaClientes, function (c, cliente) {
            $("#select-clientes").append("<option value='" + cliente.id + "'>" + cliente.nombres + ' ' + cliente.a_paterno + ' ' + cliente.a_materno + "</option>");
        })
    });
})

$(window).on("load", function () {
    window.setTimeout(function () {
        $.getJSON("/crud/contratos", function (lista) {
            generarTabla(lista);
        })
    }, 100);
});

$("#select-edificios").change(function () {
    $.getJSON("/crud/listarDisponiblesxEdificio/" + $(this).val(), function (departamentos) {
        $("#select-departamentos").html("<option value='' disabled selected>[Seleccione]</option>");
        $.each(departamentos, function (d, departamento) {
            $("#select-departamentos").append("<option value='" + departamento.id + "'>" + departamento.piso + "</option>")
        })
    })
});

$("#select-departamentos").change(function () {
    $.getJSON("/crud/departamento/" + $(this).val(), function (departamento) {
        $("#txtgarantia").val(departamento.precio * 2);
    })
});

$(document).on("click", "#btnRegistrarContrato", function () {
    LimpiarFormulario();
    fechaActual();
    document.getElementById("titulo").innerHTML = "Registrar Contrato";
    $("#mdlContrato").modal("show");
});

$(document).on("submit", "#frmContrato", function (event) {
    event.preventDefault();
    if (validarFormulario() == true) {
        $("#txtgarantia").removeAttr("disabled");
        $("#txtfecha").removeAttr("disabled");
        let form = $("#frmContrato");
        let data = form.serialize();
        console.log(data);
        $.ajax({
            type: "POST",
            url: "/crud/contrato",
            data: data,
            success: function (resultado) {
                if (resultado.respuesta == "registrado") {
                    ocuparDepartamento($("#select-departamentos").val());
                    toastr.success(resultado.mensaje);
                    $("#mdlContrato").modal("hide");
                    generarTabla(resultado.listaContratos);
                } else if (resultado.respuesta == "repetido") {
                    toastr.warning(resultado.mensaje);
                } else if (resultado.respuesta == "actualizado") {
                    toastr.info(resultado.mensaje);
                    $("#mdlContrato").modal("hide");
                    generarTabla(resultado.listaContratos);
                } else {
                    toastr.error(resultado.mensaje);
                    $("#mdlContrato").modal("hide");
                }
            },
            error: function () {
                toastr.error("Ocurrió un error");
            }
        });
    }
});

function actualizarContrato(id) {
    $.getJSON("/crud/contrato/" + id, function (contrato) {
        $("#hddid").val(contrato.id);
        $("#select-edificios").val(contrato.departamento.edificio.id);
        $('#select-edificios').trigger('change');
        window.setTimeout(function () {
            $("#select-departamentos").val(contrato.departamento.id);
        }, 100);
        $("#select-clientes").val(contrato.cliente.id);
        $("#txtestadia").val(contrato.estadia_meses);
        $("#txtgarantia").val(contrato.garantia);
        $("#txtfecha").val(contrato.fecha_contrato);
    });
    document.getElementById("titulo").innerHTML = "Actualizar Contrato";
    $("#mdlContrato").modal("show");
}

function eliminarContrato(id , idD) {
    Swal.fire({
        title: 'Se eliminará el contrato',
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
                url: '/crud/contrato/' + id,
                success: function (resultado) {
                    if (resultado.respuesta == "eliminado") {
                        Swal.fire({
                            title: resultado.titulo,
                            text: resultado.mensaje,
                            icon: resultado.icon
                        });
                        liberarDepartamento(idD);
                        generarTabla(resultado.listaContratos);
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
    tblContratos = $('#tabla-contrato');
    tblContratos.DataTable().clear();
    tblContratos.DataTable().destroy();
    tblContratos.DataTable({
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
            {
                data: function (row) {
                    let cliente = row.cliente.nombres + " " + row.cliente.a_paterno + " " + row.cliente.a_materno;
                    return cliente;
                }
            },
            {data: "departamento.edificio.direccion"},
            {data: "departamento.piso"},
            {
                data: function (row) {
                    let estadia = row.estadia_meses + " meses";
                    return estadia;
                }
            },
            {data: "fecha_contrato"},
            {
                data: function (row) {
                    let garantia = "S/. " + row.garantia;
                    return garantia;
                }
            },
            {
                data: function (row) {
                    var salida = "<button type='button' class='btn btn-primary btn-xs fa fa-pencil mx-1 icon-edit' onclick='actualizarContrato(" + row.id + ")'></button>" +
                        "<button type='button' class='btn btn-danger btn-xs fa fa-trash mx-1 icon-delete' onclick='eliminarContrato(" + row.id + ',' + row.departamento.id + ")'></button>"
                    return salida;
                }
            }
        ],
    });
}

function LimpiarFormulario() {
    $("#select-departamentos").html("<option value='' disabled>[Seleccione]</option>");
    $("#hddid").val("");
    $("#select-edificios").val("");
    $("#select-departamentos").val("");
    $("#select-clientes").val("");
    $("#txtgarantia").val("");
    $("#txtfecha").val("");
}

function fechaActual() {
    let date = new Date();
    let fecha = date.toISOString().split('T')[0];
    $("#txtfecha").val(fecha);
}

function validarFormulario() {
    return true;
}

function ocuparDepartamento(id) {
    $.ajax({
        type: "POST",
        url: "/crud/ocuparDepartamento/" + id
    });
}

function liberarDepartamento(id) {
    $.ajax({
        type: "POST",
        url: "/crud/liberarDepartamento/" + id
    });
}