package dreamhouse.app.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import dreamhouse.app.entity.Edificio;
import dreamhouse.app.service.EdificioService;

@Controller
@RequestMapping("/edificio")
public class EdificioController {

	
	@Autowired
	private EdificioService edificioService;
	
	//Listar
	@GetMapping("/listar")
	public String listar(Model model) {
		model.addAttribute("titulo", "Listar Edificios");
		model.addAttribute("edificios", edificioService.listarTodo());
		model.addAttribute("edificioActive", "active");
		return "Edificio/listEdificios";
	}
	
	//Registrar
	@GetMapping("/registrar")
	public String registrar(Map<String, Object> model) {
		Edificio edificio = new Edificio();
		model.put("titulo", "Registrar Edificio");
		model.put("button", "Registrar Datos");
		model.put("edificio", edificio);
		return "Edificio/regEdificio";
	}
	
	@PostMapping("/registrar")
	public String guardar(Edificio edificio, @RequestParam("archivo") MultipartFile imagen) {
		if(!imagen.isEmpty()) {
			String rutaAbsoluta = "D://IDAT//CICLO IV//Proyecto DreamHouse//imagesMantenimiento//Edificio";
			try {
				byte[] bytesImg = imagen.getBytes();
				Path rutaCompleta = Paths.get(rutaAbsoluta + "//" + imagen.getOriginalFilename());
				Files.write(rutaCompleta, bytesImg);
				edificio.setImagen(imagen.getOriginalFilename());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		edificioService.guardarDatos(edificio);
		return "redirect:/edificio/listar";
	}
	
	//Actualizar
	@GetMapping(value="/editar/{id}")
	public String editar(@PathVariable(value="id") Long id, Map<String, Object> model) {
		Edificio edificio;
		if(id>0) {
			edificio = edificioService.buscarPorId(id);
		} else {
			return "redirect:/edificio/listar";
		}
		model.put("edificio", edificio);
		model.put("titulo", "Editar Edificio");
		model.put("button", "Guardar Cambios");
		return "Edificio/regEdificio";
	}
	
	//Eliminar
	@GetMapping(value="/eliminar/{id}")
	public String eliminar(@PathVariable(value="id") Long id, RedirectAttributes redirectAttrs) {
		try{
			if(id>0) {
				edificioService.eliminar(id);
			}
			return "redirect:/edificio/listar";
		} catch (Exception e){
			Edificio d = edificioService.buscarPorId(id);
			redirectAttrs.addFlashAttribute("mensaje", "No se puede eliminar el edificio en "+d.getDireccion()+" ya que hay departamentos vinculados");
		return "redirect:/edificio/listar";
		}
		
	}
	
	@ResponseBody
	@GetMapping("/apiedificio")
	public List<Edificio> listarEdificios(){
		return edificioService.listarTodo();
	}
	
	@ResponseBody
	@PostMapping("/apiedificio")
	public Edificio agregarEdificios(@RequestBody Edificio e){
		return edificioService.guardarDatos(e);
	}

	@ResponseBody
	@GetMapping(path ={"/apiedificio/{id}"})
	public Edificio listarEdificiosId(@PathVariable("id") Long id){
		return edificioService.buscarPorId(id);
	}

	@ResponseBody
	@PutMapping(path ={"/apiedificio/{id}"})
	public Edificio editarEdificios(@RequestBody Edificio e, @PathVariable("id") Long id){
		e.setId(id);
		return edificioService.guardarDatos(e);
	}

	@ResponseBody
	@DeleteMapping(path ={"/apiedificio/{id}"})
	public void eliminarEdificios(@PathVariable("id") Long id){
		edificioService.eliminar(id);
	}

}