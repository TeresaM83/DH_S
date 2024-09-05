package dreamhouse.app.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name="departamento")
public class Departamento {

	@Id
	@Column(name="id_depa")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private Integer piso;
	
	private Integer n_habitaciones;
	
	private Integer n_banos;
	
	private Double area;
	
	private Double precio;
	
	private String estado;
	
	@ManyToOne
	@JoinColumn(name="id_edificio")
	private Edificio edificio;
	
}
