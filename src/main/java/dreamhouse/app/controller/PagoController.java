package dreamhouse.app.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import dreamhouse.app.entity.Pago;
import dreamhouse.app.service.AlquilerService;
import dreamhouse.app.service.PagoService;

@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
@Controller
@RequestMapping("/pago")
public class PagoController {

	@Autowired
	private PagoService pagoService;
	
	@Autowired
	private AlquilerService alquilerService;
	
	@GetMapping("/listar")
	public String listar(Model model) {
		model.addAttribute("titulo", "Listar Pagos");
		model.addAttribute("pagos", pagoService.listarTodo());
		model.addAttribute("pagoActive", "active");
		return "Pago/listPago";
	}
	
	@GetMapping("/registrar")
	public String registrar(Map<String, Object> model) {
		Pago pago = new Pago();
		model.put("titulo", "Registrar Pago");
		model.put("button", "Registrar Datos");
		model.put("pago", pago);
		model.put("listalquileres", alquilerService.listarTodo());
		return "Pago/regPago";
	}
	
	@PostMapping("/registrar")
	public String guardar(Pago pago) {
		pagoService.guardarDatos(pago);
		return "redirect:/pago/listar";
	}

	@GetMapping(value="/editar/{id}")
	public String editar(@PathVariable(value="id") Long id, Model model) {
		Pago pago = pagoService.buscarPorId(id);
		model.addAttribute("pago", pago);
		model.addAttribute("titulo", "Editar Pago");
		model.addAttribute("button", "Guardar Cambios");
		model.addAttribute("listalquileres", alquilerService.buscarPorId(pago.getAlquiler().getId()));
		return "Pago/regPago";
	}
	
	@GetMapping(value="/eliminar/{id}")
	public String eliminar(@PathVariable(value="id") Long id) {
		if(id>0) {
			pagoService.eliminar(id);
		}
		return "redirect:/pago/listar";
	}
	
	@ResponseBody
	@GetMapping("/apipago")
	public List<Pago> listarPagos(){
		return pagoService.listarTodo();
	}
	
	@ResponseBody
	@PostMapping("/apipago")
	public Pago agregarPagos(@RequestBody Pago p){
		return pagoService.guardarDatos(p);
	}

	@ResponseBody
	@PutMapping(path ={"/apipago/{id}"})
	public Pago editarPago(@RequestBody Pago p, @PathVariable("id") Long id){
		p.setId(id);
		return pagoService.guardarDatos(p);
	}

	@ResponseBody
	@DeleteMapping(path ={"/apipago/{id}"})
	public Pago eliminarPago(@PathVariable("id") Long id){
		return pagoService.eliminarA(id);
	}
}
