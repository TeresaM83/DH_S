package dreamhouse.app.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import dreamhouse.app.entity.Cliente;
import dreamhouse.app.repository.ClienteRepository;

@Service
public class ClienteService {

	@Autowired
	private ClienteRepository clienteRepository;

	public Cliente buscarPorId(Long id) {
		return clienteRepository.findById(id).get();
	}

	public Cliente buscarPorDni(String dni) {
		return clienteRepository.findByDni(dni);
	}

	public Cliente buscarPorDni_Id(String dni, Long id){
		return clienteRepository.findByDni_Id(dni, id);
	}

	public Cliente buscarPorIdUsuario(Long id){
		return clienteRepository.findByUsuarioId(id);
	}

	public List<Cliente> listarTodo() {
		return clienteRepository.findAll();
	}

	public Cliente guardarDatos(Cliente cliente) {
		return clienteRepository.save(cliente);
	}

	public void eliminar(Long id) {
		clienteRepository.deleteById(id);
	}

}
