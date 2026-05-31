package com.tienda.tienda.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tienda.tienda.model.Favorito;
import com.tienda.tienda.model.Producto;
import com.tienda.tienda.model.Usuario;

/*
 * Repositorio para la entidad Favorito.
 *
 * Al extender JpaRepository heredamos métodos CRUD básicos:
 *   save(), findById(), findAll(), deleteById(), etc.
 *
 * JpaRepository<Favorito, Long>
 *   Favorito  ->  la entidad que maneja
 *   Long      ->  tipo de dato de la clave primaria (id)
 */
@Repository
public interface FavoritoRepository extends JpaRepository<Favorito, Long> {

    /*
     * Busca todos los favoritos de un usuario específico.
     *
     * Spring Data JPA genera automáticamente la consulta:
     *   SELECT * FROM favorito WHERE usuario_id = ?
     *
     * @param usuario  el usuario del cual queremos los favoritos.
     * @return lista de Favorito con los productos marcados.
     */
    List<Favorito> findByUsuario(Usuario usuario);

    /*
     * Busca un favorito específico de un usuario para un producto.
     *
     * Se usa para evitar duplicados: antes de guardar un favorito
     * verificamos si ya existe ese mismo usuario + producto.
     *
     * Spring Data JPA genera la consulta:
     *   SELECT * FROM favorito WHERE usuario_id = ? AND producto_id = ?
     *
     * Retorna el favorito si existe, o un Optional vacío si no.
     */
    java.util.Optional<Favorito> findByUsuarioAndProducto(Usuario usuario, Producto producto);

}
