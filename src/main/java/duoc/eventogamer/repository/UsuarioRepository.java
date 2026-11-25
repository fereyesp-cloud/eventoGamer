package duoc.eventogamer.repository;

import duoc.eventogamer.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByUsername(String username);

    boolean existsByUsername(String username);

    List<Usuario> findAllByOrderByIdAsc();

    @Query("SELECT COUNT(DISTINCT u) FROM Usuario u JOIN u.roles r WHERE r = :rol AND u.activo = true")
    long contarActivosConRol(@Param("rol") String rol);
}
