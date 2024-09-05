$(document).ready(function () {
    $("#titulo-banner").hover(function () {
            $(this).css("color", "white");
            $(this).css("border-color", "white");
            $(this).css("font-size", "80px");

        }, function () {
            $(this).css("color", "black");
            $(this).css("border-color", "black");
            $(this).css("font-size", "70px");
        }
    );
    $(".btn-alquilar").click(function () {
        swal("La opción alquilar aún no está disponible, gracias...!");
    });
    $(".ver-mas").hide();

    $("#btn-acerca").click(function () {
        $("#acerca").show(500);
        $(this).css("color", "gray");
    });
    $("#img-mision").click(function (e) {
        $("#mision").toggle(2000);

    });
    $("#img-vision").click(function (e) {
        $("#vision").toggle(2000);
    });

    $("#btn-verMas").click(function (e) {
        $("#card-body").slideToggle();
    });
    $("#btn-verMas2").click(function (e) {
        $("#card-body2").slideToggle();
    });
    $("#btn-verMas3").click(function (e) {
        $("#card-body3").slideToggle();
    });
    $("#btn-verMas4").click(function (e) {
        $("#card-body4").slideToggle();
    });
    $("#btn-verMas5").click(function (e) {
        $("#card-body5").slideToggle();
    });
    $("#btn-verMas6").click(function (e) {
        $("#card-body6").slideToggle();
    });


    $(".precio").mouseleave(function () {
        $(this).css("color", "gray","opacity:", "0.2");
    });

    $(".precio").mouseover(function () {
        $(this).css("color", "black","opacity:", "0.2");
    });

    $(".iconos").mouseleave(function () {
        $(this).animate({
            left: "50px",
            height: "50",
            width: "50px",
            opacity: "3"
        })
    });

    $(".iconos").mouseover(function () {
        $(this).animate({
            left: "80px",
            height: "80",
            width: "80px",
            opacity: "3"
        })
    });
});

function enviar() {
  var formulario = document.getElementById("formEdificio");
  var dirección = document.getElementById("direccion");
  if (dirección.value.trim()!="") {
    formulario.submit();
    return true;
  } else {
	Swal.fire({
        	title: "Error",
		position: 'top-end',
        	html:"La dirección no puede estar vacía",
        	width: 300,
		timer: 1500,
		showConfirmButton: false,
        	icon: 'error'
    	});
	setTimeout(function(){
	document.getElementById('direccion').focus();
}, 2000);
	return false;
  }
}
function enviardni() {
    var formulario = document.getElementById("formDni");
    var dni =  document.getElementById("dnicli");
    if (dni.value.trim()!="" && dni.value.length==8) {
        formulario.submit();
        return true;
    } else {
    Swal.fire({
        title: "Error",
        html:"El número de dni debe contener 8 caracteres",
        width: 300,
        icon: 'error',
	position: 'top-end',
	timer: 1500,
	showConfirmButton: false
    });
	setTimeout(function(){
	document.getElementById('dnicli').focus();
	}, 2000);
        return false;
    }
}
function validarFormularioCliente() {
    var formulario = document.getElementById("formSolicitud");
    var nombres= document.getElementById("nombres");
    var a_m= document.getElementById("a_materno");
    var a_p= document.getElementById("a_paterno");
    var dni= document.getElementById("dni");
    var telefono= document.getElementById("telefono");
    var correo= document.getElementById("correo");
    var contrasena= document.getElementById("contrasena");
    if (nombres.value.trim()!="" && a_m.value.trim()!="" && a_p.value.trim()!="" && dni.value.trim()!="" && telefono.value.trim()!="" &&  correo.value.trim()!="") {
        if(dni.value.length==8 &&  telefono.value.length==9){
            formulario.submit();
            return true;
        }else if(dni.value.length!=8){
            Swal.fire({
                title: "Error",
                html:"El número de dni debe contener 8 caracteres",
                width: 300,
                icon: 'error',
		position: 'top-end',
		timer: 1500,
		showConfirmButton: false
            });
	setTimeout(function(){
	document.getElementById('dni').focus();
	}, 2000);
            return false;
        }
        else{
            Swal.fire({
                title: "Error",
                html:"El número de telefono debe contener 9 caracteres",
                width: 300,
                icon: 'error',
		position: 'top-end',
		timer: 1500,
		showConfirmButton: false
            });
	setTimeout(function(){
	document.getElementById('telefono').focus();
	}, 2000);
            return false;
        }

    } else {
        Swal.fire({
            title: "Error",
            html:"Todos los campos son obligatorios",
            width: 300,
            icon: 'error',
	    position: 'top-end',
	    timer: 1500,
	    showConfirmButton: false
        });
	setTimeout(function(){
	document.getElementById('nombres').focus();
	}, 2000);
        return false;
    }
}
function validarFormularioSolicitud() {
    var formulario = document.getElementById("formSolicitud");
    var telefono =  document.getElementById("telefono");
    if (telefono.value.trim()!="" && telefono.value.length==9) {
        formulario.submit();
        return true;
    } else {
        Swal.fire({
            title: "Error",
            html:"El número de telefono debe contener 9 caracteres",
            width: 300,
            icon: 'error',
		position: 'top-end',
	    timer: 1500,
	    showConfirmButton: false
        });
	setTimeout(function(){
	document.getElementById('telefono').focus();
	}, 2000);
        return false;
    }
}
function validarFormularioCont() {
    var formulario = document.getElementById("formCon");
    var id_depa =  document.getElementById("id_depa");
    var id_edificio =  document.getElementById("id_edificio");
    if (id_edificio.value!="0" && id_depa.value!="0") {
        formulario.submit();
        return true;
    } else {
        Swal.fire({
            title: "Error",
            html:"Los campos Edificio y Departamento son obligatorios",
            width: 300,
            icon: 'error',
		position: 'top-end',
	    timer: 1500,
	    showConfirmButton: false
        });
        return false;
    }
}
function colorOriginal() {
    document.getElementById("titulo-banner").style.color = "black";
    document.getElementById("titulo-banner").style.borderColor = "black";
    document.getElementById("titulo-banner").style.fontSize = "70px";
}

function cambiarColor() {
    document.getElementById("titulo-banner").style.color = "white";
    document.getElementById("titulo-banner").style.borderColor = "white";
    document.getElementById("titulo-banner").style.fontSize = "80px";
}

var titulo = document.getElementById("titulo-banner");
titulo.addEventListener("mouseover", cambiarColor);
titulo.addEventListener("mouseleave", colorOriginal);


$(".iconos").mouseleave(function () {
    $(this).animate({
        left: "50px",
        height: "50",
        width: "50px",
        opacity: "3"
    })
});

$(".iconos").mouseover(function () {
    $(this).animate({
        left: "80px",
        height: "80",
        width: "80px",
        opacity: "3"
    })
});

document.getElementById('n_pisos').focus();
