package dreamhouse.app.repository;

import dreamhouse.app.entity.Cargo;
import dreamhouse.app.entity.Edificio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CargoRepository extends JpaRepository<Cargo, Long> {

    public Cargo findByNombre(String nombre);

    @Query(value="select c from Cargo c where c.nombre = :nombre and c.id <> :id")
    public Cargo buscarPorNombre_Id(@Param("nombre") String nombre, @Param("id") Long id);

}
