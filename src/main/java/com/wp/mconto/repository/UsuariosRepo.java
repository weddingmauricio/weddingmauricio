package com.wp.mconto.repository;


import com.wp.mconto.model.Usuario;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface UsuariosRepo extends CrudRepository<Usuario, Long> {

    List<Usuario> findAll();

    Optional<Usuario> findById(Long id);

    Usuario save(Usuario usuario);

    @Transactional
    @Modifying
    @Query(value = "UPDATE usuarios " +
            "       SET fecha_suscripcion = :fechaSuscripcion, " +
            "           suscripcion = :suscripcion" +
            "       WHERE id = :id", nativeQuery = true)
    int updateUsuarioActivadoYFechaSuscripcion(@Param("fechaSuscripcion") LocalDate fechaSuscripcion,
                                               @Param("id") Long id,
                                               @Param("suscripcion") Boolean suscripcion);


    /*
        @Query(value = "SELECT id, " +
                "nombre, " +
                "apellido, " +
                "correo, " +
                "rol_App_id, " +
                "rol_Cartera_id, " +
                "suscripcion, " +
                "fecha_suscripcion, " +
                "empresa_id, " +
                "password " +
                "FROM " +
                "dbo.usuarios " +
                "WHERE correo = :correo", nativeQuery = true)
        List<Object[]> findByCorreo(@Param("correo") String correo);
    */
    Optional<Usuario> findByCorreo(@Param("correo") String correo);


    @Transactional
    @Modifying
    @Query(value = "UPDATE usuarios " +
            "       SET empresa_id = :empresa_id " +
            "       WHERE id = :id", nativeQuery = true)
    int saveEmpresaId(@Param("empresa_id") Long empresa_id,
                      @Param("id") Long id);

    @Query(value = "SELECT * FROM usuarios WHERE empresa_id = :empresa_id", nativeQuery = true)
    List<Usuario> findByEmpresaId(@Param("empresa_id") Long empresa_id);

    @Transactional
    @Modifying
    @Query(value = "UPDATE usuarios " +
            "       SET password = :password " +
            "       WHERE correo = :correo", nativeQuery = true)
    int updatePassword(@Param("password") String password,
                       @Param("correo") String correo);
}
