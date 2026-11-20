package duoc.eventogamer.repository;

import duoc.eventogamer.model.Valoracion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ValoracionRepository extends JpaRepository<Valoracion, Long> {

    @Query("""
        SELECT v FROM Valoracion v
        JOIN FETCH v.usuario
        WHERE v.evento.id = :eventoId
        ORDER BY v.fechaActualizacion DESC
        """)
    List<Valoracion> findByEventoIdOrderByFechaActualizacionDesc(@Param("eventoId") Long eventoId);

    Optional<Valoracion> findByEventoIdAndUsuarioId(Long eventoId, Long usuarioId);

    @Query("SELECT AVG(v.puntuacion) FROM Valoracion v WHERE v.evento.id = :eventoId")
    Double obtenerPromedioPorEventoId(@Param("eventoId") Long eventoId);

    long countByEventoId(Long eventoId);
}
