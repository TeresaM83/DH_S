package dreamhouse.app.ncontroller;

import dreamhouse.app.entity.Departamento;
import dreamhouse.app.entity.Edificio;
import dreamhouse.app.service.ClienteService;
import dreamhouse.app.service.DepartamentoService;
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
public class nDepartamentoController {

    @Autowired
    private DepartamentoService departamentoService;

    @Autowired
    private ClienteService clienteService;

    @GetMapping("/departamento")
    public String crudDepartamento(Model model) {
        model.addAttribute("titulo", "Mantenimiento Departamentos");
        model.addAttribute("departamentoActive", "active");
        return "Departamento/crudDepartamento";
    }

    @ResponseBody
    @GetMapping("/departamento/{id}")
    public Departamento getDepartamento(@PathVariable("id") Long id) {
        return departamentoService.buscarPorId(id);
    }

    @ResponseBody
    @GetMapping("/departamentos")
    public List<Departamento> getDepartamentos() {
        return departamentoService.listarTodo();
    }

    @ResponseBody
    @PostMapping("/departamento")
    public Map<String, Object> postDepartamento(Departamento departamento) {
        Map<String, Object> salida = new HashMap<>();
        Departamento repetido;
        try{
            //Registrar
            if(departamento.getId() == null){
                repetido = departamentoService.buscarPorIdEdificio_Piso(departamento.getEdificio().getId(), departamento.getPiso());
                if (Objects.nonNull(repetido)) {
                    salida.put("respuesta", "repetido");
                    salida.put("mensaje", "El número de piso ya se encuentra registrado");
                } else{
                    departamentoService.guardarDatos(departamento);
                    salida.put("respuesta", "registrado");
                    salida.put("mensaje", "Departamento registrado correctamente");
                    salida.put("listaDepartamentos", departamentoService.listarTodo());
                }
                return salida;
            }
            //Actualizar
            repetido = departamentoService.buscarPorId_IdEdificio_Piso(departamento.getEdificio().getId(), departamento.getPiso(), departamento.getId());
            if (Objects.nonNull(repetido)) {
                salida.put("respuesta", "no actualizado");
                salida.put("mensaje", "Número de piso ya registrado");
            } else{
                departamentoService.guardarDatos(departamento);
                salida.put("respuesta", "actualizado");
                salida.put("mensaje", "Departamento actualizado correctamente");
                salida.put("listaDepartamentos", departamentoService.listarTodo());
            }
        }catch(Exception e){
            salida.put("mensaje", "Departamento no registrado");
            e.printStackTrace();
        }
        return salida;
    }

    @ResponseBody
    @DeleteMapping("/departamento/{id}")
    public Map<String, Object> deleteDepartamento(@PathVariable("id") Long id) {
        Map<String, Object> salida = new HashMap<>();
        try {
            departamentoService.eliminar(id);
            salida.put("respuesta", "eliminado");
            salida.put("mensaje", "Departamento eliminado correctamente");
            salida.put("titulo", "Éxito");
            salida.put("icon","success");
            salida.put("listaDepartamentos", departamentoService.listarTodo());
        } catch (Exception e) {
            salida.put("respuesta", "error");
            salida.put("mensaje", "Es posible que el departamento este asociado a un contrato");
            salida.put("titulo", "Error");
            salida.put("icon","error");
            e.printStackTrace();
        }
        return salida;
    }

}
