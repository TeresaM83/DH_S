package dreamhouse.app.repository;

import dreamhouse.app.entity.Rol;
import dreamhouse.app.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    public Usuario findByEmail(String email);

    @Query(value="select u from Usuario u where u.email = :email and u.id <> :id")
    public Usuario findByEmail_Id(String email, Long id);

    @Query(value = "select u from Usuario u where u.estado = 'pendiente'")
    public List<Usuario> listarPendientes();

}
