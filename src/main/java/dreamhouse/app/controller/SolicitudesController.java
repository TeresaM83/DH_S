package dreamhouse.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import dreamhouse.app.service.SolicitudService;

@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
@Controller
@RequestMapping("/solicitud")
public class SolicitudesController {
    @Autowired
	private SolicitudService solicitudService;
	
    @GetMapping("/listar")
	public String listar(Model model) {
		model.addAttribute("titulo", "Listar Solicitudes");
		model.addAttribute("solicitudes", solicitudService.listarTodo());
		model.addAttribute("solicitudActive", "active");
		return "Solicitudes/listSolicitudes";
	}

    //Eliminar
	@GetMapping(value="/eliminar/{id}")
	public String eliminar(@PathVariable(value="id") Long id) {
		if(id>0) {
			solicitudService.eliminar(id);
		}
		return "redirect:/solicitud/listar";
	}
}
