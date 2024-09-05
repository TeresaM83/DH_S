package dreamhouse.app.ncontroller;

import dreamhouse.app.entity.Departamento;
import dreamhouse.app.entity.Empleado;
import dreamhouse.app.service.EmpleadoService;
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
public class EmpleadoController {

    @Autowired
    private EmpleadoService empleadoService;

    @GetMapping("/empleado")
    public String crudEmpleado(Model model) {
        model.addAttribute("titulo", "Mantenimiento Empleados");
        model.addAttribute("empleadoActive", "active");
        model.addAttribute("localActive", "active");
        model.addAttribute("ulActive", "d-block");
        return "Empleado/crudEmpleado";
    }

    @ResponseBody
    @GetMapping("/empleado/{id}")
    public Empleado getEmpleado(@PathVariable Long id) {
        return empleadoService.buscarPorId(id);
    }

    @ResponseBody
    @GetMapping("/empleados")
    public List<Empleado> getEmpleados() {
        return empleadoService.listarTodo();
    }

    @ResponseBody
    @PostMapping("/empleado")
    public Map<String, Object> postEmpleado(Empleado empleado) {
        Map<String, Object> salida = new HashMap<>();
        Empleado repetido;
        try {
            //Registrar
            if (empleado.getId() == null) {
                repetido = empleadoService.buscarPorDni(empleado.getDni());
                if (Objects.nonNull(repetido)) {
                    salida.put("respuesta", "repetido");
                    salida.put("mensaje", "El empleado ya se encuentra registrado");
                } else {
                    empleadoService.guardarDatos(empleado);
                    salida.put("respuesta", "registrado");
                    salida.put("mensaje", "Empleado registrado correctamente");
                    salida.put("listaEmpleados", empleadoService.listarTodo());
                }
                return salida;
            }
            //Actualizar
            repetido = empleadoService.buscarPorDni_Id(empleado.getDni(), empleado.getId());
            if (Objects.nonNull(repetido)) {
                salida.put("respuesta", "repetido");
                salida.put("mensaje", "El DNI ya se encuentra registrado");
            } else {
                empleadoService.guardarDatos(empleado);
                salida.put("respuesta", "actualizado");
                salida.put("mensaje", "Empleado actualizado correctamente");
                salida.put("listaEmpleados", empleadoService.listarTodo());
            }
        } catch (Exception e) {
            salida.put("mensaje", "ERROR! Empleado no registrado");
            e.printStackTrace();
        }
        return salida;
    }

    @ResponseBody
    @DeleteMapping("/empleado/{id}")
    public Map<String, Object> deleteEmpleado(@PathVariable Long id) {
        Map<String, Object> salida = new HashMap<>();
        try {
            empleadoService.eliminar(id);
            salida.put("respuesta", "mensaje");
            salida.put("mensaje", "Empleado eliminado correctamente");
            salida.put("titulo", "Éxito");
            salida.put("icon", "success");
            salida.put("listaEmpleados", empleadoService.listarTodo());
        } catch (Exception ex) {
            salida.put("respuesta", "error");
            salida.put("mensaje", "Ha ocurrido un error");
            salida.put("titulo", "Error");
            salida.put("icon", "error");
            salida.put("listaEmpleados", empleadoService.listarTodo());
        }
        return salida;
    }

}
