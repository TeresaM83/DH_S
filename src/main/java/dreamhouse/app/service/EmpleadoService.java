package dreamhouse.app.service;

import dreamhouse.app.entity.Empleado;
import dreamhouse.app.repository.EmpleadoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmpleadoService {

    @Autowired
    EmpleadoRepository empleadoRepository;

    public Empleado buscarPorId(Long id){
        return empleadoRepository.findById(id).get();
    }

    public Empleado buscarPorDni(String dni){
        return empleadoRepository.findByDni(dni);
    }

    public Empleado buscarPorDni_Id(String dni, Long id){
        return empleadoRepository.findByDni_Id(dni, id);
    }

    public Empleado buscarPorIdUsuario(Long id){
        return empleadoRepository.findByUsuarioId(id);
    }

    public List<Empleado> listarTodo(){
        return empleadoRepository.findAll();
    }

    public void guardarDatos(Empleado empleado){
        empleadoRepository.save(empleado);
    }

    public void eliminar(Long id){
        empleadoRepository.deleteById(id);
    }


}

