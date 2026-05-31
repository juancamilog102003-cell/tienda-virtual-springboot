package com.tienda.tienda.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

/*
 * Entidad que representa el carrito de compras de un cliente.
 *
 * Cada carrito se crea cuando un cliente comienza a agregar
 * productos y se guarda con la fecha y hora en que fue creado.
 *
 * @Entity  ->  indica que esta clase es una tabla en la base de datos.
 */
@Entity
public class Carrito {

    /*
     * @Id              ->  clave primaria de la tabla.
     * @GeneratedValue  ->  el id se genera automáticamente.
     *   strategy = IDENTITY ->  delega el auto-incremento a MySQL.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /*
     * Fecha y hora en que se creó el carrito.
     *
     * LocalDateTime  ->  almacena fecha y hora sin zona horaria.
     *                    Ejemplo: 2026-05-29T15:30:00
     *
     * @Column(nullable = false)  ->  este campo es obligatorio.
     */
    @Column(nullable = false)
    private LocalDateTime fechaCreacion;

    /*
     * Relación ManyToOne con Usuario.
     *
     * Cada carrito pertenece a un único usuario.
     * Esto permite buscar el carrito de un cliente cuando
     * inicia sesión y quiere agregar productos.
     *
     * @ManyToOne       ->  varios carritos (uno por sesión)
     *                      pueden pertenecer al mismo usuario
     *                      (aunque en esta versión cada usuario
     *                      tiene solo un carrito activo).
     * @JoinColumn      ->  especifica la columna foreign key
     *                      en la tabla Carrito.
     *   name = "usuario_id"  ->  nombre de la columna en MySQL.
     */
    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    public Carrito() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }
}
