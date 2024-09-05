package dreamhouse.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import dreamhouse.app.entity.Alquiler;
import dreamhouse.app.entity.Cliente;
import dreamhouse.app.entity.Contrato;
import dreamhouse.app.service.AlquilerService;
import dreamhouse.app.service.ClienteService;
import dreamhouse.app.service.ContratoService;
import dreamhouse.app.service.PagoService;

@RequestMapping("/user")
@Controller()
public class UserController {

	@Autowired
	private PagoService pagoService;
	
	@Autowired
	private ClienteService clienteService;
	
	@Autowired
	private AlquilerService alquilerService;
	
	@Autowired
	private ContratoService contratoService;
	
//	@GetMapping("/cronograma")
//	public String cronograma(Model model) {
//		model.addAttribute("titulo", "Cronograma de Pagos");
//		Cliente cliente = clienteService.obtenerClienteLogueado();
//		Contrato contrato = contratoService.buscarPorClienteId(cliente.getId());
//		Alquiler alquiler = alquilerService.buscarPorContrato(contrato);
//		model.addAttribute("pagos", pagoService.buscarPorAlquiler(alquiler));
//		return "User/CronogramaPagos";
//	}
	
	
}
