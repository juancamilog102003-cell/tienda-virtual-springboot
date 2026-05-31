package com.tienda.tienda.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tienda.tienda.model.Pedido;
import com.tienda.tienda.model.Usuario;

/*
 * Repositorio para la entidad Pedido.
 *
 * Al extender JpaRepository heredamos métodos CRUD básicos:
 *   save(), findById(), findAll(), deleteById(), etc.
 *
 * JpaRepository<Pedido, Long>
 *   Pedido  ->  la entidad que maneja
 *   Long    ->  tipo de dato de la clave primaria (id)
 */
@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    /*
     * Busca todos los pedidos de un usuario específico.
     *
     * Spring Data JPA genera automáticamente la consulta:
     *   SELECT * FROM pedidos WHERE usuario_id = ?
     *
     * Retorna una lista de pedidos (vacía si el usuario no tiene compras).
     */
    List<Pedido> findByUsuario(Usuario usuario);

}
