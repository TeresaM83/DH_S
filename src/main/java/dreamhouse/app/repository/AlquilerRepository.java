package dreamhouse.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import dreamhouse.app.entity.Alquiler;
import dreamhouse.app.entity.Contrato;

public interface AlquilerRepository extends JpaRepository<Alquiler, Long> {

	@Query(value="select * from alquiler order by id_alquiler desc limit 1", nativeQuery = true)
	public Alquiler ultimoAlquiler();
	
	public Alquiler findByContrato(Contrato contrato);
	
}
