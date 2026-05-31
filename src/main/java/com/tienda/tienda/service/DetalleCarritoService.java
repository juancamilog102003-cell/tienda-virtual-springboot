package com.tienda.tienda.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import com.tienda.tienda.model.DetalleCarrito;
import com.tienda.tienda.repository.DetalleCarritoRepository;

/*
 * Servicio que contiene la lógica para manejar los detalles
 * del carrito de compras (productos agregados al carrito).
 *
 * @Service  ->  marca esta clase como un componente de servicio
 *               para que Spring la administre automáticamente.
 */
@Service
public class DetalleCarritoService {

    /*
     * Inyección de dependencias: Spring crea una instancia de
     * DetalleCarritoRepository y la asigna aquí automáticamente.
     */
    @Autowired
    private DetalleCarritoRepository detalleCarritoRepository;

    /*
     * Guarda un detalle de carrito en la base de datos.
     *
     * Si el detalle ya tiene un id (existente), lo actualiza.
     * Si no tiene id (nuevo), lo inserta.
     */
    public void guardarDetalle(@NonNull DetalleCarrito detalle) {
        detalleCarritoRepository.save(detalle);
    }

    /*
     * Obtiene todos los detalles de carrito registrados.
     *
     * Retorna una lista con todos los detalles o una lista vacía
     * si no hay ninguno.
     */
    public List<DetalleCarrito> listarDetalles() {
        return detalleCarritoRepository.findAll();
    }

    /*
     * Busca un detalle de carrito por su id.
     *
     * Retorna el detalle si lo encuentra, o null si no existe.
     * Usa orElse(null) para evitar un Optional vacío.
     */
    public DetalleCarrito obtenerDetallePorId(@NonNull Long id) {
        return detalleCarritoRepository.findById(id).orElse(null);
    }

    /*
     * Elimina un detalle de carrito por su id.
     */
    public void eliminarDetalle(@NonNull Long id) {
        detalleCarritoRepository.deleteById(id);
    }
}
