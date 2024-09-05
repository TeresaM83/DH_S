package dreamhouse.app.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import dreamhouse.app.entity.Rol;

public interface RolRepository extends JpaRepository<Rol, Long> {
	
//	@Query(value="SELECT r FROM Rol r where r.id = :idRol")
//	public Rol obtenerRol(@Param("idRol")Long id);

}
