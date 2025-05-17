package com.wp.mconto.util;

import com.wp.mconto.model.Usuario;
import com.wp.mconto.model.login.Login;

import java.util.Objects;

public class ValidationFactory {


    public static Boolean validateUsuarios(Usuario usuario) {
        if (validField(usuario.getNombre()) &&
                validField(usuario.getApellido()) &&
                validField(usuario.getCorreo()) &&
                validField(usuario.getPassword())){
            return true;
        }
        return false;
    }

    public static Boolean validateLogin(Login login) {
        if (validField(login.getCorreo()) &&
                validField(login.getPwd())){
                return true;
        }
        return false;
    }

    private static Boolean validField(Object field) {
        return Objects.nonNull(field) && !field.toString().isEmpty();
    }
}
