package com.miproyecto.demo.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "usuarios")
public class Usuarios {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuarios")
    private Long idUsuario;

    // Un usuario puede tener muchas mascotas
    @OneToMany(mappedBy = "usuario")
    private java.util.List<Mascotas> mascotas;

    @OneToOne(mappedBy = "usuario")
    private Cliente cliente;

    // Si un usuario está relacionado a un veterinario
    @OneToOne(mappedBy = "usuario")
    private Veterinarios veterinario;

    @Column(name = "nombre", nullable = false, length = 200)
    private String nombre;

    @Column(name = "apellido", nullable = false, length = 200)
    private String apellido;

    @Column(name = "correo", nullable = false, unique = true, length = 200)
    private String correo;

    @Column(name = "contrasena", nullable = false, length = 200)
    private String contrasena;

    @Column(name = "telefono", nullable = false, unique = true, length = 11)
    private Long telefono;

    @Column(name = "direccion", nullable = false, length = 100)
    private String direccion;

    @ManyToOne
    @JoinColumn(name = "idRol", nullable = false)
    private Roles rol;

    @Column(name = "foto", length = 200)
    private String foto;

    @Column(columnDefinition = "boolean default true")
    private boolean habilitado = true;


    // Getters
    public Long getIdUsuario() {
        return idUsuario;
    }

    public java.util.List<Mascotas> getMascotas() {
        return mascotas;
    }

    public Veterinarios getVeterinario() {
        return veterinario;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public String getCorreo() {
        return correo;
    }

    public String getContrasena() {
        return contrasena;
    }

    public Long getTelefono() {
        return telefono;
    }

    public String getDireccion() {
        return direccion;
    }

    public Roles getRol() {
        return rol;
    }

    public String getFoto() {
        return foto;
    }
    



    // Setters
    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }

    public void setMascotas(java.util.List<Mascotas> mascotas) {
        this.mascotas = mascotas;
    }

    public void setVeterinario(Veterinarios veterinario) {
        this.veterinario = veterinario;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public void setTelefono(Long telefono) {
        this.telefono = telefono;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public void setRol(Roles rol) {
        this.rol = rol;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }


    public boolean isHabilitado() {
        return habilitado;
    }
    public void setHabilitado(boolean habilitado) {
        this.habilitado = habilitado;
    }
}
