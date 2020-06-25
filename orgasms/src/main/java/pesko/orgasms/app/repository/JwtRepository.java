package pesko.orgasms.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pesko.orgasms.app.domain.entities.Jwt;

import java.util.Optional;

@Repository
public interface JwtRepository  extends JpaRepository<Jwt,Long> {

    Optional<Jwt>findByToken(String token);
}
