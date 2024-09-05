package dreamhouse.app.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import dreamhouse.app.entity.Edificio;
import dreamhouse.app.entity.Reniec;
import dreamhouse.app.entity.Solicitudes;
import dreamhouse.app.service.DepartamentoService;
import dreamhouse.app.service.EdificioService;
import dreamhouse.app.service.ReniecService;
import dreamhouse.app.service.SolicitudService;

@Controller
public class HomeController {
	/*@Autowired
	private ContratoService contratoService;
	@Autowired
	private ClienteService clienteService;*/
	@Autowired
	private EdificioService edificioService;
	@Autowired
	private DepartamentoService departamentoService;
	@Autowired
	private ReniecService reniecService;
	@Autowired
	private SolicitudService soliService;

	@RequestMapping({"/index", "/", "/home","", "/inicio"})
	public String Index(Model model){
		model.addAttribute("titulo", "DREAM HOUSE");
		model.addAttribute("departamentos",departamentoService.listarTodo());
		return "index";
	}
	
	@GetMapping("/catalogo")
	public String catalogo(Model model) {
		model.addAttribute("titulo", "Catálogo");
		model.addAttribute("edificios",edificioService.listarTodo());
		return "Tienda/edificio";
	}
	
	@GetMapping(value="/catalogo/{id}")
	public String VerDepas(@PathVariable(value="id") Long id, Map<String, Object> model) {
		Edificio edificio = null;
		if(id>0) {
			edificio = edificioService.buscarPorId(id);
		} else {
			return "redirect:/edificio/listar";
		}
		model.put("edificio", edificio);
		model.put("titulo", "Departamentos");
		model.put("departamentos", departamentoService.buscarDisponiblesPorIdEdificio(edificio.getId()));
		model.put("disponibles", departamentoService.buscarDisponiblesPorIdEdificio(edificio.getId()).size());
		return "Tienda/catalogo";
	}
		
	@GetMapping(value="/catalogo/departamento/{id}")
	public String departamentosDetalle(@PathVariable(value="id") Long id, Map<String, Object> model) {
		Reniec persona= new Reniec();
		model.put("titulo", "Departamento");
		model.put("departamento",departamentoService.buscarPorId(id));
		model.put("persona", persona);
		return "Tienda/detallesDepartamento";
	}
	@PostMapping(value="/catalogo/departamento/{id}")
	public String validar(@PathVariable(value="id") Long id, Map<String, Object> model, Reniec persona, RedirectAttributes redirectAttrs) {
		if(reniecService.buscarPorDni(persona.getDni())!=null){
			persona=reniecService.buscarPorDni(persona.getDni());
			Solicitudes solicitud= new Solicitudes();
			solicitud.setPersona(persona);
			solicitud.setDepa(departamentoService.buscarPorId(id));
			model.put("titulo", "Separar Departamento");
			model.put("departamento",departamentoService.buscarPorId(id));
			model.put("persona", persona);
			model.put("solicitud", solicitud);
			return "Tienda/alquilar";
		}
		else{
			redirectAttrs.addFlashAttribute("mensaje", "Dni no válido").addFlashAttribute("clase", "danger text-center");
		return "redirect:/catalogo/departamento/"+id;
		}
	}

	@PostMapping("/catalogo/contrato/{id}")
	public String guardarregistrocontrato(Solicitudes solicitud,@PathVariable(value="id") Long id, Map<String, Object> model,RedirectAttributes redirectAttrs) {
		soliService.guardarDatos(solicitud);
		model.put("titulo", "Departamento");
		model.put("departamento",departamentoService.buscarPorId(id));
		redirectAttrs.addFlashAttribute("mensaje", "Estimad@ "+ solicitud.getPersona().getNombres()+" "+ solicitud.getPersona().getApellidos()+ " su solicitud fue enviada correctamente, luego de evaluarla nos comunicaremos con usted.").addFlashAttribute("clase", "success text-center");
		return "redirect:/catalogo/departamento/"+id;
	}
}
