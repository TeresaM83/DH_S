package dreamhouse.app.repository;
import java.util.ArrayList;

import dreamhouse.app.entity.Empleado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import dreamhouse.app.entity.Cliente;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {

	public Cliente findByDni(String dni);

	@Query(value="select c from Cliente c where c.dni = :dni and c.id <> :id")
	public Cliente findByDni_Id(@Param("dni") String dni, @Param("id") Long id);

	@Query(value="select c from Cliente c where c.usuario.id = :id")
	public Cliente findByUsuarioId(@Param("id") Long id);
}
