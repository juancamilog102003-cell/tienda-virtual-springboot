package com.tienda.tienda.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import com.tienda.tienda.model.DetallePedido;
import com.tienda.tienda.repository.DetallePedidoRepository;

/*
 * Servicio que contiene la lógica para manejar los detalles
 * de los pedidos (productos comprados en cada pedido).
 *
 * @Service  ->  marca esta clase como un componente de servicio
 *               para que Spring la administre automáticamente.
 */
@Service
public class DetallePedidoService {

    /*
     * Inyección de dependencias: Spring crea una instancia de
     * DetallePedidoRepository y la asigna aquí automáticamente.
     */
    @Autowired
    private DetallePedidoRepository detallePedidoRepository;

    /*
     * Método para guardar un detalle de pedido en la base de datos.
     *
     * Si el detalle ya tiene un id (existente), lo actualiza.
     * Si no tiene id (nuevo), lo inserta.
     */
    // Método para guardar
    public void guardarDetalle(@NonNull DetallePedido detalle) {
        detallePedidoRepository.save(detalle);
    }

    /*
     * Método para listar todos los detalles de pedido registrados.
     *
     * Retorna una lista con todos los detalles o una lista vacía
     * si no hay ninguno.
     */
    // Método para listar
    public List<DetallePedido> listarDetalles() {
        return detallePedidoRepository.findAll();
    }

    /*
     * Busca un detalle de pedido por su id.
     *
     * Retorna el detalle si lo encuentra, o null si no existe.
     * Usa orElse(null) para evitar un Optional vacío.
     */
    public DetallePedido obtenerDetallePorId(@NonNull Long id) {
        return detallePedidoRepository.findById(id).orElse(null);
    }

    /*
     * Elimina un detalle de pedido por su id.
     */
    public void eliminarDetalle(@NonNull Long id) {
        detallePedidoRepository.deleteById(id);
    }
}
