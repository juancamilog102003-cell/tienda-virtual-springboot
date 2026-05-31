package com.tienda.tienda.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/*
 * Entidad que representa un pedido realizado por un usuario.
 *
 * Cuando un cliente finaliza su compra, se genera un Pedido
 * con la fecha y el total calculado a partir de los productos
 * que agregó al carrito.
 *
 * Relación:
 *   - Muchos Pedidos pertenecen a un Usuario (ManyToOne).
 *
 * @Entity  ->  indica que esta clase es una tabla en la base de datos.
 * @Table   ->  especifica el nombre de la tabla en MySQL.
 *              Usamos "pedidos" (plural) para mantener consistencia
 *              con la tabla "usuarios" de la entidad Usuario.
 */
@Entity
@Table(name = "pedidos")
public class Pedido {

    /*
     * @Id              ->  clave primaria de la tabla.
     * @GeneratedValue  ->  el id se genera automáticamente.
     *   strategy = IDENTITY ->  delega el auto-incremento a MySQL.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /*
     * Fecha y hora en que se realizó el pedido.
     *
     * LocalDateTime  ->  almacena fecha y hora sin zona horaria.
     *                    Ejemplo: 2026-05-29T15:30:00
     *
     * @Column(nullable = false)  ->  este campo es obligatorio.
     */
    @Column(nullable = false)
    private LocalDateTime fechaPedido;

    /*
     * Monto total del pedido en dólares (o la moneda configurada).
     *
     * Se calcula sumando el precio de cada producto multiplicado
     * por su cantidad.
     *
     * @Column(nullable = false)  ->  el total es obligatorio.
     */
    @Column(nullable = false)
    private double total;

    /*
     * Relación ManyToOne con Usuario.
     *
     * Muchos Pedidos pueden pertenecer a un mismo Usuario
     * (un cliente puede hacer varias compras).
     *
     * @ManyToOne       ->  indica que varios pedidos pueden
     *                      estar asociados al mismo usuario.
     * @JoinColumn      ->  especifica la columna foreign key
     *                      en la tabla Pedido.
     *   name = "usuario_id"  ->  nombre de la columna en MySQL.
     */
    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    public Pedido() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LocalDateTime getFechaPedido() { return fechaPedido; }
    public void setFechaPedido(LocalDateTime fechaPedido) { this.fechaPedido = fechaPedido; }

    public double getTotal() { return total; }
    public void setTotal(double total) { this.total = total; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }
}
