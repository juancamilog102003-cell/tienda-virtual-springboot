package com.tienda.tienda.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/*
 * Entidad que representa un producto dentro de un pedido.
 * (Creación de la entidad)
 *
 * Cada DetallePedido vincula un pedido (Pedido) con un producto
 * (Producto) e indica cuántas unidades de ese producto se compraron
 * y el subtotal correspondiente.
 *
 * Relaciones:
 *   - Muchos DetallePedido pertenecen a un Pedido (ManyToOne).
 *   - Muchos DetallePedido referencian un Producto (ManyToOne).
 *
 * @Entity  ->  indica que esta clase es una tabla en la base de datos.
 * @Table   ->  especifica el nombre de la tabla en MySQL.
 *              Usamos "detalle_pedido" para mantener consistencia
 *              con las tablas "pedidos" y "usuarios".
 */
@Entity
@Table(name = "detalle_pedido")
public class DetallePedido {

    /*
     * @Id              ->  clave primaria de la tabla.
     * @GeneratedValue  ->  el id se genera automáticamente.
     *   strategy = IDENTITY ->  delega el auto-incremento a MySQL.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /*
     * Cantidad de unidades de un producto compradas en el pedido.
     *
     * Ejemplo: si el cliente compra 3 mouse, cantidad = 3.
     *
     * @Column(nullable = false)  ->  la cantidad es obligatoria.
     */
    @Column(nullable = false)
    private int cantidad;

    /*
     * Subtotal calculado para este detalle (precio * cantidad).
     *
     * Se almacena directamente para conservar el valor histórico
     * aunque el precio del producto cambie en el futuro.
     */
    @Column(nullable = false)
    private double subtotal;

    /*
     * Relación ManyToOne con Pedido. (Relación con Pedido)
     *
     * Muchos DetallePedido pueden pertenecer a un mismo Pedido.
     * Es decir, un pedido puede tener varios productos diferentes,
     * y cada producto aparece como un DetallePedido distinto.
     *
     * @ManyToOne       ->  indica que varios detalles pueden
     *                      estar asociados al mismo pedido.
     * @JoinColumn      ->  especifica la columna foreign key
     *                      en la tabla DetallePedido.
     *   name = "pedido_id"  ->  nombre de la columna en MySQL.
     */
    @ManyToOne
    @JoinColumn(name = "pedido_id")
    private Pedido pedido;

    /*
     * Relación ManyToOne con Producto. (Relación con Producto)
     *
     * Muchos DetallePedido pueden referenciar un mismo Producto
     * (varios clientes pueden comprar el mismo producto en
     * diferentes pedidos).
     *
     * @ManyToOne       ->  indica que varios detalles pueden
     *                      referenciar al mismo producto.
     * @JoinColumn      ->  especifica la columna foreign key
     *                      en la tabla DetallePedido.
     *   name = "producto_id"  ->  nombre de la columna en MySQL.
     */
    @ManyToOne
    @JoinColumn(name = "producto_id")
    private Producto producto;

    public DetallePedido() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }

    public double getSubtotal() { return subtotal; }
    public void setSubtotal(double subtotal) { this.subtotal = subtotal; }

    public Pedido getPedido() { return pedido; }
    public void setPedido(Pedido pedido) { this.pedido = pedido; }

    public Producto getProducto() { return producto; }
    public void setProducto(Producto producto) { this.producto = producto; }
}
