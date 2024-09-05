package dreamhouse.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import dreamhouse.app.entity.Contrato;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

public interface ContratoRepository extends JpaRepository<Contrato, Long> {

	@Query(value="select c from Contrato c where c.cliente.id = :id")
	public Contrato buscarPorIdCliente(@Param("id")Long id);

	@Query(value="select c from Contrato c where c.cliente.id = :id_cli and c.departamento.id = :id_depa and c.fecha_contrato = :fecha")
	public Contrato buscarRepetido(@Param("id_cli")Long idCliente, @Param("id_depa")Long idDepartamento, @Param("fecha") Date fechaContrato);

	@Query(value="select c from Contrato c where c.cliente.id = :id_cli and c.departamento.id = :id_depa and c.fecha_contrato = :fecha and c.id <> :id")
	public Contrato buscarRepetidoUpd(@Param("id_cli")Long idCliente, @Param("id_depa")Long idDepartamento, @Param("fecha") Date fechaContrato, @Param("id") Long id);



}
