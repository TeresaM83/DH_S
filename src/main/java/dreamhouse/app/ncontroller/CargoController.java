package dreamhouse.app.ncontroller;

import dreamhouse.app.entity.Cargo;
import dreamhouse.app.service.CargoService;
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
public class CargoController {

    @Autowired
    CargoService cargoService;

    @GetMapping("/cargo")
    public String crudCargo(Model model){
        model.addAttribute("titulo", "Mantenimiento Cargos");
        model.addAttribute("cargoActive", "active");
        model.addAttribute("localActive", "active");
        model.addAttribute("ulActive", "d-block");
        return "Cargo/crudCargo";
    }

    @ResponseBody
    @GetMapping("/cargo/{id}")
    public Cargo getCargo(@PathVariable("id") Long id){
        return cargoService.buscarPorId(id);
    }

    @ResponseBody
    @GetMapping("/cargos")
    public List<Cargo> getCargos(){
        return cargoService.listarTodo();
    }

    @ResponseBody
    @PostMapping("/cargo")
    public Map<String, Object> postCargo(@RequestBody Cargo cargo){
        Map<String, Object> salida = new HashMap<>();
        Cargo repetido;
        try{
            //Registrar
            if(cargo.getId() == null){
                repetido = cargoService.buscarPorNombre(cargo.getNombre());
                if(Objects.nonNull(repetido)){
                    salida.put("respuesta", "repetido");
                    salida.put("mensaje", "El cargo ya se encuentra registrado");
                } else{
                    cargoService.guardarDatos(cargo);
                    salida.put("respuesta", "registrado");
                    salida.put("mensaje", "Cargo registrado correctamente");
                    salida.put("listaCargos", cargoService.listarTodo());
                }
                return salida;
            }
            //Actualizar
            repetido = cargoService.buscarPorNombre_Id(cargo.getNombre(), cargo.getId());
            if(Objects.nonNull(repetido)){
                salida.put("respuesta", "repetido");
                salida.put("mensaje", "El cargo ya se encuentra registrado");
            } else{
                cargoService.guardarDatos(cargo);
                salida.put("respuesta", "actualizado");
                salida.put("mensaje", "Cargo actualizado correctamente");
                salida.put("listaCargos", cargoService.listarTodo());
            }
        }catch (Exception e){
            salida.put("mensaje", "Cargo no registrado");
            e.printStackTrace();
        }
        return salida;
    }

    @ResponseBody
    @DeleteMapping("/cargo/{id}")
    public Map<String, Object> deleteCargo(@PathVariable("id") Long id) {
        Map<String, Object> salida = new HashMap<>();
        try{
            cargoService.eliminar(id);
            salida.put("respuesta", "eliminado");
            salida.put("mensaje", "Cargo eliminado correctamente");
            salida.put("titulo", "Éxito");
            salida.put("icon","success");
            salida.put("listaCargos", cargoService.listarTodo());
        } catch (Exception e){
            salida.put("respuesta", "error");
            salida.put("mensaje", "Es posible que el cargo este asociado a uno o varios empleados");
            salida.put("titulo", "Error");
            salida.put("icon","error");
            e.printStackTrace();
        }
        return salida;
    }
}
