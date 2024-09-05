package dreamhouse.app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import dreamhouse.app.entity.Alquiler;
import dreamhouse.app.entity.Pago;

public interface PagoRepository extends JpaRepository<Pago, Long>{

	public List<Pago> findByAlquiler(Alquiler alquiler);
}
