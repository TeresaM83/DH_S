package dreamhouse.app.ncontroller;

import dreamhouse.app.entity.Cliente;
import dreamhouse.app.entity.Empleado;
import dreamhouse.app.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Controller
@RequestMapping("/crud")
public class nClienteController {

    @Autowired
    private ClienteService clienteService;

    @GetMapping("/cliente")
    public String crudCliente(Model model) {
        model.addAttribute("titulo", "Mantenimiento Clientes");
        model.addAttribute("clienteActive", "active");
        return "Cliente/crudCliente";
    }

    @ResponseBody
    @GetMapping("/cliente/{id}")
    public Cliente getCliente(@PathVariable Long id) {
        return clienteService.buscarPorId(id);
    }

    @ResponseBody
    @GetMapping("/clientes")
    public List<Cliente> getClientes() {
        return clienteService.listarTodo();
    }

    @ResponseBody
    @PostMapping("/cliente")
    public Map<String, Object> postCliente(Cliente cliente) {
        Map<String, Object> salida = new HashMap<>();
        Cliente repetido;
        try {
            //Registrar
            if (cliente.getId() == null) {
                repetido = clienteService.buscarPorDni(cliente.getDni());
                if (Objects.nonNull(repetido)) {
                    salida.put("respuesta", "repetido");
                    salida.put("mensaje", "El cliente ya se encuentra registrado");
                } else {
                    clienteService.guardarDatos(cliente);
                    salida.put("respuesta", "registrado");
                    salida.put("mensaje", "Cliente registrado correctamente");
                    salida.put("listaClientes", clienteService.listarTodo());
                }
                return salida;
            }
            //Actualizar
            repetido = clienteService.buscarPorDni_Id(cliente.getDni(), cliente.getId());
            if (Objects.nonNull(repetido)) {
                salida.put("respuesta", "repetido");
                salida.put("mensaje", "El DNI ya se encuentra registrado");
            } else {
                clienteService.guardarDatos(cliente);
                salida.put("respuesta", "actualizado");
                salida.put("mensaje", "Cliente actualizado correctamente");
                salida.put("listaClientes", clienteService.listarTodo());
            }
        } catch (Exception e) {
            salida.put("mensaje", "ERROR! Cliente no registrado");
            e.printStackTrace();
        }
        return salida;
    }

    @ResponseBody
    @DeleteMapping("/cliente/{id}")
    public Map<String, Object> deleteCliente(@PathVariable Long id) {
        Map<String, Object> salida = new HashMap<>();
        try {
            clienteService.eliminar(id);
            salida.put("respuesta", "mensaje");
            salida.put("mensaje", "Cliente eliminado correctamente");
            salida.put("titulo", "Éxito");
            salida.put("icon", "success");
            salida.put("listaClientes", clienteService.listarTodo());
        } catch (Exception ex) {
            salida.put("respuesta", "error");
            salida.put("mensaje", "Ha ocurrido un error");
            salida.put("titulo", "Error");
            salida.put("icon", "error");
            salida.put("listaClientes", clienteService.listarTodo());
        }
        return salida;
    }
}
