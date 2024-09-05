package dreamhouse.app.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dreamhouse.app.entity.Contrato;
import dreamhouse.app.repository.ContratoRepository;

@Service
public class ContratoService {

	@Autowired
	private ContratoRepository contratoRepository;

	public Contrato buscarPorId(Long id) {
		return contratoRepository.findById(id).get();
	}

	public Contrato buscarRepetido(Long idCliente, Long idDepartamento, Date fechaContrato){
		return contratoRepository.buscarRepetido(idCliente, idDepartamento, fechaContrato);
	}

	public Contrato buscarRepetidoUpd(Long idCliente, Long idDepartamento, Date fechaContrato, Long id){
		return contratoRepository.buscarRepetidoUpd(idCliente, idDepartamento, fechaContrato, id);
	}

	public List<Contrato> listarTodo(){
		return contratoRepository.findAll();
	}
	
//	public Contrato guardarDatos(Contrato contrato) {
//		Date fechaContrato = new Date();
//		if(contrato.getFecha_contrato() != null) {
//			fechaContrato = contrato.getFecha_contrato();
//		}
//		contrato = new Contrato(contrato.getId() ,contrato.getEstadia_meses(), fechaContrato, contrato.getGarantia(),
//				contrato.getCliente(), contrato.getDepartamento());
//		return contratoRepository.save(contrato);
//	}

	public Contrato guardarDatos(Contrato contrato) {
		return contratoRepository.save(contrato);
	}

	public void eliminar(Long id) {
		contratoRepository.deleteById(id);
	}

}
