package dreamhouse.app.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dreamhouse.app.entity.Solicitudes;
import dreamhouse.app.repository.SolicitudesRepository;

@Service
public class SolicitudService {

    @Autowired
	private SolicitudesRepository soliRepository;
	
	public List<Solicitudes> listarTodo() {
		return soliRepository.findAll();
	}
    public Solicitudes guardarDatos(Solicitudes soli) {
		return soliRepository.save(soli);
	}
	public Solicitudes buscarPorId(Long id) {
		return soliRepository.findById(id).get();
	}
	public void eliminar(Long id) {
		soliRepository.deleteById(id);
	}
}
