package com.miproyecto.demo.repository;

import com.miproyecto.demo.entity.Usuarios;
import com.miproyecto.demo.entity.Veterinarios;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VeterinarioRepository extends JpaRepository<Veterinarios, Long> {
    Veterinarios findByIdVeterinario(Long idVeterinario);
    Optional<Veterinarios> findByUsuario(Usuarios usuario);
}
