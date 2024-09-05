package dreamhouse.app.service;

import dreamhouse.app.entity.Cliente;
import dreamhouse.app.entity.Rol;
import dreamhouse.app.entity.Usuario;
import dreamhouse.app.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UsuarioService implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public Usuario buscarPorId(Long id){
        return usuarioRepository.findById(id).get();
    }

    public Usuario buscarPorEmail(String email){
        return usuarioRepository.findByEmail(email);
    }

    public Usuario buscarPorEmail_Id(String email, Long id){
        return usuarioRepository.findByEmail_Id(email, id);
    }

    public List<Usuario> listarTodo() {
        return usuarioRepository.findAll();
    }

    public List<Usuario> listarPendientes() {
        return usuarioRepository.listarPendientes();
    }

    public Usuario guardarDatos(Usuario usuario) {
        usuario = new Usuario(usuario.getId(), usuario.getEmail(), passwordEncoder.encode(usuario.getPassword()), usuario.getEstado(), usuario.getImagen(), usuario.getRoles());
        return usuarioRepository.save(usuario);
    }

    public Usuario updUsuario(Usuario usuario){
        return usuarioRepository.save(usuario);
    }

    public void eliminar(Long id){
        usuarioRepository.deleteById(id);
    }

    /************************LOGIN**************************/
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByEmail(username);
        if (usuario == null) {
            throw new UsernameNotFoundException("Usuario o contraseña incorrectos");
        }
        return new User(usuario.getEmail(), usuario.getPassword(), mapearRoles(usuario.getRoles()));
    }

    private Collection<? extends GrantedAuthority> mapearRoles(Collection<Rol> roles) {
        return roles.stream().map(role -> new SimpleGrantedAuthority(role.getNombre())).collect(Collectors.toList());
    }

    public Usuario obtenerUsuarioLogueado() {
        Usuario usuario;
        Authentication auth = SecurityContextHolder
                .getContext()
                .getAuthentication();
        UserDetails userDetail = (UserDetails) auth.getPrincipal();
        usuario = usuarioRepository.findByEmail(userDetail.getUsername());
        return usuario;
    }
}
