package com.tienda.tienda.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tienda.tienda.model.Carrito;
import com.tienda.tienda.model.Usuario;

/*
 * Repositorio para la entidad Carrito.
 *
 * Al extender JpaRepository heredamos métodos CRUD básicos:
 *   save(), findById(), findAll(), deleteById(), etc.
 *
 * JpaRepository<Carrito, Long>
 *   Carrito -> la entidad que maneja
 *   Long    -> tipo de dato de la clave primaria (id)
 */
@Repository
public interface CarritoRepository extends JpaRepository<Carrito, Long> {

    /*
     * Busca un carrito por el usuario al que pertenece.
     *
     * Spring Data JPA genera automáticamente la consulta:
     *   SELECT * FROM carrito WHERE usuario_id = ?
     *
     * Optional<Carrito>  ->  si el usuario ya tiene un carrito
     *                        lo devuelve, si no, devuelve vacío.
     * Así evitamos NullPointerException.
     */
    Optional<Carrito> findByUsuario(Usuario usuario);

}
