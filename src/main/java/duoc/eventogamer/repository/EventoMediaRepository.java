package duoc.eventogamer.repository;

import duoc.eventogamer.model.EventoMedia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EventoMediaRepository extends JpaRepository<EventoMedia, Long> {

    @Query("""
        SELECT m FROM EventoMedia m
        JOIN FETCH m.usuario
        WHERE m.evento.id = :eventoId
        ORDER BY m.fechaCreacion DESC
        """)
    List<EventoMedia> findByEventoIdOrderByFechaCreacionDesc(@Param("eventoId") Long eventoId);
}
