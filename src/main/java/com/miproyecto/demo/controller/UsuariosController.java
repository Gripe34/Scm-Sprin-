package com.miproyecto.demo.controller;

import com.miproyecto.demo.dto.ActividadFisicaDTO;
import com.miproyecto.demo.dto.UsuariosDTO;
import com.miproyecto.demo.entity.Cliente;
import com.miproyecto.demo.entity.Roles;
import com.miproyecto.demo.entity.Usuarios;
import com.miproyecto.demo.exceptions.CustomException;
import com.miproyecto.demo.repository.ClienteRepository;
import com.miproyecto.demo.repository.RolesRepository;
import com.miproyecto.demo.repository.UsuariosRepository;
import com.miproyecto.demo.service.ActividadFisicaService;
import com.miproyecto.demo.service.UsuariosService;
import org.hibernate.boot.jaxb.spi.Binding;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.bind.annotation.PostMapping;



import java.util.List;

@Controller
@RequestMapping("/usuarios")
public class UsuariosController {

    private final UsuariosService usuariosService;
    private final UsuariosRepository usuariosRepository;
    private final ModelMapper modelMapper;
    private final RolesRepository rolesRepository;
    private final PasswordEncoder passwordEncoder;
    private final ClienteRepository clienteRepository;

    @Autowired
    public UsuariosController(UsuariosService usuariosService, PasswordEncoder passwordEncoder, RolesRepository rolesRepository, ModelMapper modelMapper, UsuariosRepository usuariosRepository, ClienteRepository clienteRepository) {
        this.usuariosRepository = usuariosRepository;
        this.usuariosService = usuariosService;
        this.passwordEncoder = passwordEncoder;
        this.rolesRepository = rolesRepository;
        this.modelMapper = modelMapper;
        this.clienteRepository = clienteRepository;
    }




    // Obtener todos los usuarios
    @GetMapping("/all")
    public ResponseEntity<List<UsuariosDTO>> getAllUsuarios() {
        List<UsuariosDTO> usuarios = usuariosService.findAllUsuarios();
        return ResponseEntity.ok(usuarios);
    }

    // Obtener usuario por ID
    @GetMapping("/{id}")
    public ResponseEntity<UsuariosDTO> getUsuarioById(@PathVariable Long id) {
        UsuariosDTO usuario = usuariosService.getUsuariosById(id);
        return ResponseEntity.ok(usuario);
    }

    // Crear nuevo usuario
    @PostMapping
    public ResponseEntity<UsuariosDTO> crearUsuario(@RequestBody UsuariosDTO dto) {
        UsuariosDTO nuevoUsuario = usuariosService.crearUsuario(dto);
        return ResponseEntity.ok(nuevoUsuario);
    }

    // Actualizar usuario existente
    @PutMapping("/{id}")
    public ResponseEntity<UsuariosDTO> actualizarUsuario(@PathVariable Long id, @RequestBody UsuariosDTO dto) {
        UsuariosDTO actualizado = usuariosService.updateUsuarios(id, dto);
        return ResponseEntity.ok(actualizado);
    }

    // Eliminar usuario
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarUsuario(@PathVariable Long id) {
        usuariosService.deleteUsuarios(id);
        return ResponseEntity.noContent().build();
    }




    //VISTAS-------------------------------------------------------------------
    //Mostrar formulario de creacion
    @GetMapping("/crear")
    public String mostrarFormularioCrear(Model model) {
        // Si no hay atributo 'usuario' (por ejemplo, no viene de un submit con errores),
        // ponemos uno vacío para el formulario
        if (!model.containsAttribute("usuario")) {
            model.addAttribute("usuario", new UsuariosDTO());
        }

        // lista para la tabla
        model.addAttribute("usuarios", usuariosService.findAllUsuarios());

        // Thymeleaf buscará src/main/resources/templates/usuarios/crear.html
        return "usuarios/crear";
    }


    //Porcesar formulario de creacion
    @PostMapping("/crear")
    public String crearUsuarioForm(@ModelAttribute UsuariosDTO usuarioDTO) {

        // Encriptar contraseña
        String encodedPassword = passwordEncoder.encode(usuarioDTO.getContrasena());
        usuarioDTO.setContrasena(encodedPassword);

        // Mapear DTO a entidad Usuarios
        Usuarios usuario = modelMapper.map(usuarioDTO, Usuarios.class);

        // Asignar rol CLIENTE automáticamente
        Roles rolCliente = rolesRepository.findByIdRol(2L)
                .orElseThrow(() -> new CustomException("Rol no encontrado"));
        usuario.setRol(rolCliente);

        // Guardar usuario primero
        Usuarios usuarioGuardado = usuariosRepository.save(usuario);
        usuario.setHabilitado(true);

        // Crear la entidad Cliente vinculada al usuario recién guardado
        Cliente cliente = new Cliente();
        cliente.setUsuario(usuarioGuardado);
        cliente.setDireccion(usuarioDTO.getDireccion());
        cliente.setTelefono(usuarioDTO.getTelefono());


        // Guardar Cliente
        clienteRepository.save(cliente);

        return "redirect:/login";
    }



    // Listar usuarios
    @GetMapping("/listar")
    public String listarUsuarios(Model model) {
        model.addAttribute("usuarios", usuariosService.findAllUsuarios());
        return "usuarios/listar"; // templates/usuarios/listar.html
    }

    //Eliminar usuario
    @PostMapping("/eliminar/{id}")
    public String eliminarUsuario(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        usuariosService.deleteUsuarios(id);
        redirectAttributes.addFlashAttribute("successMessage", "Usuario eliminado exitosamente");
        return "redirect:/admin/gestionar-usuarios";
    }

  //EDITAR USUARIO
    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Long id, Model model) {
        UsuariosDTO usuario = usuariosService.getUsuariosById(id);
        model.addAttribute("usuario", usuario);

        List<Roles> roles = rolesRepository.findAll();
        model.addAttribute("roles", roles);

        return "usuarios/editar";
    }

    @PostMapping("/editar/{id}")
    public String actualizarUsuario(@PathVariable Long id,
            @ModelAttribute("usuario") UsuariosDTO usuarioDTO,
            BindingResult result,
            RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            return "usuarios/editar";
        }

        usuariosService.updateUsuarios(id, usuarioDTO);
        redirectAttributes.addFlashAttribute("successMessage", "Usuario actualizado exitosamente");

        return "redirect:/admin/gestionar-usuarios";
    }


    @PostMapping("/bloquear/{id}")
    public String bloquearUsuario(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            usuariosService.bloquearUsuario(id);
            redirectAttributes.addFlashAttribute("successMessage", "Estado del usuario actualizado exitosamente.");
        } catch (CustomException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        // Redirige de vuelta a la vista de gestión de usuarios
        return "redirect:/admin/gestionar-usuarios";
    }

}
