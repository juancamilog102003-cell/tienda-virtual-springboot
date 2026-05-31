package com.tienda.tienda.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tienda.tienda.model.Usuario;

/*
 * Repositorio para la entidad Usuario.
 *
 * Al extender JpaRepository heredamos métodos CRUD básicos:
 *   save(), findById(), findAll(), deleteById(), etc.
 *
 * JpaRepository<Usuario, Long>
 *   Usuario -> la entidad que maneja
 *   Long    -> tipo de dato de la clave primaria (id)
 */
@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    /*
     * Spring Data JPA genera automáticamente la consulta
     * a partir del nombre del método.
     *
     * findByCorreo(String correo)
     *   -> busca en la tabla un usuario cuyo "correo" coincida
     *      con el parámetro recibido.
     *
     * Optional<Usuario>
     *   -> puede devolver un usuario si existe, o vacío si no.
     *      Así evitamos errores de NullPointerException.
     */
    Optional<Usuario> findByCorreo(String correo);
}
