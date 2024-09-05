package dreamhouse.app.service;

import dreamhouse.app.entity.Cargo;
import dreamhouse.app.entity.Empleado;
import dreamhouse.app.repository.CargoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CargoService {

    @Autowired
    CargoRepository cargoRepository;

    public Cargo buscarPorId(Long id){
        return cargoRepository.findById(id).get();
    }

    public Cargo buscarPorNombre(String nombre){
        return cargoRepository.findByNombre(nombre);
    }

    public Cargo buscarPorNombre_Id(String nombre, Long id){
        return cargoRepository.buscarPorNombre_Id(nombre, id);
    }

    public List<Cargo> listarTodo(){
        return cargoRepository.findAll();
    }

    public Cargo guardarDatos(Cargo cargo){
        return cargoRepository.save(cargo);
    }

    public void eliminar(Long id){
        cargoRepository.deleteById(id);
    }
}
