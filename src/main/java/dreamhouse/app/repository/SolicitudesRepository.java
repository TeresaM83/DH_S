package dreamhouse.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import dreamhouse.app.entity.Solicitudes;

public interface SolicitudesRepository extends JpaRepository<Solicitudes, Long>{
    
}
