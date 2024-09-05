package dreamhouse.app.repository;

import dreamhouse.app.entity.Empleado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface EmpleadoRepository extends JpaRepository<Empleado, Long> {

    public Empleado findByDni(String dni);

    @Query(value="select e from Empleado e where e.dni = :dni and e.id <> :id")
    public Empleado findByDni_Id(@Param("dni") String dni, @Param("id") Long id);

    @Query(value="select e from Empleado e where e.usuario.id = :id")
    public Empleado findByUsuarioId(@Param("id") Long id);
}
