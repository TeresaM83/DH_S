package dreamhouse.app.entity;


import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name="cliente")
public class Cliente {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id_cli")
	private Long id;
	
	private String nombres;
	
	private String a_paterno;
	
	private String a_materno;
	
	private String dni;
	
	private String telefono;

	private String estado;

	@OneToOne
	@JoinColumn(name="id_usuario")
	private Usuario usuario;
}
