package com.wp.mconto.config;


import com.wp.mconto.model.PojoUsuario;
import com.wp.mconto.repository.UsuariosRepo;
import com.wp.mconto.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UsuariosRepo usuariosRepo;

    @Autowired
    UsuarioService usuarioService;

    public UserDetailsServiceImpl(UsuariosRepo usuariosRepo) {
        this.usuariosRepo = usuariosRepo;
    }


    @Override
    public UserDetails loadUserByUsername(String correo) {


        PojoUsuario pojoUsuario = usuarioService.findByCorreo(correo).orElse(null);


        if (pojoUsuario.getCorreo().equals(correo)) {

            return new User(
                    pojoUsuario.getCorreo(),
                    pojoUsuario.getPassword(),
                    Arrays.asList(new SimpleGrantedAuthority(pojoUsuario.getRolAppCode()))
            );
        } else {
            throw new UsernameNotFoundException("Usuario no encontrado");
        }
    }
}