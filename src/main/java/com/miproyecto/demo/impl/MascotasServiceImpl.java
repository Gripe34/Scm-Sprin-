package com.miproyecto.demo.impl;

import com.miproyecto.demo.dto.MascotasDTO;
import com.miproyecto.demo.entity.Mascotas;
import com.miproyecto.demo.entity.Usuarios;
import com.miproyecto.demo.exceptions.CustomException;
import com.miproyecto.demo.repository.MascotasRepository;
import com.miproyecto.demo.repository.UsuariosRepository;
import com.miproyecto.demo.service.MascotasService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MascotasServiceImpl implements MascotasService {

    private final MascotasRepository mascotasRepository;
    private final ModelMapper modelMapper;
    private  final UsuariosRepository usuarioRepositorio;
    private final Path rootLocation = Paths.get(System.getProperty("user.dir"), "uploads", "mascotas");

    @Autowired
    public MascotasServiceImpl(MascotasRepository mascotasRepository, ModelMapper modelMapper, UsuariosRepository usuariorepositorio){
        this.mascotasRepository = mascotasRepository;
        this.modelMapper = modelMapper;
        this.usuarioRepositorio = usuariorepositorio;
    }


    @Override
    public List<MascotasDTO> findAllMascotas() {
        List<Mascotas> mascotas = mascotasRepository.findAll();
        return mascotas.stream()
                .map(mascota ->  modelMapper.map(mascota, MascotasDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public MascotasDTO getMascotasById(Long idMascota) {
        return mascotasRepository.findById(idMascota)
                .map(mascota -> modelMapper.map(mascota, MascotasDTO.class))
                .orElseThrow(() -> new RuntimeException("Mascota no encontrada con id: " + idMascota));
    }

    @Override
    public MascotasDTO crearMascotas(MascotasDTO mascotasDTO) {
        if (mascotasDTO.getIdMascota() != null && mascotasRepository.existsById(mascotasDTO.getIdMascota())) {
            throw new CustomException("El id ya existe ");
        }
        Mascotas mascotas = modelMapper.map(mascotasDTO, Mascotas.class);
        return modelMapper.map(mascotas, MascotasDTO.class);
    }

    @Override
    public MascotasDTO updateMascotas(Long idMascota, MascotasDTO mascotasDTO) {
        Mascotas mascotasExistente = mascotasRepository.findById(idMascota)
                .orElseThrow(() -> new CustomException("Mascota no encontrada con id: " + idMascota));

        mascotasExistente.setNombre(mascotasDTO.getNombre());
        // Actualiza otros atributos aquí

        mascotasExistente = mascotasRepository.save(mascotasExistente);
        return modelMapper.map(mascotasExistente, MascotasDTO.class);
    }


    @Override
    public void deleteMascotas(Long idMascota) {
        Mascotas mascotasExistente = mascotasRepository.findById(idMascota)
                .orElseThrow(() -> new CustomException("Mascota no encontrada con id : " + idMascota));
        mascotasRepository.delete(mascotasExistente);
    }

    @Override
    public List<MascotasDTO> obtenerMascotasPorDuenoId(Long duenoId) {
        // 1. Llama al repositorio para buscar las mascotas del dueño en la BD
        List<Mascotas> mascotasDelUsuario = mascotasRepository.findByUsuario_IdUsuario(duenoId);

        // 2. Convierte la lista de entidades a una lista de DTOs para la vista
        return mascotasDelUsuario.stream()
                .map(this::convertirADTO) // Reutilizamos tu método helper, ¡perfecto!
                .collect(Collectors.toList());
    }

    private String guardarFoto(MultipartFile archivo) {
        if (archivo == null || archivo.isEmpty()) {
            return null;
        }
        try {
            // ... (aquí va la limpieza del nombre del archivo)
            String nombreOriginal = archivo.getOriginalFilename();
            String nombreLimpio = nombreOriginal.replaceAll("[^a-zA-Z0-9._-]", "");
            String nombreUnico = System.currentTimeMillis() + "_" + nombreLimpio;

            // ¡AQUÍ ESTÁ LA LÍNEA QUE BUSCAS! 👇
            // Esto crea la carpeta "uploads/mascotas" si no existe.
            Files.createDirectories(this.rootLocation);

            // Ahora que sabemos que la carpeta existe, guardamos el archivo.
            Files.copy(archivo.getInputStream(), this.rootLocation.resolve(nombreUnico));

            return nombreUnico;
        } catch (IOException e) {
            throw new CustomException("Error al guardar la foto: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public MascotasDTO crearMascota(MascotasDTO mascotaDTO) {
        Mascotas mascota = modelMapper.map(mascotaDTO, Mascotas.class);
        Usuarios usuario = usuarioRepositorio.findById(mascotaDTO.getIdUsuario())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + mascotaDTO.getIdUsuario()));
        mascota.setUsuario(usuario);

        String nombreFoto = guardarFoto(mascotaDTO.getArchivoFoto());
        mascota.setFoto(nombreFoto);

        Mascotas mascotaGuardada = mascotasRepository.save(mascota);
        return convertirADTO(mascotaGuardada);
    }

    @Override
    public List<MascotasDTO> getAllMascotas() {
        return mascotasRepository.findAll().stream().map(this::convertirADTO).collect(Collectors.toList());
    }

    private MascotasDTO convertirADTO(Mascotas mascota) {
        MascotasDTO dto = modelMapper.map(mascota, MascotasDTO.class);
        if (mascota.getUsuario() != null) {
            dto.setNombreDueno(mascota.getUsuario().getNombre() + " " + mascota.getUsuario().getApellido());
        }
        return dto;
    }


}
