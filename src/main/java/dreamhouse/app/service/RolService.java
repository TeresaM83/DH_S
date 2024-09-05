package dreamhouse.app.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dreamhouse.app.entity.Rol;
import dreamhouse.app.repository.RolRepository;

import java.util.List;

@Service
public class RolService {

	@Autowired
	private RolRepository rolRepository;
	
//	public Rol obtenerRol(Long id) {
//		return rolRepository.obtenerRol(id);
//	}

	public List<Rol> listarTodo(){
		return rolRepository.findAll();
	}
}
