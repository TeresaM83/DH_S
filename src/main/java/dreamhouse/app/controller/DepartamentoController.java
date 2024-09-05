package dreamhouse.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;


import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import dreamhouse.app.entity.Departamento;
import dreamhouse.app.service.DepartamentoService;
import dreamhouse.app.service.EdificioService;

@Controller
@RequestMapping("departamento")
public class DepartamentoController {

	@Autowired
	DepartamentoService departamentoService;
	
	@Autowired
	EdificioService edificioService;
	
	@GetMapping("/listar")
	public String listar(Model model) {
		model.addAttribute("titulo", "Listar Departamentos");
		model.addAttribute("departamentos", departamentoService.listarTodo());
		model.addAttribute("departamentoActive", "active");
		return "Departamento/listDepart";
	}
	
	@GetMapping("/registrar")
	public String registrar(Model model) {
		Departamento departamento = new Departamento();
		model.addAttribute("titulo", "Registrar Departamento");
		model.addAttribute("button", "Registrar Datos");
		model.addAttribute("departamento", departamento);
		model.addAttribute("listedificios", edificioService.listarTodo());
		return "Departamento/regDepart";
	}
	
	@PostMapping("/registrar")
	public String guardar(Departamento departamento) {
		departamentoService.guardarDatos(departamento);
		return "redirect:/departamento/listar";
	}

	@GetMapping(value="/editar/{id}")
	public String editar(@PathVariable("id") Long id, Model model) {
		Departamento departamento = null;
		if(id>0) {
			departamento = departamentoService.buscarPorId(id);
		} else {
			return "redirect:/departamento/listar";
		}
		model.addAttribute("departamento", departamento);
		model.addAttribute("titulo", "Editar Departamento");
		model.addAttribute("button", "Guardar Cambios");
		model.addAttribute("listedificios", edificioService.buscarPorId(departamento.getEdificio().getId()));
		return "Departamento/regDepart";
	}
	
	@GetMapping(value="/eliminar/{id}")
	public String eliminar(@PathVariable(value="id") Long id, RedirectAttributes redirectAttrs) {
		try {
			if(id>0) {
				departamentoService.eliminar(id);
			}
			return "redirect:/departamento/listar";
		} catch (Exception e) {
			Departamento d = departamentoService.buscarPorId(id);
			redirectAttrs.addFlashAttribute("mensaje", "No se puede eliminar el departamento piso "+d.getPiso() +"de "+d.getEdificio().getDireccion()+" ya que está vinculado a otros registros").addFlashAttribute("clase", "danger text-center");
		return "redirect:/departamento/listar";
		}
	}
	
}
