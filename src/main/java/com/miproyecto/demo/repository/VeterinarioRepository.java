package com.miproyecto.demo.repository;

import com.miproyecto.demo.entity.Veterinarios;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VeterinarioRepository extends JpaRepository<Veterinarios, Long> {
    Veterinarios findByIdVeterinario(Long idVeterinario);
}
