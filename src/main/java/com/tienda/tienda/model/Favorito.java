package com.tienda.tienda.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/*
 * Entidad que representa un producto favorito de un usuario.
 *
 * Cada Favorito vincula un usuario (Usuario) con un producto
 * (Producto) que el cliente ha marcado como favorito.
 *
 * Relaciones:
 *   - Muchos Favoritos pertenecen a un Usuario (ManyToOne).
 *   - Muchos Favoritos referencian un Producto (ManyToOne).
 *
 * @Entity  ->  indica que esta clase es una tabla en la base de datos.
 *
 * @Table(name = "favorito")  ->  especifica el nombre exacto de la
 *                                 tabla en MySQL. Sin esta anotación
 *                                 Hibernate podría generar un nombre
 *                                 distinto (ej. Favorito con mayúscula).
 */
@Entity
@Table(name = "favorito")
public class Favorito {

    // Creación de la entidad
    /*
     * @Id              ->  clave primaria de la tabla.
     * @GeneratedValue  ->  el id se genera automáticamente.
     *   strategy = IDENTITY ->  delega el auto-incremento a MySQL.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Relación con Usuario
    /*
     * Relación ManyToOne con Usuario.
     *
     * Muchos Favoritos pueden pertenecer a un mismo Usuario
     * (un cliente puede tener varios productos favoritos).
     *
     * @ManyToOne       ->  indica que varios favoritos pueden
     *                      estar asociados al mismo usuario.
     * @JoinColumn      ->  especifica la columna foreign key
     *                      en la tabla Favorito.
     *   name = "usuario_id"  ->  nombre de la columna en MySQL.
     */
    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    // Relación con Producto
    /*
     * Relación ManyToOne con Producto.
     *
     * Muchos Favoritos pueden referenciar un mismo Producto
     * (varios clientes pueden marcar el mismo producto como favorito).
     *
     * @ManyToOne       ->  indica que varios favoritos pueden
     *                      referenciar al mismo producto.
     * @JoinColumn      ->  especifica la columna foreign key
     *                      en la tabla Favorito.
     *   name = "producto_id"  ->  nombre de la columna en MySQL.
     */
    @ManyToOne
    @JoinColumn(name = "producto_id")
    private Producto producto;

    public Favorito() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    public Producto getProducto() { return producto; }
    public void setProducto(Producto producto) { this.producto = producto; }
}
