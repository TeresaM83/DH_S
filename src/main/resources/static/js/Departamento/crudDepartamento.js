$(document).ready(function () {
    $.getJSON("/crud/edificios", function (listaEdificios) {
        $.each(listaEdificios, function (e, edificio) {
            $("#select-edificios").append("<option value='" + edificio.id + "'>" + edificio.direccion + "</option>");
        })
    })
});

$(window).on("load", function () {
    window.setTimeout(function () {
        $.getJSON("/crud/departamentos", function (lista) {
            generarTabla(lista);
        })
    }, 100);
});

$(document).on("click", "#btnRegistrarDepartamento", function () {
    LimpiarFormulario();
    document.getElementById("titulo").innerHTML = "Registrar Departamento";
    $("#mdlDepartamento").modal("show");
});

$(document).on("submit", "#frmDepartamento", function (e) {
    e.preventDefault();
    if (validarFormulario() == true) {
        var form = $("#frmDepartamento");
        var data = form.serialize();
        $.ajax({
            type: "POST",
            url: "/crud/departamento",
            data: data,
            success: function (resultado) {
                if (resultado.respuesta == "registrado") {
                    toastr.success(resultado.mensaje);
                    $("#mdlDepartamento").modal("hide");
                    generarTabla(resultado.listaDepartamentos);
                } else if (resultado.respuesta == "repetido") {
                    toastr.warning(resultado.mensaje);
                } else if (resultado.respuesta == "actualizado") {
                    toastr.info(resultado.mensaje);
                    $("#mdlDepartamento").modal("hide");
                    generarTabla(resultado.listaDepartamentos);
                } else if (resultado.respuesta == "no actualizado") {
                    toastr.warning(resultado.mensaje);
                } else {
                    toastr.error(resultado.mensaje);
                    $("#mdlDepartamento").modal("hide");
                }
            },
            error: function () {
                toastr.error("Ocurrió un error");
            }
        });
    }
});

function actualizarDepartamento(id) {
    $.getJSON("/crud/departamento/" + id, function (departamento) {
        $("#hddid").val(departamento.id);
        $("#select-edificios").val(departamento.edificio.id);
        $("#txtpiso").val(departamento.piso);
        $("#txthabitaciones").val(departamento.n_habitaciones);
        $("#txtbanos").val(departamento.n_banos);
        $("#txtarea").val(departamento.area);
        $("#txtprecio").val(departamento.precio);
        $("#select-estado").val(departamento.estado);
    });
    document.getElementById("titulo").innerHTML = "Actualizar Departamento";
    $("#mdlDepartamento").modal("show");
}

function eliminarDepartamento(id) {
    Swal.fire({
        title: 'Se eliminará el departamento',
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
                url: '/crud/departamento/' + id,
                success: function (resultado) {
                    if (resultado.respuesta == "eliminado") {
                        Swal.fire({
                            title: resultado.titulo,
                            text: resultado.mensaje,
                            icon: resultado.icon
                        });
                        generarTabla(resultado.listaDepartamentos);
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
    tblDepartamentos = $('#tabla-departamento');
    tblDepartamentos.DataTable().clear();
    tblDepartamentos.DataTable().destroy();
    tblDepartamentos.DataTable({
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
            {data: "edificio.direccion"},
            {data: "piso"},
            {data: "n_habitaciones"},
            {data: "n_banos"},
            {data: "area"},
            {data: "precio"},
            {data: "estado"},
            {
                data: function (row, type, val, meta) {
                    var salida = "<button type='button' class='btn btn-primary btn-xs fa fa-pencil mx-1 icon-edit btnActualizarDepartamento' onclick='actualizarDepartamento(" + row.id + ")'></button>" +
                        "<button type='button' class='btn btn-danger btn-xs fa fa-trash mx-1 icon-delete btnEliminarDepartamento' onclick='eliminarDepartamento(" + row.id + ")'></button>"
                    return salida;
                }
            }
        ],
    });
}

function LimpiarFormulario() {
    $("#hddid").val("");
    $("#select-edificios").val("");
    $("#txtpiso").val("");
    $("#txthabitaciones").val("");
    $("#txtbanos").val("");
    $("#txtarea").val("");
    $("#txtprecio").val("");
    $("#select-estado").val("");
}

function validarFormulario() {
    if ($("#select-edificios").val() == "" || $("#txtpiso").val() == "" || $("#txthabitaciones").val() == "" ||
        $("#txtbanos").val() == "" || $("#txtarea").val() == "" || $("#txtprecio").val() == "" || $("#select-estado").val() == "") {
        toastr.error("Falta completar campos");
        return false;
    } else {
        return true;
    }
}