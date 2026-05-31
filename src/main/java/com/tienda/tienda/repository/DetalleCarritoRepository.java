package com.tienda.tienda.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tienda.tienda.model.Carrito;
import com.tienda.tienda.model.DetalleCarrito;

/*
 * Repositorio para la entidad DetalleCarrito.
 *
 * Al extender JpaRepository heredamos métodos CRUD básicos:
 *   save(), findById(), findAll(), deleteById(), etc.
 *
 * JpaRepository<DetalleCarrito, Long>
 *   DetalleCarrito  ->  la entidad que maneja
 *   Long            ->  tipo de dato de la clave primaria (id)
 */
@Repository
public interface DetalleCarritoRepository extends JpaRepository<DetalleCarrito, Long> {

    /*
     * Busca todos los detalles (productos) que pertenecen a un carrito.
     *
     * Spring Data JPA genera automáticamente la consulta:
     *   SELECT * FROM detalle_carrito WHERE carrito_id = ?
     *
     * @param carrito  el carrito del cual queremos los productos.
     * @return lista de DetalleCarrito con los productos agregados.
     */
    List<DetalleCarrito> findByCarrito(Carrito carrito);

}
