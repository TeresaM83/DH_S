package dreamhouse.app.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dreamhouse.app.entity.Alquiler;
import dreamhouse.app.entity.Pago;
import dreamhouse.app.repository.PagoRepository;

@Service
public class PagoService {
	
	@Autowired
	private PagoRepository pagoRepository;
	
	public List<Pago> listarTodo(){
		return pagoRepository.findAll();
	}
	
	public Pago guardarDatos(Pago pago) {
		return pagoRepository.save(pago);
	}
	
	public Pago buscarPorId(Long id) {
		return pagoRepository.findById(id).get();
	}
	
	public void eliminar(Long id) {
		pagoRepository.deleteById(id);
	}
	
	public List<Pago> buscarPorAlquiler(Alquiler alquiler){
		return pagoRepository.findByAlquiler(alquiler);
	}

	public Pago eliminarA(Long id) {
		Pago p= pagoRepository.findById(id).get();
		pagoRepository.deleteById(id);
		return p;
	}
}
