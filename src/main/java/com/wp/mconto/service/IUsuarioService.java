package com.wp.mconto.service;


import com.wp.mconto.model.PojoUsuario;
import com.wp.mconto.model.ResponseCompose;
import com.wp.mconto.model.Usuario;
import com.wp.mconto.model.login.Login;

import java.util.Optional;

public interface IUsuarioService {


    ResponseCompose save(Usuario usuario);

    ResponseCompose actualizarPassword(Login nuevaClave);
    ResponseCompose verificarSuscripcion(Long id);
    Optional<PojoUsuario> findByCorreo(String correo);
    PojoUsuario updateUsuario(Usuario usuario);
    PojoUsuario findById(Long id, String authHeader);

}
