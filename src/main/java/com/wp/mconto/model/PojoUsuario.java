package com.wp.mconto.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PojoUsuario {

    private Long id;
    private String nombre;
    private String apellido;
    private String correo;
    private String rolCarteraCode;
    private String rolAppCode;
    private Boolean suscripcion;
    private String empresa;
    private String empresaCorreo;
    private String fechaSuscripcion;
    private String token;
    private String password;


}
