package com.tienda.tienda.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tienda.tienda.model.DetallePedido;

/*
 * Repositorio para la entidad DetallePedido.
 *
 * Al extender JpaRepository heredamos métodos CRUD básicos:
 *   save(), findById(), findAll(), deleteById(), etc.
 *
 * JpaRepository<DetallePedido, Long>
 *   DetallePedido  ->  la entidad que maneja
 *   Long           ->  tipo de dato de la clave primaria (id)
 */
@Repository
public interface DetallePedidoRepository extends JpaRepository<DetallePedido, Long> {

}
