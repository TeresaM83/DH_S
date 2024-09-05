package dreamhouse.app.service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dreamhouse.app.entity.Alquiler;
import dreamhouse.app.entity.Contrato;
import dreamhouse.app.repository.AlquilerRepository;

@Service
public class AlquilerService {

	@Autowired
	private AlquilerRepository alquilerRepository;

	public List<Alquiler> listarTodo() {
		return alquilerRepository.findAll();
	}

	public Alquiler guardarDatos(Alquiler alquiler) {
		Date fechaInicio = new Date();
		Calendar c = Calendar.getInstance();
		c.setTime(fechaInicio);
		c.add(Calendar.DATE, 7);
		fechaInicio = c.getTime();
		
		if (alquiler.getFecha_inicio() != null) {
			fechaInicio = alquiler.getFecha_inicio();
		}
		alquiler = new Alquiler(alquiler.getId(), fechaInicio, alquiler.getEstado(), alquiler.getContrato());
		return alquilerRepository.save(alquiler);
	}

	public Alquiler buscarPorId(Long id) {
		return alquilerRepository.findById(id).get();
	}

	public void eliminar(Long id) {
		alquilerRepository.deleteById(id);
	}
	
	public Alquiler buscarPorContrato(Contrato contrato) {
		return alquilerRepository.findByContrato(contrato);
	}

	public Alquiler eliminarA(Long id) {
		Alquiler a = alquilerRepository.findById(id).get();
		alquilerRepository.deleteById(id);
		return a;
	}
	
	public Alquiler ultimoRegistro() {
		return alquilerRepository.ultimoAlquiler();
	}
}
