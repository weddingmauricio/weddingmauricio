package com.wp.mconto.service;

import com.wp.mconto.model.PojoUsuario;
import com.wp.mconto.model.ResponseCompose;
import com.wp.mconto.model.Usuario;
import com.wp.mconto.model.login.Login;
import com.wp.mconto.repository.UsuariosRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UsuarioService implements IUsuarioService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UsuarioService.class);

    @Autowired
    UsuariosRepo usuariosRepo;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public ResponseCompose save(Usuario usuario) {
        ResponseCompose responseCompose = new ResponseCompose();
        String passwordEncoded = passwordEncoder.encode(usuario.getPassword());
        usuario.setPassword(passwordEncoded);

        responseCompose.setObjeto(usuariosRepo.save(usuario));
        responseCompose.setStatus(true);
        responseCompose.setMsg("Usuario almacenado correctamente");
        return responseCompose;

    }



    @Override
    public ResponseCompose verificarSuscripcion(Long id) {
        ResponseCompose responseCompose = new ResponseCompose();
        Optional<Usuario> usuarioSuscrito = usuariosRepo.findById(id);
        LocalDate fechaSuscripcion = usuarioSuscrito.get().getFechaSuscripcion();
        long diasEntre = ChronoUnit.DAYS.between(fechaSuscripcion, LocalDate.now());
        while (usuarioSuscrito.get().getSuscripcion()) {
            if (diasEntre == 350) {
                responseCompose.setMsg("La suscripción caduca en 15 días");
                responseCompose.setStatus(true);
                return responseCompose;
            } else {
                if (diasEntre > 350 && diasEntre < 365) {
                    responseCompose.setMsg("La suscripción caduca en menos de quince (15) días");
                    responseCompose.setStatus(true);
                    return responseCompose;
                } else {
                    if (diasEntre >= 365) {
                        responseCompose.setMsg("La suscripción caducó.");
                        responseCompose.setStatus(false);
                        return responseCompose;
                    }
                }
            }
            responseCompose.setMsg("La suscripción está activa.");
            responseCompose.setStatus(true);
            return responseCompose;
        }
        responseCompose.setMsg("El usuario no está activado.");
        responseCompose.setStatus(false);
        return responseCompose;
    }

    @Override
    public Optional<PojoUsuario> findByCorreo(String correo) {
        Usuario usuario = usuariosRepo.findByCorreo(correo).orElse(null);
        if (Objects.isNull(usuario)) {
            return Optional.empty();
        }
        LOGGER.info("El usuario logeado tiene correo " + usuario.getCorreo());
        PojoUsuario pojo = new PojoUsuario();
        pojo.setId(usuario.getId());
        pojo.setNombre(usuario.getNombre());
        pojo.setApellido(usuario.getApellido());
        pojo.setCorreo(usuario.getCorreo());
        pojo.setSuscripcion(usuario.getSuscripcion());
        pojo.setPassword(usuario.getPassword());
        return Optional.of(pojo);
    }

    @Override
    public PojoUsuario findById(Long id, String authHeader) {
        Usuario usuarioFound = usuariosRepo.findById(id).orElse(null);
        return PojoUsuario
                .builder()
                .id(usuarioFound.getId())
                .nombre(usuarioFound.getNombre())
                .apellido(usuarioFound.getApellido())
                .suscripcion(usuarioFound.getSuscripcion())
                .fechaSuscripcion(String.valueOf(usuarioFound.getFechaSuscripcion()))
                .correo(usuarioFound.getCorreo())
                .token(authHeader)

                .build();
    }

    @Override
    public PojoUsuario updateUsuario(Usuario usuario) {
        Usuario usuarioActualizado = usuariosRepo.findById(usuario.getId()).map(
                userNuevo -> {
                    userNuevo.setNombre(usuario.getNombre());
                    userNuevo.setApellido(usuario.getApellido());
                    userNuevo.setCorreo(usuario.getCorreo());
                    userNuevo.setPassword(usuario.getPassword());
                    userNuevo.setSuscripcion(usuario.getSuscripcion());
                    return usuariosRepo.save(userNuevo);
                }
        ).get();

        return PojoUsuario
                .builder()
                .nombre(usuarioActualizado.getNombre())
                .apellido(usuarioActualizado.getApellido())
                .correo(usuarioActualizado.getCorreo())
                .fechaSuscripcion(usuarioActualizado.getFechaSuscripcion().toString())
                .suscripcion(usuarioActualizado.getSuscripcion())
                .id(usuarioActualizado.getId())
                .build();

    }

    @Override
    public ResponseCompose actualizarPassword(Login nuevaClave) {
        ResponseCompose responseCompose = new ResponseCompose();

        Usuario usuario = usuariosRepo.findByCorreo(nuevaClave.getCorreo()).orElse(null);
        if (Objects.isNull(usuario)) {
            responseCompose.setStatus(false);
            responseCompose.setMsg("usuario no encontrado");
            return responseCompose;
        }
        String passwordEncoded = passwordEncoder.encode(nuevaClave.getPwd());
        usuario.setPassword(passwordEncoded);

        try {
            usuariosRepo.save(usuario);
            responseCompose.setStatus(true);
            responseCompose.setObjeto("Contraseña actualizada");
            responseCompose.setMsg("Procedimiento correcto");
            return responseCompose;
        } catch (Exception e) {
            responseCompose.setStatus(false);
            responseCompose.setMsg("Operacion incorrecta");
            return responseCompose;
        }
    }
}
