package dreamhouse.app.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name="contrato")
public class Contrato {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id_contrato")
	private Long id;
	
	private Integer estadia_meses;
	
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private Date fecha_contrato;
	
	private Double garantia;
	
	@ManyToOne
	@JoinColumn(name="id_cli")
	private Cliente cliente;
	
	@ManyToOne
	@JoinColumn(name="id_depa")
	private Departamento departamento;
}
