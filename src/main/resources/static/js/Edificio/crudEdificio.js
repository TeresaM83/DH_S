$(window).on("load", function () {
    window.setTimeout(function () {
        $.getJSON("/crud/edificios", function (lista) {
            generarTabla(lista);
        })
    }, 100);
});

$(document).on("click", "#btnRegistrarEdificio", function () {
    LimpiarFormulario();
    document.getElementById("titulo").innerHTML = "Registrar Edificio";
    document.getElementById("preview").classList.add("d-none");
    document.getElementById("nombre-imagen").classList.add("d-none");
    $("#mdlEdificio").modal("show");
});

$(document).on("submit", "#frmEdificio", function (event) {
    event.preventDefault();
    if (validarFormulario() == true) {
        var form = $("#frmEdificio")[0];
        var data = new FormData(form);
        console.log(data)
        $.ajax({
            type: "POST",
            enctype: 'multipart/form-data',
            url: "/crud/edificio",
            data: data,
            processData: false,
            contentType: false,
            cache: false,
            success: function (resultado) {
                if (resultado.respuesta == "registrado") {
                    toastr.success(resultado.mensaje);
                    $("#mdlEdificio").modal("hide");
                    generarTabla(resultado.listaEdificios);
                } else if (resultado.respuesta == "repetido") {
                    toastr.warning(resultado.mensaje);
                } else if (resultado.respuesta == "actualizado") {
                    toastr.info(resultado.mensaje);
                    $("#mdlEdificio").modal("hide");
                    generarTabla(resultado.listaEdificios);
                } else if (resultado.respuesta == "no actualizado") {
                    toastr.warning(resultado.mensaje);
                } else {
                    toastr.error(resultado.mensaje);
                    $("#mdlEdificio").modal("hide");
                }
            },
            error: function () {
                toastr.error("Ocurrió un error");
            }
        });
    }
});

function actualizarEdificio(id) {
    document.getElementById("cerrarPreview").innerHTML = "";
    document.getElementById("preview").classList.remove("d-none");
    document.getElementById("nombre-imagen").classList.remove("d-none");
    $.getJSON("/crud/edificio/" + id, function (edificio) {
        $("#hddid").val(edificio.id);
        $("#txtnpisos").val(edificio.n_pisos);
        $("#txtdireccion").val(edificio.direccion);
        $("#imagen").val("");
        document.getElementById("nombre-imagen").innerHTML = edificio.imagen;
        document.getElementById("previewEdificio").src = "/imagesMantenimiento/Edificio/" + edificio.imagen;
        document.getElementById("titulo").innerHTML = "Actualizar Edificio";
        $("#mdlEdificio").modal("show");
    })
}

function eliminarEdificio(id) {
    Swal.fire({
        title: 'Se eliminará el edificio',
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
                url: '/crud/edificio/' + id,
                success: function (resultado) {
                    Swal.fire({
                        title: resultado.titulo,
                        text: resultado.mensaje,
                        icon: resultado.icon
                    });
                    generarTabla(resultado.listaEdificios);
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
    tblEdificios = $('#tabla-edificio');
    tblEdificios.DataTable().clear();
    tblEdificios.DataTable().destroy();
    tblEdificios.DataTable({
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
            {data: "n_pisos"},
            {data: "direccion"},
            {data: "imagen"},
            {
                data: function (row, type, val, meta) {
                    var salida = "<button type='button' class='btn btn-primary btn-xs fa fa-pencil mx-1 icon-edit btnActualizarEdificio' onclick='actualizarEdificio(" + row.id + ")'></button>" +
                        "<button type='button' class='btn btn-danger btn-xs fa fa-trash mx-1 icon-delete btnEliminarEdificio' onclick='eliminarEdificio(" + row.id + ")'></button>"
                    return salida;
                }
            }
        ],
        // dom: "<'row'<'col-sm-4'l><'col-sm-4 text-center'B><'col-sm-4'f>>" +
        //     "<'row'<'col-sm-12'tr>>" +
        //     "<'row'<'col-sm-5'i><'col-sm-7'p>>",
        // buttons: [{
        //     extend: 'excelHtml5',
        //     footer: true,
        //     title: 'Reporte de Usuarios',
        //     filename: 'Reporte Usuarios',
        //     text: '<span class="btn btn-success"><i class="fa fa-file-excel"></i></span>'
        //     },
        //     {
        //         extend: 'pdfHtml5',
        //         download: 'open',
        //         footer: true,
        //         title: 'Reporte de Usuarios',
        //         filename: 'Reporte Usuarios',
        //         text: '<span class="badge  badge-danger"><i class="fa fa-file-pdf"></i></span>',
        //         exportOptions: {
        //             columns: [0, ':visible']
        //         }
        //     },
        //     {
        //         extend: 'copyHtml5',
        //         footer: true,
        //         title: 'Reporte de Usuarios',
        //         filename: 'Reporte de Usuarios',
        //         text: '<span class="badge  badge-primary"><i class="fa fa-copy"></i></span>',
        //         exportOptions: {
        //             columns: [0, ':visible']
        //         }
        //     },
        //     {
        //         extend: 'print',
        //         footer: true,
        //         filename: 'Export_File_print',
        //         text: '<span class="badge badge-light"><i class="fa fa-print"></i></span>'
        //     },
        //     {
        //         extend: 'csvHtml5',
        //         footer: true,
        //         filename: 'Export_File_csv',
        //         text: '<span class="badge  badge-success"><i class="fa fa-file-csv"></i></span>'
        //     },
        //     {
        //         extend: 'colvis',
        //         text: '<span class="badge  badge-info"><i class="fa fa-columns"></i></span>',
        //         postfixButtons: ['colvisRestore']
        //     }]
    });
}

function LimpiarFormulario() {
    $("#hddid").val("");
    $("#txtnpisos").val("");
    $("#txtdireccion").val("");
    $("#imagen").val("");
}

function validarFile(inputFile) {
    if (inputFile.value == "") {
        return true;
    } else {
        //EXTENSIONES Y TAMANO PERMITIDO.
        var extensiones_permitidas = [".png", ".jpg", ".jpeg"];
        var tamano = 8; // EXPRESADO EN MB.
        var rutayarchivo = inputFile.value;
        var ultimo_punto = inputFile.value.lastIndexOf(".");
        var extension = rutayarchivo.slice(ultimo_punto, rutayarchivo.length);

        if (extensiones_permitidas.indexOf(extension) == -1) {
            toastr.warning("Verifique la extension del archivo");
            document.getElementById(inputFile.id).value = "";
            return false; // Si la extension es no válida ya no chequeo lo de abajo.
        }
        if ((inputFile.files[0].size / 1048576) > tamano) {
            toastr.warning("El archivo no puede superar los " + tamano + "MB");
            document.getElementById(inputFile.id).value = "";
            return false;
        }
        return true;
    }
}

function validarFormulario() {
    if (document.getElementById("txtnpisos").value == "" ||
        document.getElementById("txtdireccion").value == "") {
        toastr.error("Falta completar campos");
        return false;
    } else if (validarFile(document.getElementById("imagen")) == false) {
        toastr.error("Imagen no válida");
        return false;
    } else {
        return true;
    }
}

function previewImage(e) {
    const file = e.target.files[0];
    const url = URL.createObjectURL(file);
    document.getElementById("previewEdificio").src = url;
    document.getElementById("nombre-imagen").classList.add("d-none");
    document.getElementById("cerrarPreview").innerHTML =
        "<button type='button' class='btn btn-danger mb-1' onclick='deletePreview()'><i class='fas fa-window-close'></i></button>";
    document.getElementById("preview").classList.remove("d-none");
};

function deletePreview() {
    if ($("#hddid").val() == "") {
        document.getElementById("preview").classList.add("d-none");
        document.getElementById("previewEdificio").src = "";
        document.getElementById("imagen").value = "";
        document.getElementById("cerrarPreview").innerHTML = "";
    } else {
        $.getJSON("/crud/edificio/" + $("#hddid").val(), function (edificio) {
            document.getElementById("imagen").value = "";
            document.getElementById("cerrarPreview").innerHTML = "";
            document.getElementById("nombre-imagen").classList.remove("d-none");
            document.getElementById("nombre-imagen").innerHTML = edificio.imagen;
            document.getElementById("previewEdificio").src = "/imagesMantenimiento/Edificio/" + edificio.imagen;
        })
    }
}

