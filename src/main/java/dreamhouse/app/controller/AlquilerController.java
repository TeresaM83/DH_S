package dreamhouse.app.controller;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import dreamhouse.app.entity.Alquiler;
import dreamhouse.app.entity.Pago;
import dreamhouse.app.service.AlquilerService;
import dreamhouse.app.service.ContratoService;
import dreamhouse.app.service.PagoService;

@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
@Controller
@RequestMapping("/alquiler")
public class AlquilerController {

    @Autowired
    AlquilerService alquilerService;

    @Autowired
    ContratoService contratoService;

    @Autowired
    PagoService pagoService;

    @GetMapping("/listar")
    public String listar(Model model) {
        model.addAttribute("titulo", "Listar Alquileres");
        model.addAttribute("alquileres", alquilerService.listarTodo());
        model.addAttribute("alquilerActive", "active");
        return "Alquiler/listAlquiler";
    }

    @GetMapping("/registrar")
    public String registrar(Map<String, Object> model) {
        Alquiler alquiler = new Alquiler();
        model.put("titulo", "Registrar Alquiler");
        model.put("button", "Registrar Datos");
        model.put("alquiler", alquiler);
        model.put("listcontratos", contratoService.listarTodo());
        return "Alquiler/regAlquiler";
    }

    @PostMapping("/registrar")
    public String guardar(Alquiler alquiler, Pago pago) {
        if (alquiler.getId() == null) {
            try {
                alquilerService.guardarDatos(alquiler);
                Alquiler ultimoAlquiler = alquilerService.ultimoRegistro();
                Date fechaInicio = ultimoAlquiler.getFecha_inicio();
                Calendar c = Calendar.getInstance();

                for (int i = 0; i < ultimoAlquiler.getContrato().getEstadia_meses(); i++) {
                    c.setTime(fechaInicio);
                    c.add(Calendar.MONTH, 1);
                    fechaInicio = c.getTime();
                    pago = new Pago(fechaInicio, "Pendiente", ultimoAlquiler);
                    pagoService.guardarDatos(pago);
                }
            } catch (Exception e) {
                return "Error";
            }
        } else {
            alquilerService.guardarDatos(alquiler);
        }
        return "redirect:/alquiler/listar";
    }

    @GetMapping(value = "/editar/{id}")
    public String editar(@PathVariable(value = "id") Long id, Map<String, Object> model) {
        Alquiler alquiler = null;
        if (id > 0) {
            alquiler = alquilerService.buscarPorId(id);
        } else {
            return "redirect:/alquiler/listar";
        }
        model.put("alquiler", alquiler);
        model.put("titulo", "Editar Alquiler");
        model.put("button", "Guardar Cambios");
        model.put("listcontratos", contratoService.buscarPorId(alquiler.getContrato().getId()));
        return "Alquiler/regAlquiler";
    }

    @GetMapping(value = "/eliminar/{id}")
    public String eliminar(@PathVariable(value = "id") Long id, RedirectAttributes redirectAttrs) {
        try {
            if (id > 0) {
                alquilerService.eliminar(id);
            }
            return "redirect:/alquiler/listar";
        } catch (Exception e) {
            Alquiler a = alquilerService.buscarPorId(id);
            redirectAttrs.addFlashAttribute("mensaje", "No se puede eliminar el registro ya que esta asociado al contrato con id: " + a.getContrato().getId()).addFlashAttribute("clase", "danger text-center");
            return "redirect:/alquiler/listar";
        }
    }

    @ResponseBody
    @GetMapping("/apialquiler")
    public List<Alquiler> listarAlquileres() {
        return alquilerService.listarTodo();
    }

    @ResponseBody
    @PostMapping("/apialquiler")
    public void agregarAlquileres(@RequestBody Alquiler a, Pago pago) {
        if (a.getId() == null) {
            try {
                alquilerService.guardarDatos(a);
                Alquiler ultimoAlquiler = alquilerService.ultimoRegistro();
                Date fechaInicio = ultimoAlquiler.getFecha_inicio();
                Calendar c = Calendar.getInstance();

                for (int i = 0; i < ultimoAlquiler.getContrato().getEstadia_meses(); i++) {
                    c.setTime(fechaInicio);
                    c.add(Calendar.MONTH, 1);
                    fechaInicio = c.getTime();
                    pago = new Pago(fechaInicio, "Pendiente", ultimoAlquiler);
                    pagoService.guardarDatos(pago);
                }
            } catch (Exception e) {
                System.out.println("OCURRIO UN ERROR DURANTE EL PROCESO DE REGISTRO ");
            }
        } else {
            alquilerService.guardarDatos(a);
        }

    }

    @ResponseBody
    @PutMapping(path = {"/apialquiler/{id}"})
    public Alquiler editarAlquiler(@RequestBody Alquiler a, @PathVariable("id") Long id) {
        a.setId(id);
        return alquilerService.guardarDatos(a);
    }

    @ResponseBody
    @DeleteMapping(path = {"/apialquiler/{id}"})
    public Alquiler eliminarAlquiler(@PathVariable("id") Long id) {
        return alquilerService.eliminarA(id);
    }

    @ResponseBody
    @GetMapping("/ultimo")
    public Alquiler ultimoAlquiler() {
        return alquilerService.ultimoRegistro();
    }
}