package com.wp.mconto.controller;

//import com.wp.mconto.config.JwtService;
import com.wp.mconto.config.JwtService;
import com.wp.mconto.model.PojoUsuario;
import com.wp.mconto.model.ResponseCompose;
import com.wp.mconto.model.ResponseHTTP;
import com.wp.mconto.model.Usuario;
import com.wp.mconto.model.login.Login;
import com.wp.mconto.service.MensajeriaService;
import com.wp.mconto.service.UsuarioService;
import com.wp.mconto.util.ValidationFactory;
import com.wp.mconto.util.ValidationToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST})
@RequestMapping("/wp-mconto/auth")
public class AuthCtr {

    @Autowired
    UsuarioService usuarioService;

    @Autowired
    MensajeriaService mensajeriaService;

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @Autowired
    public AuthCtr(AuthenticationManager authenticationManager, JwtService jwtService, UserDetailsService userDetailsService) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @GetMapping("/version")
    public String version() {
        return "version 2.0 en http";
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseHTTP> login(@RequestBody Login login) {
        if (!ValidationFactory.validateLogin(login)) {
            return new ResponseEntity<>(new ResponseHTTP(HttpStatus.BAD_REQUEST.value(), "campos obligatorios requeridos"),
                    HttpStatus.BAD_REQUEST);
        }
        try {
            // Autenticación del usuario
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(login.getCorreo(), login.getPwd())
            );

            System.out.println("Usuario encontrado en base de datos ----------------");

            // Una vez autenticado, generamos el JWT
            String token = jwtService.generateToken(authentication.getName(), 36000000);

            System.out.println("Token generado ---------------------");

            PojoUsuario usuario = usuarioService.findByCorreo(login.getCorreo()).orElse(null);

            if (Objects.isNull(usuario)) {
                return new ResponseEntity<>(new ResponseHTTP(HttpStatus.FORBIDDEN.value(), null),
                        HttpStatus.FORBIDDEN);
            }

            usuario.setToken(token);
            usuario.setPassword("");

            return new ResponseEntity<>(new ResponseHTTP(HttpStatus.CREATED.value(), usuario),
                    HttpStatus.CREATED);

        } catch (BadCredentialsException e) {
            return new ResponseEntity<>(new ResponseHTTP(HttpStatus.BAD_GATEWAY.value(), null),
                    HttpStatus.BAD_GATEWAY);
        }
    }

    @PostMapping("/")
    public ResponseEntity<ResponseHTTP> guardarUsuario(@RequestBody Usuario usuarioInput) {
        if (ValidationFactory.validateUsuarios(usuarioInput)) {
            String pwd = usuarioInput.getPassword();
            try {
                ResponseCompose responseCompose = new ResponseCompose();
                PojoUsuario pojoUsuario = usuarioService.findByCorreo(usuarioInput.getCorreo()).orElse(null);

                if (Objects.nonNull(pojoUsuario)) {
                    responseCompose.setMsg("Usuario existente.");
                    return new ResponseEntity<>(new ResponseHTTP(HttpStatus.FORBIDDEN.value(), responseCompose.getMsg()),
                            HttpStatus.FORBIDDEN);
                }

                usuarioService.save(usuarioInput);
                usuarioInput.setPassword(pwd);

                String token = jwtService.generateToken(usuarioInput.getCorreo(), 36000000);

                System.out.println("Token generado ---------------------");

                PojoUsuario usuario = usuarioService.findByCorreo(usuarioInput.getCorreo()).orElse(null);
                usuario.setToken(token);
                usuario.setPassword("");

                return new ResponseEntity<>(new ResponseHTTP(HttpStatus.CREATED.value(), usuario),
                        HttpStatus.CREATED);

            } catch (Exception e) {
                return new ResponseEntity<>(new ResponseHTTP(HttpStatus.BAD_GATEWAY.value(), null),
                        HttpStatus.BAD_GATEWAY);
            }
        }
        return new ResponseEntity<>(new ResponseHTTP(HttpStatus.BAD_REQUEST.value(), "campos obligatorios requeridos"),
                HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/token/{correo}")
    public ResponseEntity<ResponseHTTP> generarToken(@PathVariable("correo") String correo) {

        if (Objects.isNull(correo)) {
            return new ResponseEntity<>(new ResponseHTTP(HttpStatus.BAD_REQUEST.value(), "campos obligatorios requeridos"),
                    HttpStatus.BAD_REQUEST);
        }

        String token = jwtService.generateToken(correo, 12000000);

        try {
            mensajeriaService.enviarCorreo(correo, token);
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseHTTP(HttpStatus.BAD_REQUEST.value(), "campos obligatorios requeridos"),
                    HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(new ResponseHTTP(HttpStatus.CREATED.value(), "token enviado"),
                HttpStatus.CREATED);
    }

    @PostMapping("/cambiar")
    public ResponseEntity<ResponseHTTP> cambiarClave(@RequestHeader(value = "Authorization", required = false) String authHeader,
                                                     @RequestBody Login nuevaClave) {

        //ValidationToken validationToken = new ValidationToken(jwtService);

        //if (validationToken.validationToken(authHeader)) {

            if (Objects.isNull(nuevaClave)) {
                return new ResponseEntity<>(new ResponseHTTP(HttpStatus.BAD_REQUEST.value(), "campos obligatorios requeridos"),
                        HttpStatus.BAD_REQUEST);
            }

            try {
                usuarioService.actualizarPassword(nuevaClave);
            } catch (Exception e) {
                return new ResponseEntity<>(new ResponseHTTP(HttpStatus.BAD_REQUEST.value(), "campos obligatorios requeridos"),
                        HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<>(new ResponseHTTP(HttpStatus.CREATED.value(), "password actualizado"),
                    HttpStatus.CREATED);

        }
        //return new ResponseEntity<>(new ResponseHTTP(HttpStatus.BAD_GATEWAY.value(), "falló la operación"),
          //      HttpStatus.BAD_GATEWAY);
    }

