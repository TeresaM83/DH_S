package dreamhouse.app.entity;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
@Table(name = "edificio")
public class Edificio {

	@Id
	@Column(name = "id_edificio")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
			
	private Integer n_pisos;

	private String direccion;
	
	private String imagen;
//	@JsonIgnore
//	@OneToMany(mappedBy = "edificio", cascade = CascadeType.ALL)
//	private List<Departamento> departamentos
}
