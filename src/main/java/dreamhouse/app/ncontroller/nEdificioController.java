package dreamhouse.app.ncontroller;

import dreamhouse.app.entity.Edificio;
import dreamhouse.app.entity.ResultadoResponse;
import dreamhouse.app.service.ClienteService;
import dreamhouse.app.service.EdificioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Controller
@RequestMapping("/crud")
public class nEdificioController {

    @Autowired
    EdificioService edificioService;

    @Autowired
    private ClienteService clienteService;

    @GetMapping("/edificio")
    public String crudEdificio(Model model) {
        model.addAttribute("titulo", "Mantenimiento Edificios");
        model.addAttribute("edificioActive", "active");
        return "Edificio/crudEdificio";
    }

    @ResponseBody
    @GetMapping("/edificios")
    public List<Edificio> getEdificios() {
        return edificioService.listarTodo();
    }

    @ResponseBody
    @GetMapping("/edificio/{id}")
    public Edificio getEdificio(@PathVariable("id") Long id) {
        return edificioService.buscarPorId(id);
    }

    @ResponseBody
    @PostMapping("/edificio")
    public Map<String, Object> postEdificio(@ModelAttribute Edificio edificio, @RequestParam("archivo") MultipartFile imagen){
        Map<String, Object> salida = new HashMap<>();
        Edificio repetido;
        String nuevoNombre = imagen.getOriginalFilename().replace(" ", "_");
        String rutaAbsoluta = "D://DH//imagesMantenimiento//Edificio";
        try{
            //Registrar
            if(edificio.getId() == null){
                repetido = edificioService.buscarPorDireccion(edificio.getDireccion());
                if (Objects.nonNull(repetido)) {
                    salida.put("respuesta", "repetido");
                    salida.put("mensaje", "El edificio ya se encuentra registrado");
                } else{
                    if(!imagen.isEmpty()) {
                        try {
                            byte[] bytesImg = imagen.getBytes();
                            Path rutaCompleta = Paths.get(rutaAbsoluta + "//" + nuevoNombre);
                            Files.write(rutaCompleta, bytesImg);
                            edificio.setImagen(nuevoNombre);
                        } catch (Exception e) {
                            e.printStackTrace();
                            salida.put("mensaje", "Imagen no válida");
                            return salida;
                        }
                    } else{
                        edificio.setImagen("default.jpg");
                    }
                    edificioService.guardarDatos(edificio);
                    salida.put("respuesta", "registrado");
                    salida.put("mensaje", "Edificio registrado correctamente");
                    salida.put("listaEdificios", edificioService.listarTodo());
                }
                return salida;
            }
            //Actualizar
            Edificio edificioActual = edificioService.buscarPorId(edificio.getId());
            repetido = edificioService.buscarPorDireccionYId(edificio.getDireccion(), edificio.getId());
            if (Objects.nonNull(repetido)) {
                salida.put("respuesta", "no actualizado");
                salida.put("mensaje", "Dirección ya registrada");
            } else{
                if(!imagen.isEmpty()) {
                    try {
                        byte[] bytesImg = imagen.getBytes();
                        Path rutaCompleta = Paths.get(rutaAbsoluta + "//" + nuevoNombre);
                        Files.write(rutaCompleta, bytesImg);
                        edificio.setImagen(nuevoNombre);
                    } catch (Exception e) {
                        e.printStackTrace();
                        salida.put("mensaje", "Imagen no válida");
                        return salida;
                    }
                } else{
                    edificio.setImagen(edificioActual.getImagen());
                }
                edificioService.guardarDatos(edificio);
                salida.put("respuesta", "actualizado");
                salida.put("mensaje", "Edificio actualizado correctamente");
                salida.put("listaEdificios", edificioService.listarTodo());
            }
        } catch (Exception e){
            salida.put("mensaje", "Edificio no registrado");
            e.printStackTrace();
        }
        return salida;
    }

    @ResponseBody
    @DeleteMapping("/edificio/{id}")
    public Map<String, Object> deleteEdificio(@PathVariable("id") Long id) {
        Map<String, Object> salida = new HashMap<>();
        try {
            edificioService.eliminar(id);
            salida.put("respuesta", "mensaje");
            salida.put("mensaje", "Edificio eliminado correctamente");
            salida.put("titulo", "Éxito");
            salida.put("icon","success");
            salida.put("listaEdificios", edificioService.listarTodo());
        } catch (Exception ex) {
            salida.put("respuesta", "error");
            salida.put("mensaje", "Es posible que el edificio este asociado a uno o más departamentos");
            salida.put("titulo", "Error");
            salida.put("icon","error");
            salida.put("listaEdificios", edificioService.listarTodo());
        }
        return salida;
    }
}
