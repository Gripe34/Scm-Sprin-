package com.miproyecto.demo.repository;

import com.miproyecto.demo.entity.Citas;
import com.miproyecto.demo.entity.Usuarios;
import com.miproyecto.demo.entity.Veterinarios;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CitasRepository extends JpaRepository<Citas, Long> {
    Citas findByIdCita(Long idCita);
    List<Citas> findByVeterinario(Veterinarios veterinario);
    List<Citas> findByMascota_Usuario(Usuarios usuario);

}
