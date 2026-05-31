package com.tienda.tienda.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import com.tienda.tienda.model.Pedido;
import com.tienda.tienda.model.Usuario;
import com.tienda.tienda.repository.PedidoRepository;

/*
 * Servicio que contiene la lógica para manejar los pedidos
 * realizados por los usuarios.
 *
 * @Service  ->  marca esta clase como un componente de servicio
 *               para que Spring la administre automáticamente.
 */
@Service
public class PedidoService {

    /*
     * Inyección de dependencias: Spring crea una instancia de
     * PedidoRepository y la asigna aquí automáticamente.
     */
    @Autowired
    private PedidoRepository pedidoRepository;

    /*
     * Guarda un pedido en la base de datos.
     *
     * Si el pedido ya tiene un id (existente), lo actualiza.
     * Si no tiene id (nuevo), lo inserta.
     */
    public void guardarPedido(@NonNull Pedido pedido) {
        pedidoRepository.save(pedido);
    }

    /*
     * Obtiene todos los pedidos registrados.
     *
     * Retorna una lista con todos los pedidos o una lista vacía
     * si no hay ninguno.
     */
    public List<Pedido> listarPedidos() {
        return pedidoRepository.findAll();
    }

    /*
     * Busca un pedido por su id.
     *
     * Retorna el pedido si lo encuentra, o null si no existe.
     * Usa orElse(null) para evitar un Optional vacío.
     */
    public Pedido obtenerPedidoPorId(@NonNull Long id) {
        return pedidoRepository.findById(id).orElse(null);
    }

    /*
     * Elimina un pedido por su id.
     */
    public void eliminarPedido(@NonNull Long id) {
        pedidoRepository.deleteById(id);
    }

    // ------------------------------------------------------------------
    // Buscar pedidos por usuario
    // ------------------------------------------------------------------
    /*
     * Obtiene todos los pedidos realizados por un usuario específico.
     *
     * Consulta la base de datos filtrando por el campo "usuario"
     * (la relación ManyToOne con la entidad Usuario).
     *
     * @param usuario  el cliente dueño de los pedidos.
     * @return lista de pedidos del usuario (vacía si nunca compró).
     */
    public List<Pedido> buscarPedidosPorUsuario(@NonNull Usuario usuario) {

        // Buscar pedidos del usuario
        return pedidoRepository.findByUsuario(usuario);
    }
}
