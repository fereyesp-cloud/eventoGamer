package duoc.eventogamer.service;

import duoc.eventogamer.dto.ActualizarUsuarioRequest;
import duoc.eventogamer.dto.CrearUsuarioRequest;
import duoc.eventogamer.dto.UsuarioAdminDTO;
import duoc.eventogamer.model.Usuario;
import duoc.eventogamer.repository.UsuarioRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.regex.Pattern;

@Service
public class UsuarioAdminService {

    public static final String ROL_USER = "USER";
    public static final String ROL_ADMIN = "ADMIN";
    private static final Set<String> ROLES_VALIDOS = Set.of(ROL_USER, ROL_ADMIN);
    private static final Pattern USERNAME_PATTERN = Pattern.compile("^[a-zA-Z0-9_]{3,20}$");
    private static final String USUARIO_NO_ENCONTRADO = "Usuario no encontrado";

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioAdminService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional(readOnly = true)
    public List<UsuarioAdminDTO> listarTodos() {
        return usuarioRepository.findAllByOrderByIdAsc().stream()
                .map(this::toDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public UsuarioAdminDTO obtenerPorId(Long id) {
        return toDTO(buscarUsuario(id));
    }

    @Transactional
    public UsuarioAdminDTO crearUsuario(CrearUsuarioRequest request) {
        String username = request.getUsername() != null ? request.getUsername().trim() : "";
        if (!USERNAME_PATTERN.matcher(username).matches()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "El usuario debe tener entre 3 y 20 caracteres (letras, números o guion bajo)");
        }
        if (request.getPassword() == null || request.getPassword().length() < 8 || request.getPassword().length() > 64) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "La contraseña debe tener entre 8 y 64 caracteres");
        }
        if (usuarioRepository.existsByUsername(username)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Ese nombre de usuario ya está registrado");
        }

        Set<String> roles = normalizarRoles(request.getRoles());
        Usuario nuevo = new Usuario(username, passwordEncoder.encode(request.getPassword()), roles);
        nuevo.setActivo(request.getActivo() == null || request.getActivo());

        return toDTO(usuarioRepository.save(nuevo));
    }

    @Transactional
    public UsuarioAdminDTO actualizarUsuario(Long id, ActualizarUsuarioRequest request, String adminActual) {
        Usuario usuario = buscarUsuario(id);

        if (request.getRoles() != null) {
            Set<String> nuevosRoles = normalizarRoles(request.getRoles());
            validarCambioAdmin(usuario, nuevosRoles.contains(ROL_ADMIN), adminActual);
            usuario.setRoles(nuevosRoles);
        }

        if (request.getActivo() != null) {
            validarCambioEstado(usuario, request.getActivo(), adminActual);
            usuario.setActivo(request.getActivo());
        }

        if (request.getNuevaPassword() != null && !request.getNuevaPassword().isBlank()) {
            String pwd = request.getNuevaPassword();
            if (pwd.length() < 8 || pwd.length() > 64) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "La nueva contraseña debe tener entre 8 y 64 caracteres");
            }
            usuario.setPassword(passwordEncoder.encode(pwd));
        }

        return toDTO(usuarioRepository.save(usuario));
    }

    @Transactional
    public UsuarioAdminDTO cambiarEstado(Long id, boolean activo, String adminActual) {
        Usuario usuario = buscarUsuario(id);
        validarCambioEstado(usuario, activo, adminActual);
        usuario.setActivo(activo);
        return toDTO(usuarioRepository.save(usuario));
    }

    @Transactional
    public UsuarioAdminDTO cambiarRolAdmin(Long id, boolean otorgarAdmin, String adminActual) {
        Usuario usuario = buscarUsuario(id);
        validarCambioAdmin(usuario, otorgarAdmin, adminActual);

        Set<String> roles = new HashSet<>(usuario.getRoles());
        roles.add(ROL_USER);
        if (otorgarAdmin) {
            roles.add(ROL_ADMIN);
        } else {
            roles.remove(ROL_ADMIN);
        }
        usuario.setRoles(roles);
        return toDTO(usuarioRepository.save(usuario));
    }

    @Transactional
    public void eliminarUsuario(Long id, String adminActual) {
        Usuario usuario = buscarUsuario(id);
        if (usuario.getUsername().equals(adminActual)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "No puedes eliminar tu propia cuenta de administrador");
        }
        if (usuario.isActivo() && usuario.getRoles().contains(ROL_ADMIN)
                && usuarioRepository.contarActivosConRol(ROL_ADMIN) <= 1) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Debe permanecer al menos un administrador activo en el sistema");
        }
        try {
            usuarioRepository.delete(usuario);
            usuarioRepository.flush();
        } catch (DataIntegrityViolationException ex) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "El usuario tiene actividad asociada (eventos, comentarios o multimedia). Desactívalo en su lugar.");
        }
    }

    private Usuario buscarUsuario(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, USUARIO_NO_ENCONTRADO));
    }

    private Set<String> normalizarRoles(Set<String> rolesSolicitados) {
        Set<String> resultado = new HashSet<>();
        resultado.add(ROL_USER);
        if (rolesSolicitados == null || rolesSolicitados.isEmpty()) {
            return resultado;
        }
        for (String rolRaw : rolesSolicitados) {
            if (rolRaw == null || rolRaw.isBlank()) {
                continue;
            }
            String rol = rolRaw.trim().toUpperCase(Locale.ROOT);
            if (rol.startsWith("ROLE_")) {
                rol = rol.substring(5);
            }
            if (!ROLES_VALIDOS.contains(rol)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Rol no permitido: " + rolRaw + ". Solo se permiten USER y ADMIN");
            }
            resultado.add(rol);
        }
        return resultado;
    }

    private void validarCambioEstado(Usuario usuario, boolean nuevoActivo, String adminActual) {
        if (nuevoActivo) {
            return;
        }
        if (usuario.getUsername().equals(adminActual)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "No puedes desactivar tu propia cuenta de administrador");
        }
        if (usuario.isActivo() && usuario.getRoles().contains(ROL_ADMIN)
                && usuarioRepository.contarActivosConRol(ROL_ADMIN) <= 1) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "No se puede desactivar al último administrador activo");
        }
    }

    private void validarCambioAdmin(Usuario usuario, boolean tendraAdmin, String adminActual) {
        if (tendraAdmin) {
            return;
        }
        if (usuario.getUsername().equals(adminActual)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "No puedes quitarte el rol de administrador a ti mismo");
        }
        if (usuario.isActivo() && usuario.getRoles().contains(ROL_ADMIN)
                && usuarioRepository.contarActivosConRol(ROL_ADMIN) <= 1) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Debe permanecer al menos un administrador activo en el sistema");
        }
    }

    private UsuarioAdminDTO toDTO(Usuario usuario) {
        List<String> rolesOrdenados = usuario.getRoles().stream().sorted().toList();
        return new UsuarioAdminDTO(usuario.getId(), usuario.getUsername(), usuario.isActivo(), rolesOrdenados);
    }
}
