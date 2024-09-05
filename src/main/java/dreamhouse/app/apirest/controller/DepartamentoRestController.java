package dreamhouse.app.apirest.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dreamhouse.app.entity.Departamento;
import dreamhouse.app.service.DepartamentoService;

@RequestMapping("/api")
@RestController
public class DepartamentoRestController {

	@Autowired
	private DepartamentoService departamentoService;

	@GetMapping("/departamento")
	public List<Departamento> getDepartamentos() {
		return departamentoService.listarTodo();
	}

	@GetMapping("/departamento/{id}")
	public Departamento getDepartamento(@PathVariable("id") Long id) {
		return departamentoService.buscarPorId(id);
	}

	@PostMapping("/departamento")
	public Departamento postDepartamento(@RequestBody Departamento departamento) {
		return departamentoService.guardarDatos(departamento);
	}

	@PutMapping("/departamento/{id}")
	public Departamento putDepartamento(@RequestBody Departamento departamento, 
			@PathVariable("id") Long id) {
		departamento.setId(id);
		return departamentoService.guardarDatos(departamento);
	}

	@DeleteMapping("/departamento/{id}")
	public void deleteDepartamento(@PathVariable("id") Long id) {
		departamentoService.eliminar(id);
	}
}
