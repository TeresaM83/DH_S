package dreamhouse.app.ncontroller;

import dreamhouse.app.entity.Contrato;
import dreamhouse.app.entity.Departamento;
import dreamhouse.app.service.ContratoService;
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
public class nContratoController {

    @Autowired
    ContratoService contratoService;

    @Autowired
    DepartamentoService departamentoService;

    @GetMapping("/contrato")
    public String listar(Model model) {
        model.addAttribute("titulo", "Mantenimiento Contratos");
        model.addAttribute("contratoActive", "active");
        return "Contrato/crudContrato";
    }

    @ResponseBody
    @GetMapping("/contrato/{id}")
    public Contrato getContrato(@PathVariable Long id) {
        return contratoService.buscarPorId(id);
    }

    @ResponseBody
    @GetMapping("/contratos")
    public List<Contrato> getContratos() {
        return contratoService.listarTodo();
    }

    @ResponseBody
    @PostMapping("/contrato")
    public Map<String, Object> postContrato(Contrato contrato) {
        Map<String, Object> salida = new HashMap<>();
        Contrato repetido;
        try {
            //Registrar
            if (contrato.getId() == null) {
                repetido = contratoService.buscarRepetido(contrato.getCliente().getId(),
                        contrato.getDepartamento().getId(), contrato.getFecha_contrato());
                if (Objects.nonNull(repetido)) {
                    salida.put("respuesta", "repetido");
                    salida.put("mensaje", "El contrato ya se encuentra registrado");
                } else {
                    contratoService.guardarDatos(contrato);
                    salida.put("respuesta", "registrado");
                    salida.put("mensaje", "Contrato registrado correctamente");
                    salida.put("listaContratos", contratoService.listarTodo());
                }
                return salida;
            }
            //Actualizar
            repetido = contratoService.buscarRepetidoUpd(contrato.getCliente().getId(),
                    contrato.getDepartamento().getId(), contrato.getFecha_contrato(), contrato.getId());
            if (Objects.nonNull(repetido)) {
                salida.put("respuesta", "repetido");
                salida.put("mensaje", "Ya existe un contrato con los mismos datos registrados");
            } else {
                contratoService.guardarDatos(contrato);
                salida.put("respuesta", "actualizado");
                salida.put("mensaje", "Contrato actualizado correctamente");
                salida.put("listaContratos", contratoService.listarTodo());
            }
        } catch (Exception e) {
            salida.put("mensaje", "ERROR! Contrato no registrado");
            e.printStackTrace();
        }
        return salida;
    }

    @ResponseBody
    @DeleteMapping("/contrato/{id}")
    public Map<String, Object> deleteContrato(@PathVariable Long id) {
        Map<String, Object> salida = new HashMap<>();
        try {
            contratoService.eliminar(id);
            salida.put("respuesta", "eliminado");
            salida.put("mensaje", "Contrato eliminado correctamente");
            salida.put("titulo", "Éxito");
            salida.put("icon", "success");
            salida.put("listaContratos", contratoService.listarTodo());
        } catch (Exception ex) {
            salida.put("respuesta", "error");
            salida.put("mensaje", "Es posible que el contrato esté asociado a un alquiler");
            salida.put("titulo", "Error");
            salida.put("icon", "error");
            salida.put("listaContratos", contratoService.listarTodo());
        }
        return salida;
    }

    /*Combo dependiente*/
    @ResponseBody
    @GetMapping("/listarDisponiblesxEdificio/{id}")
    public List<Departamento> buscarDisponiblesPorIdEdificio(@PathVariable Long id) {
        return departamentoService.buscarDisponiblesPorIdEdificio(id);
    }

    @ResponseBody
    @PostMapping("/ocuparDepartamento/{id}")
    public Map<String, Object> ocuparDepartamento(@PathVariable Long id){
        Map<String, Object> salida = new HashMap<>();
        try{
            departamentoService.ocuparDepartamento(id);
            salida.put("respuesta", "ocupado");
        }catch (Exception e){
            e.printStackTrace();
            salida.put("respuesta", "error");
        }
        return salida;
    }

    @ResponseBody
    @PostMapping("/liberarDepartamento/{id}")
    public Map<String, Object> liberarDepartamento(@PathVariable Long id){
        Map<String, Object> salida = new HashMap<>();
        try{
            departamentoService.liberarDepartamento(id);
            salida.put("respuesta", "disponible");
        }catch (Exception e){
            e.printStackTrace();
            salida.put("respuesta", "error");
        }
        return salida;
    }
}
