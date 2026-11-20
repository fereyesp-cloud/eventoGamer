package duoc.eventogamer.repository;

import duoc.eventogamer.model.Comentario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ComentarioRepository extends JpaRepository<Comentario, Long> {

    @Query("""
        SELECT c FROM Comentario c
        JOIN FETCH c.usuario
        WHERE c.evento.id = :eventoId
        ORDER BY c.fechaCreacion DESC
        """)
    List<Comentario> findByEventoIdOrderByFechaCreacionDesc(@Param("eventoId") Long eventoId);

    long countByEventoId(Long eventoId);
}
