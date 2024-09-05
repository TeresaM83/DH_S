package dreamhouse.app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import dreamhouse.app.entity.Departamento;
import org.springframework.transaction.annotation.Transactional;

public interface DepartamentoRepository extends JpaRepository<Departamento, Long> {

	@Query(value="select d from Departamento d where d.estado = 'disponible'")
	public List<Departamento> listarDisponibles();
	
	@Query(value="select d from Departamento d where d.edificio.id = :idEdificio and d.estado = 'disponible'")
	public List<Departamento> buscarPorIdEdificio(@Param("idEdificio") Long idEdificio);

	@Query(value="select d from Departamento d where d.edificio.id = :idEdificio and d.piso = :piso")
	public Departamento buscarPorIdEdificio_Piso(@Param("idEdificio") Long idEdificio, @Param("piso") Integer piso);

	@Query(value="select d from Departamento d where d.edificio.id = :idEdificio and d.piso = :piso and d.id <> :id")
	public Departamento buscarPorId_IdEdificio_Piso(@Param("idEdificio") Long idEdificio, @Param("piso") Integer piso, @Param("id") Long id);

	@Transactional
	@Modifying
	@Query(value="update Departamento d set d.estado = 'Ocupado' where d.id = :id")
	public void ocuparDepartamento(@Param("id") Long id);

	@Transactional
	@Modifying
	@Query(value="update Departamento d set d.estado = 'Disponible' where d.id = :id")
	public void liberarDepartamento(@Param("id") Long id);
}
