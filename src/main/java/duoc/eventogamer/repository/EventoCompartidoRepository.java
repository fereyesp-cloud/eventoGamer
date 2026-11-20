package duoc.eventogamer.repository;

import duoc.eventogamer.model.CanalCompartido;
import duoc.eventogamer.model.EventoCompartido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EventoCompartidoRepository extends JpaRepository<EventoCompartido, Long> {

    @Query("""
        SELECT c FROM EventoCompartido c
        JOIN FETCH c.usuario
        JOIN FETCH c.evento
        WHERE c.evento.id = :eventoId
        ORDER BY c.fechaCompartido DESC
        """)
    List<EventoCompartido> findByEventoIdOrderByFechaCompartidoDesc(@Param("eventoId") Long eventoId);

    @Query("""
        SELECT c FROM EventoCompartido c
        JOIN FETCH c.usuario
        JOIN FETCH c.evento
        WHERE c.canal = :canal
        ORDER BY c.fechaCompartido DESC
        """)
    List<EventoCompartido> findByCanalOrderByFechaCompartidoDesc(@Param("canal") CanalCompartido canal);

    long countByEventoId(Long eventoId);
}
