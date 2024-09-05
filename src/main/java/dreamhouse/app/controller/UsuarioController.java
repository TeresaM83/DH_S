package dreamhouse.app.controller;

import dreamhouse.app.entity.Cliente;
import dreamhouse.app.entity.Empleado;
import dreamhouse.app.entity.Usuario;
import dreamhouse.app.service.ClienteService;
import dreamhouse.app.service.EmpleadoService;
import dreamhouse.app.service.RolService;
import dreamhouse.app.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Controller
@RequestMapping("/usuario")
public class UsuarioController {

    @Autowired
    UsuarioService usuarioService;

    @Autowired
    EmpleadoService empleadoService;

    @Autowired
    ClienteService clienteService;

    @Autowired
    RolService rolService;

    @ResponseBody
    @GetMapping("/listarJson")
    public List<Usuario> getUsuarios(){
        return usuarioService.listarTodo();
    }

    @GetMapping("/listar")
    public String listar(Model model) {
        model.addAttribute("titulo", "Listar Usuarios");
        model.addAttribute("usuarios", usuarioService.listarTodo());
        model.addAttribute("usuarioActive", "active");
        return "Usuario/listUsuario";
    }

    @GetMapping("/registrar")
    public String registrar(Model model) {
        Usuario usuario = new Usuario();
        model.addAttribute("listroles", rolService.listarTodo());
        model.addAttribute("titulo", "Registrar Usuario");
        model.addAttribute("button", "Registrar Datos");
        model.addAttribute("usuario", usuario);
        return "Usuario/regUsuario";
    }

    @PostMapping("/registrar")
    public String guardar(Usuario usuario) {
        usuarioService.guardarDatos(usuario);
        return "redirect:/usuario/listar";
    }

    @GetMapping(value = "/editar/{id}")
    public String editar(@PathVariable(value = "id") Long id, Model model) {
        Usuario usuario = null;
        if (id > 0) {
            usuario = usuarioService.buscarPorId(id);
        } else {
            return "redirect:/usuario/listar";
        }
        model.addAttribute("usuario", usuario);
        model.addAttribute("titulo", "Editar Usuario");
        model.addAttribute("button", "Guardar Cambios");
        model.addAttribute("listroles", rolService.listarTodo());
        return "Usuario/regUsuario";
    }

    @GetMapping(value = "/eliminar/{id}")
    public String eliminar(@PathVariable(value = "id") Long id, RedirectAttributes redirectAttrs) {
        try {
            if (id > 0) {
                usuarioService.eliminar(id);
            }
            return "redirect:/usuario/listar";
        } catch (Exception e) {
            Usuario u = usuarioService.buscarPorId(id);
            redirectAttrs.addFlashAttribute("mensaje", "No se puede eliminar al usuario: " + u.getEmail() + ", 	ya que tiene registros vinculados").addFlashAttribute("clase", "danger text-center");
            return "redirect:/usuario/listar";
        }
    }
}
