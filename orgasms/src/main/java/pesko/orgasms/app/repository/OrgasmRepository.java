package pesko.orgasms.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pesko.orgasms.app.domain.entities.Orgasm;

@Repository
public interface OrgasmRepository extends JpaRepository<Orgasm,Integer> {


}
