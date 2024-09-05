package dreamhouse.app.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dreamhouse.app.entity.Departamento;
import dreamhouse.app.repository.DepartamentoRepository;

@Service
public class DepartamentoService {

	@Autowired
	DepartamentoRepository departamentoRepository;

	public List<Departamento> listarTodo() {
		return departamentoRepository.findAll();
	}

	public Departamento guardarDatos(Departamento departamento) {
		return departamentoRepository.save(departamento);
	}

	public Departamento buscarPorId(Long id) {
		return departamentoRepository.findById(id).get();
	}

	public void eliminar(Long id) {
		departamentoRepository.deleteById(id);
	}
	
	public List<Departamento> listarDisponibles(){
		return departamentoRepository.listarDisponibles();
	}
	
	public List<Departamento> buscarDisponiblesPorIdEdificio(Long idEdificio){
		return departamentoRepository.buscarPorIdEdificio(idEdificio);
	}

	public Departamento buscarPorIdEdificio_Piso(Long idEdificio, Integer piso){
		return departamentoRepository.buscarPorIdEdificio_Piso(idEdificio, piso);
	}

	public Departamento buscarPorId_IdEdificio_Piso(Long idEdificio, Integer piso, Long id){
		return departamentoRepository.buscarPorId_IdEdificio_Piso(idEdificio, piso, id);
	}

	public void ocuparDepartamento(Long id){
		departamentoRepository.ocuparDepartamento(id);
	}

	public void liberarDepartamento(Long id){
		departamentoRepository.liberarDepartamento(id);
	}

}
