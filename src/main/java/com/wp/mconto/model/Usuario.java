package com.wp.mconto.model;


import com.wp.mconto.model.pago.Transaccion;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;


@Entity
@Table(name="usuarios")
@EqualsAndHashCode
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;
    private String apellido;
    private String correo;
    private String password;
    private Boolean suscripcion;
    private LocalDate fechaSuscripcion;

    @OneToMany(mappedBy = "usuario")
    private List<Transaccion> transacciones;

}
