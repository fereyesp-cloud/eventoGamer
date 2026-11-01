package duoc.eventogamer.repository;

import duoc.eventogamer.model.Evento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface EventoRepository extends JpaRepository<Evento, Long> {

    // ===== API de inicio =====
    List<Evento> findTop4ByOrderByFechaPublicacionDesc();

    List<Evento> findByPopularTrueOrderByFechaAsc();

    // ===== Búsqueda avanzada (todos los filtros son opcionales) =====
    @Query("""
        SELECT e FROM Evento e
        WHERE (:nombre IS NULL OR LOWER(e.nombre) LIKE LOWER(CONCAT('%', :nombre, '%')))
          AND (:videojuego IS NULL OR LOWER(e.videojuego) LIKE LOWER(CONCAT('%', :videojuego, '%')))
          AND (:categoria IS NULL OR LOWER(e.categoria) = LOWER(:categoria))
          AND (:paisCiudad IS NULL
               OR LOWER(e.pais) LIKE LOWER(CONCAT('%', :paisCiudad, '%'))
               OR LOWER(e.ciudad) LIKE LOWER(CONCAT('%', :paisCiudad, '%'))
               OR LOWER(e.ubicacion) LIKE LOWER(CONCAT('%', :paisCiudad, '%')))
          AND (:fecha IS NULL OR e.fecha = :fecha)
          AND (:modalidad IS NULL OR LOWER(e.modalidad) = LOWER(:modalidad))
        ORDER BY e.fecha ASC, e.hora ASC
        """)
    List<Evento> buscarAvanzado(@Param("nombre") String nombre,
                                @Param("videojuego") String videojuego,
                                @Param("categoria") String categoria,
                                @Param("paisCiudad") String paisCiudad,
                                @Param("fecha") LocalDate fecha,
                                @Param("modalidad") String modalidad);

    // ===== Compatibilidad con las vistas web =====
    List<Evento> findAllByOrderByFechaAscHoraAsc();

    List<Evento> findByFechaGreaterThanEqualOrderByFechaAscHoraAsc(LocalDate desde);

    List<Evento> findByDestacadoTrueAndFechaGreaterThanEqualOrderByFechaAsc(LocalDate desde);

    // ===== Listas para los filtros =====
    @Query("SELECT DISTINCT e.categoria FROM Evento e ORDER BY e.categoria")
    List<String> listarCategorias();

    @Query("SELECT DISTINCT e.videojuego FROM Evento e ORDER BY e.videojuego")
    List<String> listarVideojuegos();

    @Query("SELECT DISTINCT e.ciudad FROM Evento e WHERE e.ciudad IS NOT NULL ORDER BY e.ciudad")
    List<String> listarCiudades();
}