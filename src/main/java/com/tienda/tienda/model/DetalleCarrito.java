package com.tienda.tienda.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

/*
 * Entidad que representa un producto dentro de un carrito de compras.
 *
 * Cada DetalleCarrito vincula un carrito (Carrito) con un producto
 * (Producto) e indica cuántas unidades de ese producto se agregaron.
 *
 * Relaciones:
 *   - Muchos DetalleCarrito pertenecen a un Carrito (ManyToOne).
 *   - Muchos DetalleCarrito referencian un Producto (ManyToOne).
 *
 * @Entity  ->  indica que esta clase es una tabla en la base de datos.
 */
@Entity
public class DetalleCarrito {

    /*
     * @Id              ->  clave primaria de la tabla.
     * @GeneratedValue  ->  el id se genera automáticamente.
     *   strategy = IDENTITY ->  delega el auto-incremento a MySQL.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /*
     * Cantidad de unidades de un producto agregadas al carrito.
     *
     * Ejemplo: si el cliente agrega 3 mouse, cantidad = 3.
     *
     * @Column(nullable = false)  ->  la cantidad es obligatoria.
     */
    @Column(nullable = false)
    private int cantidad;

    /*
     * Relación ManyToOne con Carrito.
     *
     * Muchos DetalleCarrito pueden pertenecer a un mismo Carrito.
     *
     * @ManyToOne       ->  indica que varios detalles pueden
     *                      estar asociados al mismo carrito.
     * @JoinColumn      ->  especifica la columna foreign key
     *                      en la tabla DetalleCarrito.
     *   name = "carrito_id"  ->  nombre de la columna en MySQL.
     */
    @ManyToOne
    @JoinColumn(name = "carrito_id")
    private Carrito carrito;

    /*
     * Relación ManyToOne con Producto.
     *
     * Muchos DetalleCarrito pueden referenciar un mismo Producto
     * (varios clientes pueden agregar el mismo producto a su carrito).
     *
     * @ManyToOne       ->  indica que varios detalles pueden
     *                      referenciar al mismo producto.
     * @JoinColumn      ->  especifica la columna foreign key
     *                      en la tabla DetalleCarrito.
     *   name = "producto_id"  ->  nombre de la columna en MySQL.
     */
    @ManyToOne
    @JoinColumn(name = "producto_id")
    private Producto producto;

    public DetalleCarrito() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }

    public Carrito getCarrito() { return carrito; }
    public void setCarrito(Carrito carrito) { this.carrito = carrito; }

    public Producto getProducto() { return producto; }
    public void setProducto(Producto producto) { this.producto = producto; }
}
