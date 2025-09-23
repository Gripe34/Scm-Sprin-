package com.miproyecto.demo.repository;

import com.miproyecto.demo.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    Optional<Cliente> findByUsuarioIdUsuario(Long idUsuario);

    boolean existsByUsuarioIdUsuario(Long idUsuario);

    void deleteByUsuario_IdUsuario(Long idUsuario);

}
