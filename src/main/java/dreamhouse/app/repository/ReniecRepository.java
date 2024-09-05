package dreamhouse.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import dreamhouse.app.entity.Reniec;

public interface ReniecRepository extends JpaRepository<Reniec, Long>{
    public Reniec findByDni(int dni);
}
