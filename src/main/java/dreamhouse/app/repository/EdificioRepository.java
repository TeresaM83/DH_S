package dreamhouse.app.repository;



import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import dreamhouse.app.entity.Edificio;

public interface EdificioRepository extends JpaRepository<Edificio, Long> {

	@Query(value="select e from Edificio e where e.id = :idEdificio")
	public Edificio buscarPorDepaId(@Param("idEdificio") Long idEdificio);

	@Query(value="select e from Edificio e where e.direccion = :direccion and e.id <> :id")
	public Edificio buscarPorDireccionAndId(@Param("direccion") String direccion, @Param("id") Long id);

	public Edificio findByDireccion(String direccion);
}
