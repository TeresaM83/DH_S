$(document).ready(function () {
    $.getJSON("/crud/usuarioLogeado", function (salida) {
        document.getElementById("imgUser").src = "/imagesUsuarios/" + salida.usuario.imagen;
        if(salida.mensaje == "empleado"){
            empleado = nombreApellido(salida.empleado.nombres, salida.empleado.a_paterno);
            $("#username-log").html(empleado);
            $("#userrol-log").html(salida.empleado.cargo.nombre);
        } else if(salida.mensaje == "cliente"){
            cliente = nombreApellido(salida.cliente.nombres, salida.cliente.a_paterno);
            $("#username-log").html(cliente);
            $("#userrol-log").html(salida.cliente.usuario.email);
        } else{
            Toastify({
                text: "Ocurrio un error",
                duration: 3000,
                close:true,
                gravity:"top",
                position: "center",
                backgroundColor: "#dc3545",
            }).showToast();
        }
    })
});

function nombreApellido(nombres, apellidoP){
    cadenaNombres = nombres.split(" ");
    pNombre = cadenaNombres[0];

    return pNombre + " " + apellidoP;
}