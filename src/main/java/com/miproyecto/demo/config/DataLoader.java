package com.miproyecto.demo.config;

import com.miproyecto.demo.entity.Roles;
import com.miproyecto.demo.repository.RolesRepository;
import com.miproyecto.demo.repository.UsuariosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private  final RolesRepository rolesRepository;
    @Autowired
    private final PasswordEncoder passwordEncoder;
    @Autowired
    private  final UsuariosRepository userRepository;

    public DataLoader(RolesRepository rolesRepository, PasswordEncoder passwordEncoder, UsuariosRepository userRepository) {
        this.rolesRepository = rolesRepository;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }


    @Override
    public void run(String... args) throws Exception {
        if (rolesRepository.count()==0){
            System.out.println("No existen roles, creando roles iniciales...");
            // Usando los nombres exactos de tu base de datos
            rolesRepository.save(new Roles( 1L, "administrador"));
            rolesRepository.save(new Roles(2L, "cliente"));
            rolesRepository.save(new Roles(3L, "veterinario"));
            System.out.println("Roles creados.");
        }
        //administrador creador

        if (!userRepository.existsByCorreo("jhoani@gmail.com")) {
            System.out.println("Usuario administrador no encontrado, creando admin...");
            com.miproyecto.demo.entity.Roles adminRol = rolesRepository.findByRol("administrador")
                    .orElseThrow(() -> new RuntimeException("Error: Rol 'administrador' no encontrado."));
            com.miproyecto.demo.entity.Usuarios admin = new com.miproyecto.demo.entity.Usuarios();
            admin.setNombre("jhoani");
            admin.setApellido("roscon");
            admin.setCorreo("jhoani@gmail.com");
            admin.setContrasena(passwordEncoder.encode("1234"));
            admin.setTelefono(3122002134L);
            admin.setDireccion("Dirección de Administrador");
            admin.setRol(adminRol);
            userRepository.save(admin);

            System.out.println("Usuario administrador creado exitosamente.");
        }














    }
}
