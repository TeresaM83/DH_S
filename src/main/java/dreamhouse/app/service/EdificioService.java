package dreamhouse.app.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dreamhouse.app.entity.Edificio;
import dreamhouse.app.repository.EdificioRepository;

@Service
public class EdificioService {

	@Autowired
	private EdificioRepository edificioRepository;
	
	@Autowired

	public List<Edificio> listarTodo() {
		return edificioRepository.findAll();
	}

	public Edificio guardarDatos(Edificio edificio) {
		return edificioRepository.save(edificio);
	}

	public Edificio buscarPorId(Long id) {
		return edificioRepository.findById(id).get();
	}

	public void eliminar(Long id) {
		edificioRepository.deleteById(id);
	}

	public Edificio buscarPorDireccionYId(String direccion, Long id){
		return edificioRepository.buscarPorDireccionAndId(direccion, id);
	}

	public Edificio buscarPorDireccion(String direccion){
		return edificioRepository.findByDireccion(direccion);
	}

	public Edificio buscarPorDepaId(Long idEdificio) {
		return edificioRepository.buscarPorDepaId(idEdificio);
	}

}
