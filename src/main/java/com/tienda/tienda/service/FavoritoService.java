package com.tienda.tienda.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import com.tienda.tienda.model.Favorito;
import com.tienda.tienda.model.Producto;
import com.tienda.tienda.model.Usuario;
import com.tienda.tienda.repository.FavoritoRepository;

/*
 * Servicio que contiene la lógica para manejar los productos
 * favoritos de los usuarios.
 *
 * @Service  ->  marca esta clase como un componente de servicio
 *               para que Spring la administre automáticamente.
 */
@Service
public class FavoritoService {

    /*
     * Inyección de dependencias: Spring crea una instancia de
     * FavoritoRepository y la asigna aquí automáticamente.
     */
    @Autowired
    private FavoritoRepository favoritoRepository;

    // Método para guardar favorito
    /*
     * Guarda un favorito en la base de datos.
     *
     * Si el favorito ya tiene un id (existente), lo actualiza.
     * Si no tiene id (nuevo), lo inserta.
     */
    public void guardarFavorito(@NonNull Favorito favorito) {
        favoritoRepository.save(favorito);
    }

    // Método para listar favoritos
    /*
     * Obtiene todos los favoritos de un usuario específico.
     *
     * @param usuario  el usuario del cual queremos los favoritos.
     * @return lista de favoritos del usuario (vacía si no tiene).
     */
    public List<Favorito> listarFavoritosPorUsuario(@NonNull Usuario usuario) {

        // Buscar favoritos del usuario
        return favoritoRepository.findByUsuario(usuario);
    }

    /*
     * Obtiene todos los favoritos registrados.
     *
     * Retorna una lista con todos los favoritos o una lista vacía
     * si no hay ninguno.
     */
    public List<Favorito> listarFavoritos() {
        return favoritoRepository.findAll();
    }

    /*
     * Busca un favorito por su id.
     *
     * Retorna el favorito si lo encuentra, o null si no existe.
     * Usa orElse(null) para evitar un Optional vacío.
     */
    public Favorito obtenerFavoritoPorId(@NonNull Long id) {
        return favoritoRepository.findById(id).orElse(null);
    }

    /*
     * Elimina un favorito por su id.
     */
    public void eliminarFavorito(@NonNull Long id) {
        favoritoRepository.deleteById(id);
    }

    // ------------------------------------------------------------------
    // Evitar duplicados: verifica si el usuario ya tiene el producto
    // ------------------------------------------------------------------
    /*
     * Verifica si un usuario ya tiene un producto marcado como favorito.
     *
     * 1. Busca en la base de datos por usuario y producto.
     * 2. Si encuentra un registro, retorna true (ya existe).
     * 3. Si no encuentra nada, retorna false (se puede agregar).
     *
     * @param usuario   el dueño del favorito.
     * @param producto  el producto a verificar.
     * @return true si ya existe, false si no.
     */
    public boolean existeFavorito(@NonNull Usuario usuario, @NonNull Producto producto) {

        // Buscar favorito por usuario y producto
        java.util.Optional<Favorito> favoritoExistente = favoritoRepository.findByUsuarioAndProducto(usuario, producto);

        // Si ya existe, retorna true; si no, retorna false
        return favoritoExistente.isPresent();
    }
}
