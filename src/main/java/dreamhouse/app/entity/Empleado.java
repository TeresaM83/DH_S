package dreamhouse.app.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name="empleado")
public class Empleado {

    @Id
    @Column(name="id_empleado")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    @ManyToOne
    @JoinColumn(name="id_cargo")
    private Cargo cargo;
}
