package pesko.orgasms.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pesko.orgasms.app.domain.entities.Role;

@Repository
public interface RoleRepository  extends JpaRepository<Role,Integer> {

    Role findByAuthority(String authority);
}
