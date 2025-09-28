package com.miproyecto.demo.repository;

import com.miproyecto.demo.entity.Usuarios;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuariosRepository extends JpaRepository<Usuarios, Long> {
    Usuarios findByIdUsuario(Long idUsuario);

    Optional<Usuarios> findBycorreo(String correo);
    List<Usuarios> findByRolIdRol(Long idRol);

    @Query("SELECT DISTINCT d.mascota.usuario FROM DiagnosticoDueno d WHERE d.veterinario.idVeterinario = :idVeterinario")

    List<Usuarios> findDuenosByVeterinarioId(@Param("idVeterinario") Long idVeterinario);

    Page<Usuarios> findByRol_Rol(String rol, Pageable pageable);

    boolean findByCorreo(String gonorrea);
    boolean existsByCorreo(String correo);
    @Query("SELECT DISTINCT d.mascota.usuario FROM DiagnosticoDueno d " +
            "WHERE d.veterinario.idVeterinario = :idVeterinario " +
            "AND (:#{#nombre} IS NULL OR :#{#nombre} = '' OR LOWER(d.mascota.usuario.nombre) LIKE LOWER(CONCAT('%', :nombre, '%'))) " +
            "AND (:#{#apellido} IS NULL OR :#{#apellido} = '' OR LOWER(d.mascota.usuario.apellido) LIKE LOWER(CONCAT('%', :apellido, '%'))) " +
            "AND (:#{#correo} IS NULL OR :#{#correo} = '' OR LOWER(d.mascota.usuario.correo) LIKE LOWER(CONCAT('%', :correo, '%')))")
    List<Usuarios> findDuenosByVeterinarioConFiltros(@Param("idVeterinario") Long idVeterinario,
                                                     @Param("nombre") String nombre,
                                                     @Param("apellido") String apellido,
                                                     @Param("correo") String correo);

}