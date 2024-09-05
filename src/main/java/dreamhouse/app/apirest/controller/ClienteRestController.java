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

import dreamhouse.app.entity.Cliente;
import dreamhouse.app.service.ClienteService;

@RequestMapping("/api")
@RestController
public class ClienteRestController {
	
    @Autowired
    ClienteService clienteService;

	@GetMapping("/cliente")
	public List<Cliente> listarClientes() {
		return clienteService.listarTodo();
	}

    @GetMapping("/cliente/{id}")
	public Cliente clienteId(@PathVariable("id")Long id) {
		return clienteService.buscarPorId(id);
	}

	@PostMapping("/cliente")
	public Cliente agregarClientes(@RequestBody Cliente c) {
		return clienteService.guardarDatos(c);
	}

	@PutMapping(path = { "/cliente/{id}" })
	public Cliente editarCliente(@RequestBody Cliente e, @PathVariable("id") Long id) {
		e.setId(id);
		return clienteService.guardarDatos(e);
	}

	@DeleteMapping(path = { "/cliente/{id}" })
	public void eliminarCliente(@PathVariable("id") Long id) {
		clienteService.eliminar(id);
	}
}
