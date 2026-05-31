package com.tienda.tienda.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/*
 * Entidad que representa un usuario del sistema.
 * 
 * @Entity  ->  indica que esta clase es una tabla en la base de datos.
 * @Table   ->  especifica el nombre de la tabla (opcional, si se omite
 *              usa el nombre de la clase: "Usuario").
 */
@Entity
@Table(name = "usuarios")
public class Usuario {

    /*
     * @Id              ->  clave primaria de la tabla.
     * @GeneratedValue  ->  el id se genera automáticamente.
     *   strategy = IDENTITY ->  delega el auto-incremento a MySQL.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /*
     * @NotBlank  ->  valida que el campo no sea null, ni vacío, ni
     *                solo espacios en blanco.
     * @Size      ->  limita la cantidad de caracteres.
     * @Column    ->  configura la columna en la base de datos
     *   (nullable = false  ->  la columna no acepta NULL).
     */
    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
    @Column(nullable = false, length = 100)
    private String nombre;

    /*
     * @Email      ->  valida que el formato sea un correo válido.
     * @Column     ->  unique = true  ->  no pueden existir dos
     *                usuarios con el mismo correo.
     */
    @NotBlank(message = "El correo es obligatorio")
    @Email(message = "Debe ingresar un correo electrónico válido")
    @Column(nullable = false, unique = true, length = 150)
    private String correo;

    /*
     * @NotBlank  ->  la contraseña no puede estar vacía.
     * @Size      ->  mínimo 6 caracteres por seguridad.
     */
    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 6, max = 60, message = "La contraseña debe tener entre 6 y 60 caracteres")
    @Column(nullable = false, length = 60)
    private String password;

    /*
     * Rol del usuario dentro del sistema.
     *
     * String        ->  almacena el nombre del rol como texto.
     * "ADMIN"       ->  usuario administrador con acceso completo.
     * "CLIENTE"     ->  usuario cliente con acceso limitado.
     *
     * @Column       ->  nullable = false  ->  el rol es obligatorio.
     *                   length = 20       ->  espacio suficiente para
     *                                        "ADMIN" o "CLIENTE".
     */
    @Column(nullable = false, length = 20)
    private String rol;

    // ------------------------------------------------------------
    // Constructor vacío requerido por JPA
    // ------------------------------------------------------------
    public Usuario() {
    }

    // ------------------------------------------------------------
    // Getters y Setters
    // ------------------------------------------------------------

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    // ------------------------------------------------------------
    // Getter y Setter de rol
    // ------------------------------------------------------------

    /*
     * Devuelve el rol del usuario.
     * Puede ser "ADMIN" o "CLIENTE".
     */
    public String getRol() {
        return rol;
    }

    /*
     * Asigna el rol al usuario.
     * Debe recibir "ADMIN" o "CLIENTE".
     */
    public void setRol(String rol) {
        this.rol = rol;
    }
}
