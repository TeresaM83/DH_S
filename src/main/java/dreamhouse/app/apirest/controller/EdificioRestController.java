package dreamhouse.app.apirest.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;

import dreamhouse.app.entity.ResultadoResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import dreamhouse.app.entity.Edificio;
import dreamhouse.app.service.EdificioService;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/api")
public class EdificioRestController {

    @Autowired
    private EdificioService edificioService;

    @GetMapping("/mantenimientoEdificio")
    public String mantenimientoEdificios(Model model) {
        model.addAttribute("edificios", edificioService.listarTodo());
        model.addAttribute("titulo", "Mantenimiento Edificios");
        model.addAttribute("edificioActive", "active");
        return "Edificio/mantEdificio";
    }

    @ResponseBody
    @GetMapping("/edificios")
    public List<Edificio> getEdificios() {
        return edificioService.listarTodo();
    }

    @GetMapping("/edificio/{id}")
    public Edificio getEdificio(@PathVariable("id") Long id) {
        return edificioService.buscarPorId(id);
    }

    @ResponseBody
    @PostMapping("/edificio")
    public ResultadoResponse postEdificio(@ModelAttribute Edificio edificio, @RequestParam("archivo")MultipartFile imagen) {
        String nuevoNombre = imagen.getOriginalFilename().replace(" ", "_");
        String mensaje = "Edificio registrado correctamente";
        String respuesta = "registrado";
        Edificio repetido = edificioService.buscarPorDireccion(edificio.getDireccion());
        try {
            //Registrar
            if (edificio.getId() == null) {
                if (Objects.nonNull(repetido)) {
                    mensaje = "El edificio ya se encuentra registrado";
                    respuesta = "repetido";
                    return new ResultadoResponse(respuesta, mensaje);
                }
                if(!imagen.isEmpty()) {
                    String rutaAbsoluta = "D://DH//imagesMantenimiento//Edificio";
                    try {
                        byte[] bytesImg = imagen.getBytes();
                        Path rutaCompleta = Paths.get(rutaAbsoluta + "//" + nuevoNombre);
                        Files.write(rutaCompleta, bytesImg);
                        edificio.setImagen(nuevoNombre);
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                        respuesta = "error";
                        mensaje = "Imagen no válida";
                    }
                } else{
                    edificio.setImagen("default.jpg");
                }
                edificioService.guardarDatos(edificio);
                return new ResultadoResponse(respuesta, mensaje);
            }
            //Actualizar
            Edificio edificioActual = edificioService.buscarPorId(edificio.getId());
            repetido = edificioService.buscarPorDireccionYId(edificio.getDireccion(), edificio.getId());
            mensaje = "Datos actualizados correctamente";
            respuesta = "actualizado";
            if (Objects.nonNull(repetido)) {
                mensaje = "El edificio ya se encuentra registrado";
                respuesta = "no actualizado";
                return new ResultadoResponse(respuesta, mensaje);
            }
            if(!imagen.isEmpty()) {
                String rutaAbsoluta = "D://DH//imagesMantenimiento//Edificio";
                try {
                    byte[] bytesImg = imagen.getBytes();
                    Path rutaCompleta = Paths.get(rutaAbsoluta + "//" + nuevoNombre);
                    Files.write(rutaCompleta, bytesImg);
                    edificio.setImagen(nuevoNombre);
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            } else{
                edificio.setImagen(edificioActual.getImagen());
            }
            edificioService.guardarDatos(edificio);
        } catch (Exception exception) {
            mensaje = "Edificio no registrado";
            respuesta = "error";
        }
        return new ResultadoResponse(respuesta, mensaje);
    }

    @ResponseBody
    @PutMapping("/edificio/{id}")
    public Edificio putEdificios(@RequestBody Edificio edificio, @PathVariable("id") Long id) {
        edificio.setId(id);
        return edificioService.guardarDatos(edificio);
    }

    @ResponseBody
    @DeleteMapping("/edificio/{id}")
    public ResultadoResponse eliminarEdificios(@PathVariable("id") Long id) {
        String titulo = "Exito";
        String mensaje = "Edificio eliminado correctamente";
        String respuesta = "eliminado";
        String icon = "success";
        try {
            edificioService.eliminar(id);
        } catch (Exception ex) {
            titulo = "Error";
            mensaje = "Es posible que el edificio este asociado a uno o más departamentos";
            respuesta = "error";
            icon = "error";
        }
        return new ResultadoResponse(titulo, respuesta, mensaje, icon);
    }

}
