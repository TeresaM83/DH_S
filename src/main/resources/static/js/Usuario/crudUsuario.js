$(document).ready(function () {
    $.getJSON("/crud/roles", function (listaRoles) {
        $.each(listaRoles, function (r, rol) {
            $("#div-roles").append("<div class='form-check'>" +
                "<input class='form-check-input' type='checkbox' name='roles' value='" + rol.id + "' id='chk" + rol.nombre + "' />" +
                "<label class='form-check-label' for='chk" + rol.nombre + "' >" + rol.nombre.replace("ROLE_", "") + "</label>" +
                "</div>");
        })
    })
});

$(window).on("load", function () {
    window.setTimeout(function () {
        $.getJSON("/crud/usuarios", function (lista) {
            generarTabla(lista);
        })
    }, 100);
});

$(document).on("click", "#btnRegistrarUsuario", function () {
    LimpiarFormulario();
    document.getElementById("titulo").innerHTML = "Registrar Usuario";
    document.getElementById("preview").classList.add("d-none");
    document.getElementById("nombre-imagen").classList.add("d-none");
    $("#txtemail").removeAttr("disabled");
    $("#txtpassword").removeAttr("disabled");
    $("#select-estado").val("pendiente");
    $("#select-estado").attr("disabled", "");
    $("#secc-admin").html("");
    $("#mdlUsuario").modal("show");
});

$(document).on("click", "#btnCancelar", function (e) {
    LimpiarFormulario();
});

$(document).on("submit", "#frmUsuario", function (e) {
    e.preventDefault();
    if (validarFormulario() == true) {
        $("#txtemail").removeAttr("disabled");
        $("#txtpassword").removeAttr("disabled");
        $("#select-estado").removeAttr("disabled");
        $("#optPendiente").removeAttr("disabled");
        var form = $("#frmUsuario")[0];
        var data = new FormData(form);
        console.log(data);
        $.ajax({
            type: "POST",
            enctype: 'multipart/form-data',
            url: "/crud/usuario",
            data: data,
            processData: false,
            contentType: false,
            cache: false,
            success: function (resultado) {
                if (resultado.respuesta == "registrado") {
                    toastr.success(resultado.mensaje);
                    $("#mdlUsuario").modal("hide");
                    //generarTabla(resultado.listaUsuarios);
                } else if (resultado.respuesta == "repetido") {
                    toastr.warning(resultado.mensaje);
                } else if (resultado.respuesta == "actualizado") {
                    toastr.info(resultado.mensaje);
                    $("#mdlUsuario").modal("hide");
                    generarTabla(resultado.listaUsuarios);
                } else if (resultado.respuesta == "no actualizado") {
                    toastr.warning(resultado.mensaje);
                } else {
                    toastr.error(resultado.mensaje);
                    $("#mdlUsuario").modal("hide");
                }
            },
            error: function () {
                toastr.error("Ocurrió un error");
                $("#mdlUsuario").modal("hide");
            }
        });
    }
});

function actualizarUsuario(id) {
    LimpiarFormulario();
    document.getElementById("cerrarPreview").innerHTML = "";
    document.getElementById("preview").classList.remove("d-none");
    document.getElementById("nombre-imagen").classList.remove("d-none");
    document.getElementById("titulo").innerHTML = "Actualizar Usuario";
    $("#secc-admin").html("");

    $.getJSON("/crud/usuarioLogeado", function (salida) {
        if (salida.mensaje == "empleado") {
            if (salida.empleado.cargo.nombre == "Administrador General") {
                $("#secc-admin").append("<button type='button' class='btn btn-warning' " +
                    "id='btnEdit'>Habilitar Edición</button>");
            }
        }
    })

    window.setTimeout(function () {
        $.getJSON("/crud/usuario/" + id, function (usuario) {
            $("#hddid").val(usuario.id);
            $("#txtemail").val(usuario.email);
            $("#txtemail").attr("disabled", "");
            $("#txtpassword").val(usuario.password);
            $("#txtpassword").attr("disabled", "");
            $("#optPendiente").attr("disabled", "");
            $("#select-estado").val(usuario.estado);
            $("#select-estado").removeAttr("disabled");
            $("#nombre-imagen").html(usuario.imagen);
            document.getElementById("previewUsuarioImg").src = "/imagesUsuarios/" + usuario.imagen;
            let idInputs = [];
            let usuarioRoles = usuario.roles;
            usuarioRoles.forEach(function (x) {
                idInputs.push("chk" + x.nombre);
            });
            idInputs.forEach(function (i) {
                document.getElementById(i).checked = true;
                console.log(i);
            })
        });
        $("#mdlUsuario").modal("show");
    }, 20);
}

$(document).on("click", "#btnEdit", function (e) {
    $("#txtemail").removeAttr("disabled");
    $("#txtpassword").removeAttr("disabled");
    $("#btnEdit").attr("disabled", "");
});

function eliminarUsuario(id) {
    Swal.fire({
        title: 'Se eliminará el usuario',
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
                url: '/crud/usuario/' + id,
                success: function (resultado) {
                    if (resultado.respuesta == "eliminado") {
                        Swal.fire({
                            title: resultado.titulo,
                            text: resultado.mensaje,
                            icon: resultado.icon
                        });
                        generarTabla(resultado.listaUsuarios);
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
    tblUsuarios = $('#tabla-usuario');
    tblUsuarios.DataTable().clear();
    tblUsuarios.DataTable().destroy();
    tblUsuarios.DataTable({
        data: lista,
        responsive: true,
        columnDefs: [
            {responsivePriority: 1, targets: 0},
            {responsivePriority: 2, targets: -1}
        ],
        autoWidth: false,
        columns: [
            {data: "id"},
            {data: "email"},
            {
                data: function (row) {
                    let nombreRoles = [];
                    let usuarioRoles = row.roles;
                    usuarioRoles.forEach(function (x) {
                        nombreRoles.push(x.nombre);
                    });
                    let columnaRoles = [];
                    nombreRoles.forEach(function (i) {
                        columnaRoles.push(i.replace("ROLE_", " "));
                    });
                    return columnaRoles.toString();
                }
            },
            {data: "imagen"},
            {data: "estado"},
            {
                data: function (row, type, val, meta) {
                    var salida = "<button type='button' class='btn btn-primary btn-xs fa fa-pencil mx-1 icon-edit' onclick='actualizarUsuario(" + row.id + ")'></button>" +
                        "<button type='button' class='btn btn-danger btn-xs fa fa-trash mx-1 icon-delete' onclick='eliminarUsuario(" + row.id + ")'></button>"
                    return salida;
                }
            }
        ],
        order: [[0, "asc"]],
        language: {url: '//cdn.datatables.net/plug-ins/1.10.15/i18n/Spanish.json'},
        searching: false
    }).columns.adjust().responsive.recalc();

}

function LimpiarFormulario() {
    $("#hddid").val("");
    $("#txtemail").val("");
    $("#txtpassword").val("");
    $("#select-estado").val("");
    $("#imagen").val("");
    //Limpiar checkbox
    $.getJSON("/crud/roles", function (listaRoles) {
        $.each(listaRoles, function (r, rol) {
            document.getElementById("chk" + rol.nombre).checked = false;
        })
    })
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
    if ($("#txtemail").val() == "" || $("#txtpassword").val() == "" || $("#select-estado").val() == "") {
        toastr.error("Falta completar campos");
        return false;
    } else if (validarFile(document.getElementById("imagen")) == false) {
        toastr.error("Imagen no válida");
        return false;
    } else if(validarCheckBox() == false){
        toastr.error("Seleccione un rol");
        return false;
    } else {
        return true;
    }
}

function previewImage(e) {
    const file = e.target.files[0];
    const url = URL.createObjectURL(file);
    document.getElementById("previewUsuarioImg").src = url;
    document.getElementById("nombre-imagen").classList.add("d-none");
    document.getElementById("cerrarPreview").innerHTML =
        "<button type='button' class='btn btn-danger mb-1' onclick='deletePreview()'><i class='fas fa-window-close'></i></button>";
    document.getElementById("preview").classList.remove("d-none");
};

function deletePreview() {
    if ($("#hddid").val() == "") {
        document.getElementById("preview").classList.add("d-none");
        document.getElementById("previewUsuarioImg").src = "";
        document.getElementById("imagen").value = "";
        document.getElementById("cerrarPreview").innerHTML = "";
    } else {
        $.getJSON("/crud/usuario/" + $("#hddid").val(), function (usuario) {
            document.getElementById("imagen").value = "";
            document.getElementById("cerrarPreview").innerHTML = "";
            document.getElementById("nombre-imagen").classList.remove("d-none");
            document.getElementById("nombre-imagen").innerHTML = usuario.imagen;
            document.getElementById("previewUsuarioImg").src = "/imagesUsuarios/" + usuario.imagen;
        })
    }
}

function validarCheckBox(){
    let contador = 0;
    $.getJSON("/crud/roles", function (listaRoles) {
        $.each(listaRoles, function (r, rol) {
            console.log(contador);
            if(document.getElementById("chk" + rol.nombre).checked){
                contador++;
            }
            console.log(document.getElementById("chk" + rol.nombre).checked);
        })
        return contador > 0;
    })
    console.log(contador);
}
