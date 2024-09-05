package dreamhouse.app.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dreamhouse.app.entity.Reniec;
import dreamhouse.app.repository.ReniecRepository;

@Service
public class ReniecService {

    @Autowired
	private ReniecRepository reniecRepository;

    public List<Reniec> listarTodo() {
		return reniecRepository.findAll();
	}

    public Reniec buscarPorDni(int dni) {
		return reniecRepository.findByDni(dni);
	}
}
