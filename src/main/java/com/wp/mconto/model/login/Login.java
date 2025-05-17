package com.wp.mconto.model.login;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Login {
    private String correo;
    private String pwd;
}
