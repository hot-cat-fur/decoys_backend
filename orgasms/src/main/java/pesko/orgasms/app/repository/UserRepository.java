package pesko.orgasms.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pesko.orgasms.app.domain.entities.User;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    Optional<User> findByUsername(String username);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM users_orgasms uo where uo.user_id = ?1",nativeQuery = true)
    Integer deleteUserByIdCustom(Long id);
}
