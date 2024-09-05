package dreamhouse.app.controller;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import dreamhouse.app.entity.Contrato;
import dreamhouse.app.entity.Departamento;
import dreamhouse.app.service.ClienteService;
import dreamhouse.app.service.ContratoService;
import dreamhouse.app.service.DepartamentoService;
import dreamhouse.app.service.EdificioService;

@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
@Controller
@RequestMapping("/contrato")
public class ContratoController {

	@Autowired
	ContratoService contratoService;

	@Autowired
	EdificioService edificioService;

	@Autowired
	DepartamentoService departamentoService;

	@Autowired
	ClienteService clienteService;

	@GetMapping("/listar")
	public String listar(Model model) {
		model.addAttribute("titulo", "Listar Contratos");
		model.addAttribute("contratos", contratoService.listarTodo());
		model.addAttribute("contratoActive", "active");
		return "Contrato/listContrato";
	}

	@GetMapping("/registrar")
	public String registrar(Map<String, Object> model) {
		Contrato contrato = new Contrato();
		model.put("titulo", "Registrar Contrato");
		model.put("button", "Registrar Datos");
		model.put("contrato", contrato);
		model.put("listdepas", departamentoService.listarDisponibles());
		model.put("listedificios", edificioService.listarTodo());
		model.put("listclientes", clienteService.listarTodo());
		return "Contrato/regContrato";
	}

	@PostMapping("/registrar")
	public String guardar(Contrato contrato) {
		contratoService.guardarDatos(contrato);
		return "redirect:/contrato/listar";
	}

	@GetMapping(value = "/editar/{id}")
	public String editar(@PathVariable(value = "id") Long id, Map<String, Object> model) {
		Contrato contrato = null;
		if (id > 0) {
			contrato = contratoService.buscarPorId(id);
		} else {
			return "redirect:/contrato/listar";
		}
		model.put("contrato", contrato);
		model.put("titulo", "Editar Contrato");
		model.put("button", "Guardar Cambios");
		model.put("depa", departamentoService.buscarPorId(contrato.getDepartamento().getId()));
		model.put("listedificios",edificioService.buscarPorDepaId(contrato.getDepartamento().getEdificio().getId()));
		model.put("listclientes", clienteService.buscarPorId(contrato.getCliente().getId()));
		return "Contrato/regContrato";
	}

	@GetMapping(value = "/eliminar/{id}")
	public String eliminar(@PathVariable(value = "id") Long id, RedirectAttributes redirectAttrs) {
		try{
			if (id > 0) {
				contratoService.eliminar(id);
			}
			return "redirect:/contrato/listar";
		}catch(Exception e){
			redirectAttrs.addFlashAttribute("mensaje", "No se puede eliminar el contrato con id: "+id+" ya que hay alquileres vinculados").addFlashAttribute("clase", "danger text-center");
		return "redirect:/contrato/listar";
		}

	}	

	/*Combo dependiente*/
	@GetMapping("/listaDepas")
	public @ResponseBody List<Departamento> listaDepas(
			@RequestParam(value = "idEdificio", required = true) Long idEdificio) {
		return departamentoService.buscarDisponiblesPorIdEdificio(idEdificio);
	}
	
	@GetMapping("/precioDepa")
	public @ResponseBody Departamento obtenerDepa(
			@RequestParam(value = "idDepa", required = true) Long idDepa) {
		return departamentoService.buscarPorId(idDepa);
	}
	
	@ResponseBody
	@GetMapping("/apicontrato")
	public List<Contrato> listarContratos(){
		return contratoService.listarTodo();
	}
	
	@ResponseBody
	@PostMapping("/apicontrato")
	public Contrato agregarContratos(@RequestBody Contrato c){
		return contratoService.guardarDatos(c);
	}

	@ResponseBody
	@PutMapping(path ={"/apicontrato/{id}"})
	public Contrato editarContrato(@RequestBody Contrato c, @PathVariable("id") Long id){
		c.setId(id);
		return contratoService.guardarDatos(c);
	}

	@ResponseBody
	@DeleteMapping(path ={"/apicontrato/{id}"})
	public void eliminarContrato(@PathVariable("id") Long id){
		contratoService.eliminar(id);
	}
}
