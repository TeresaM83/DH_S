package dreamhouse.app.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import dreamhouse.app.entity.Cliente;
import dreamhouse.app.service.ClienteService;

@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
@Controller
@RequestMapping("/cliente")
public class ClienteController {

    @Autowired
    ClienteService clienteService;

    @GetMapping("/listar")
    public String listar(Model model) {
        model.addAttribute("titulo", "Listar Clientes");
        model.addAttribute("clientes", clienteService.listarTodo());
        model.addAttribute("clienteActive", "active");
        return "Cliente/listCliente";
    }

    @GetMapping("/registrar")
    public String registrar(Map<String, Object> model) {
        Cliente cliente = new Cliente();
        model.put("titulo", "Registrar Cliente");
        model.put("button", "Registrar Datos");
        model.put("cliente", cliente);
        return "Cliente/regCliente";
    }

    @PostMapping("/registrar")
    public String guardar(Cliente cliente) {
        clienteService.guardarDatos(cliente);
        return "redirect:/cliente/listar";
    }

    @GetMapping(value = "/editar/{id}")
    public String editar(@PathVariable(value = "id") Long id, Map<String, Object> model) {
        Cliente cliente = null;
        if (id > 0) {
            cliente = clienteService.buscarPorId(id);
        } else {
            return "redirect:/cliente/listar";
        }
        model.put("cliente", cliente);
        model.put("titulo", "Editar Cliente");
        model.put("button", "Guardar Cambios");
        return "Cliente/regCliente";
    }

    @GetMapping(value = "/eliminar/{id}")
    public String eliminar(@PathVariable(value = "id") Long id, RedirectAttributes redirectAttrs) {
        try {
            if (id > 0) {
                clienteService.eliminar(id);
            }
            return "redirect:/cliente/listar";
        } catch (Exception e) {
            Cliente c = clienteService.buscarPorId(id);
            redirectAttrs.addFlashAttribute("mensaje", "No se puede eliminar al cliente: " + c.getNombres() + ", 	ya que tiene registros vinculados").addFlashAttribute("clase", "danger text-center");
            return "redirect:/cliente/listar";
        }
    }


}
