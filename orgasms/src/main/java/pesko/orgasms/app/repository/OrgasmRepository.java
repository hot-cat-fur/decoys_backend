package pesko.orgasms.app.repository;

import org.aspectj.weaver.ast.Or;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pesko.orgasms.app.domain.entities.Orgasm;
import pesko.orgasms.app.domain.entities.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrgasmRepository extends JpaRepository<Orgasm, Long> {


    Optional<Orgasm> findByTitle(String title);

    @Query(value = "SELECT  * FROM orgasm o join  orgasm_like_dislike old on o.id = old.orgasm_id" +
            " where old.like_dislike = FALSE AND old.like_dislike_key = ?1 AND  o.pending = FALSE "
            , nativeQuery = true)
    List<Orgasm> findDisliked(String username);

    @Query(value = "SELECT  * FROM orgasm o join  orgasm_like_dislike old on o.id = old.orgasm_id" +
            " where old.like_dislike = TRUE AND old.like_dislike_key = ?1 AND  o.pending = FALSE "
            , nativeQuery = true)
    List<Orgasm> findLiked(String username);

    @Query(value = "SELECT  * FROM orgasm o where o.id NOT IN (SELECT l.orgasm_id from orgasm_like_dislike l where l.like_dislike_key = ?1)" +
            "AND o.pending = FALSE "
            , nativeQuery = true)
    List<Orgasm> findRandom(String username);

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM orgasm_like_dislike o where o.like_dislike_key = ?1", nativeQuery = true)
    int deleteLikeDislikeUserKey(String username);

    List<Orgasm> findAllByUserUsername(String username);

    @Query(value = "SELECT * FROM orgasm o join orgasm_like_dislike old on old.orgasm_id=o.id where  old.like_dislike_key=?1 AND old.like_dislike = TRUE", nativeQuery = true)
    List<Orgasm> findAllOrgasmsLikedBy(String username);


    @Query(value = "SELECT * FROM orgasm o join orgasm_like_dislike old on old.orgasm_id=o.id where  old.like_dislike_key=?1 AND old.like_dislike = FALSE", nativeQuery = true)
    List<Orgasm> findAllOrgasmsDislikedBy(String username);

//    @Transactional
//    @Modifying
//    @Query(value = "DELETE FROM orgasm_like_dislike old where old.orgasm_id = ?1 ; DELETE FROM orgasm o where o.user_id = ?1 AND o.title = ?2",nativeQuery = true)
//    void ffs(Long id,String title);

}
