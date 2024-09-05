package dreamhouse.app.security;


import dreamhouse.app.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private UsuarioService usuarioService;

    @Bean
    public static BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(4);
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider auth = new DaoAuthenticationProvider();
        auth.setUserDetailsService(usuarioService);
        auth.setPasswordEncoder(passwordEncoder());
        return auth;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable().authorizeRequests().antMatchers(
                        "/index",
                        "/",
                        "/home",
                        "/inicio",
                        "/catalogo/**",
                        "/imagesMantenimiento/**",
                        "/assets/**",
                        "/bootstrap/**",
                        "/css/**",
                        "/datatables/**",
                        "/images/**",
                        "/jquery/**",
                        "/js/**",
                        "/sweetalert2/**",
                        "/toastr/**",
                        "/pasarelacontrato/**",
                        "/api/**",
                        "/usuario/**"
                ).permitAll()
                .antMatchers("/edificio/**", "/departamento/**", "/cliente/**", "/contrato/**", "/alquiler/**", "/pago/**", "/solicitud/**", "/admin/**").access("hasRole('ADMINISTRADOR')")
                .antMatchers("/user/**").access("hasRole('CLIENTE')")
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/login")
                .permitAll()
                .successHandler(new LoginSuccessHandle())
                .failureUrl("/login?error=true")
                .usernameParameter("username")
                .passwordParameter("password")
                .and()
                .logout()
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .logoutSuccessUrl("/index")
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .permitAll();
    }
}
