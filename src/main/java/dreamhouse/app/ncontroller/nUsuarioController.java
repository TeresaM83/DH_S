package dreamhouse.app.ncontroller;

import dreamhouse.app.entity.Cliente;
import dreamhouse.app.entity.Departamento;
import dreamhouse.app.entity.Empleado;
import dreamhouse.app.entity.Usuario;
import dreamhouse.app.service.ClienteService;
import dreamhouse.app.service.EmpleadoService;
import dreamhouse.app.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RequestMapping("/crud")
@Controller
public class nUsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    EmpleadoService empleadoService;

    @Autowired
    ClienteService clienteService;

    @GetMapping("/usuario")
    public String crudUsuario(Model model) {
        model.addAttribute("titulo", "Listar Usuarios");
        model.addAttribute("usuarioActive", "active");
        return "Usuario/crudUsuario";
    }

    @ResponseBody
    @GetMapping("/usuario/{id}")
    public Usuario getUsuario(@PathVariable("id") Long id) {
        return usuarioService.buscarPorId(id);
    }

    @ResponseBody
    @GetMapping("/usuarios")
    public List<Usuario> getUsuarios() {
        return usuarioService.listarTodo();
    }

    @ResponseBody
    @GetMapping("/usuariosPendientes")
    public List<Usuario> getUsuariosPendientes() {
        return usuarioService.listarPendientes();
    }

    @ResponseBody
    @PostMapping("/usuario")
    public Map<String, Object> postUsuario(@ModelAttribute Usuario usuario, @RequestParam("archivo") MultipartFile imagen) {
        Map<String, Object> salida = new HashMap<>();
        try {
            Usuario repetido;
            String nuevoNombre = imagen.getOriginalFilename().replace(" ", "_");
            String rutaAbsoluta = "D://DH//imagesUsuarios";

            //Registrar
            if (usuario.getId() == null) {
                repetido = usuarioService.buscarPorEmail(usuario.getEmail());
                if (Objects.nonNull(repetido)) {
                    salida.put("respuesta", "repetido");
                    salida.put("mensaje", "El correo ya se encuentra registrado");
                } else {
                    if (!imagen.isEmpty()) {
                        try {
                            byte[] bytesImg = imagen.getBytes();
                            Path rutaCompleta = Paths.get(rutaAbsoluta + "//" + nuevoNombre);
                            Files.write(rutaCompleta, bytesImg);
                            usuario.setImagen(nuevoNombre);
                        } catch (Exception e) {
                            e.printStackTrace();
                            salida.put("mensaje", "Imagen no válida");
                            return salida;
                        }
                    } else {
                        usuario.setImagen("default.jpg");
                    }
                    usuarioService.guardarDatos(usuario);
                    salida.put("respuesta", "registrado");
                    salida.put("mensaje", "Usuario registrado correctamente");
                    salida.put("listaUsuarios", usuarioService.listarTodo());
                }
                return salida;
            }

            //Actualizar
            repetido = usuarioService.buscarPorEmail_Id(usuario.getEmail(), usuario.getId());
            if (Objects.nonNull(repetido)) {
                salida.put("respuesta", "no actualizado");
                salida.put("mensaje", "Correo ya registrado");
            } else {
                Usuario antiguo = usuarioService.buscarPorId(usuario.getId());
                if (!imagen.isEmpty()) {
                    try {
                        byte[] bytesImg = imagen.getBytes();
                        Path rutaCompleta = Paths.get(rutaAbsoluta + "//" + nuevoNombre);
                        Files.write(rutaCompleta, bytesImg);
                        usuario.setImagen(nuevoNombre);
                    } catch (Exception e) {
                        e.printStackTrace();
                        salida.put("mensaje", "Imagen no válida");
                        return salida;
                    }
                } else {
                    usuario.setImagen(antiguo.getImagen());
                }
                //Validando contraseña
                if (antiguo.getPassword().equals(usuario.getPassword())) {
                    usuarioService.updUsuario(usuario);
                } else {
                    usuarioService.guardarDatos(usuario);
                }
                salida.put("respuesta", "actualizado");
                salida.put("mensaje", "Usuario actualizado correctamente");
                salida.put("listaUsuarios", usuarioService.listarTodo());
            }
        } catch (Exception e) {
            salida.put("mensaje", "Usuario no registrado");
            e.printStackTrace();
        }
        return salida;
    }

    @ResponseBody
    @DeleteMapping("/usuario/{id}")
    public Map<String, Object> deleteUsuario(@PathVariable("id") Long id) {
        Map<String, Object> salida = new HashMap<>();
        try {
            usuarioService.eliminar(id);
            salida.put("respuesta", "eliminado");
            salida.put("mensaje", "Usuario eliminado correctamente");
            salida.put("titulo", "Éxito");
            salida.put("icon", "success");
            salida.put("listaUsuarios", usuarioService.listarTodo());
        } catch (Exception e) {
            salida.put("respuesta", "error");
            salida.put("mensaje", "Es posible que el usuario tenga registros asociados");
            salida.put("titulo", "Error");
            salida.put("icon", "error");
            e.printStackTrace();
        }
        return salida;
    }

    @ResponseBody
    @GetMapping("/usuarioLogeado")
    public Map<String, Object> userLog() {
        Map<String, Object> salida = new HashMap<>();
        try {
            Usuario usuario = usuarioService.obtenerUsuarioLogueado();
            Empleado empleado = empleadoService.buscarPorIdUsuario(usuario.getId());
            if (Objects.nonNull(empleado)) {
                salida.put("mensaje", "empleado");
                salida.put("empleado", empleado);
                salida.put("usuario", usuario);
            } else {
                Cliente cliente = clienteService.buscarPorIdUsuario(usuario.getId());
                salida.put("mensaje", "cliente");
                salida.put("cliente", cliente);
                salida.put("usuario", usuario);
            }
        } catch (Exception e) {
            salida.put("Error", "Ocurrió un error");
            e.printStackTrace();
        }
        return salida;
    }
}

