package pesko.orgasms.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pesko.orgasms.app.domain.entities.Event;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event,Long> {

    List<Event> findAllByDateBetween(LocalDateTime start, LocalDateTime end);


}
