package dreamhouse.app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

	@GetMapping("/login")
	public String iniciarSesion(Model model) {
		model.addAttribute("titulo", "Iniciar Sesión");
		return "Seguridad/login";
	}

}
