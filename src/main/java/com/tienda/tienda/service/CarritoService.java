package com.tienda.tienda.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import com.tienda.tienda.model.Carrito;
import com.tienda.tienda.model.DetalleCarrito;
import com.tienda.tienda.model.Producto;
import com.tienda.tienda.model.Usuario;
import com.tienda.tienda.repository.CarritoRepository;
import com.tienda.tienda.repository.DetalleCarritoRepository;

/*
 * Servicio que contiene la lógica para manejar carritos de compras.
 *
 * @Service  ->  marca esta clase como un componente de servicio
 *               para que Spring la administre automáticamente.
 */
@Service
public class CarritoService {

    /*
     * Inyección de dependencias: Spring crea una instancia de
     * CarritoRepository y la asigna aquí automáticamente.
     */
    @Autowired
    private CarritoRepository carritoRepository;

    /*
     * Inyectamos DetalleCarritoRepository para poder guardar
     * los productos que el cliente agrega a su carrito.
     */
    @Autowired
    private DetalleCarritoRepository detalleCarritoRepository;

    /*
     * Guarda un carrito en la base de datos.
     *
     * Si el carrito ya tiene un id (existente), lo actualiza.
     * Si no tiene id (nuevo), lo inserta.
     */
    public void guardarCarrito(@NonNull Carrito carrito) {
        carritoRepository.save(carrito);
    }

    /*
     * Obtiene todos los carritos registrados.
     *
     * Retorna una lista con todos los carritos o una lista vacía
     * si no hay ninguno.
     */
    public java.util.List<Carrito> listarCarritos() {
        return carritoRepository.findAll();
    }

    /*
     * Busca un carrito por su id.
     *
     * Retorna el carrito si lo encuentra, o null si no existe.
     * Usa orElse(null) para evitar un Optional vacío.
     */
    public Carrito obtenerCarritoPorId(@NonNull Long id) {
        return carritoRepository.findById(id).orElse(null);
    }

    /*
     * Elimina un carrito por su id.
     */
    public void eliminarCarrito(@NonNull Long id) {
        carritoRepository.deleteById(id);
    }

    /*
     * Busca un carrito existente para el usuario o crea uno nuevo.
     *
     * 1. Busca en la base de datos si el usuario ya tiene un carrito.
     * 2. Si existe, lo retorna (no crea duplicados).
     * 3. Si no existe, crea uno nuevo con la fecha actual y lo guarda.
     *
     * @param usuario  el cliente dueño del carrito.
     * @return el carrito existente o el recién creado.
     */
    public Carrito obtenerOCrearCarrito(@NonNull Usuario usuario) {

        // Buscar carrito existente para este usuario
        java.util.Optional<Carrito> carritoExistente = carritoRepository.findByUsuario(usuario);

        if (carritoExistente.isPresent()) {
            // Si ya existe, lo devolvemos tal cual
            return carritoExistente.get();
        }

        // Crear nuevo carrito con la fecha y hora actual
        Carrito nuevoCarrito = new Carrito();
        nuevoCarrito.setFechaCreacion(LocalDateTime.now());
        nuevoCarrito.setUsuario(usuario);

        // Guardar el nuevo carrito en la base de datos
        return carritoRepository.save(nuevoCarrito);
    }

    /*
     * Agrega un producto al carrito del usuario con cantidad = 1.
     *
     * Pasos:
     *   1. Obtener o crear el carrito del usuario.
     *   2. Crear un nuevo DetalleCarrito.
     *   3. Asociar el carrito, el producto y la cantidad.
     *   4. Guardar el detalle en la base de datos.
     *
     * @param usuario   el cliente que va a comprar.
     * @param producto  el producto seleccionado.
     */
    public void agregarProductoAlCarrito(@NonNull Usuario usuario, @NonNull Producto producto) {

        // ---------------------------------------------------------------
        // Paso 1: Obtener o crear el carrito del usuario
        // ---------------------------------------------------------------
        Carrito carrito = obtenerOCrearCarrito(usuario);

        // ---------------------------------------------------------------
        // Paso 2: Crear un nuevo detalle para este producto
        // ---------------------------------------------------------------
        DetalleCarrito detalle = new DetalleCarrito();

        // ---------------------------------------------------------------
        // Paso 3: Asociar el carrito, el producto y la cantidad
        // ---------------------------------------------------------------
        detalle.setCarrito(carrito);
        detalle.setProducto(producto);
        detalle.setCantidad(1); // Por defecto se agrega 1 unidad

        // ---------------------------------------------------------------
        // Paso 4: Guardar el detalle en la base de datos
        // ---------------------------------------------------------------
        detalleCarritoRepository.save(detalle);
    }

    /*
     * Obtiene todos los detalles (productos) de un carrito específico.
     *
     * Llama al repositorio que ejecuta:
     *   SELECT * FROM detalle_carrito WHERE carrito_id = ?
     *
     * @param carrito  el carrito del cual queremos los productos.
     * @return lista con los detalles del carrito (vacía si no hay productos).
     */
    public java.util.List<DetalleCarrito> obtenerDetallesDelCarrito(@NonNull Carrito carrito) {
        return detalleCarritoRepository.findByCarrito(carrito);
    }

    /*
     * Calcula el total del carrito sumando (precio * cantidad) de cada producto.
     *
     * Recorre la lista de detalles y para cada uno multiplica
     * el precio del producto por la cantidad agregada.
     *
     * @param detalles  lista de DetalleCarrito con los productos.
     * @return el monto total a pagar.
     */
    public double calcularTotal(@NonNull java.util.List<DetalleCarrito> detalles) {
        double total = 0.0;

        // Recorrer cada detalle y sumar su subtotal
        for (DetalleCarrito detalle : detalles) {
            // Subtotal = precio del producto * cantidad comprada
            double subtotal = detalle.getProducto().getPrecio() * detalle.getCantidad();
            total = total + subtotal;
        }

        return total;
    }

    /*
     * Elimina un producto del carrito por su id de DetalleCarrito.
     *
     * @param detalleId  el id del detalle que se quiere eliminar.
     */
    public void eliminarDetalleDelCarrito(@NonNull Long detalleId) {
        detalleCarritoRepository.deleteById(detalleId);
    }

    // ------------------------------------------------------------------
    // Vaciar carrito: elimina todos los productos del carrito
    // ------------------------------------------------------------------
    /*
     * Elimina todos los detalles (productos) de un carrito.
     *
     * Esto se usa después de finalizar la compra para dejar
     * el carrito vacío.
     *
     * 1. Obtener todos los DetalleCarrito del carrito.
     * 2. Eliminarlos todos de la base de datos con deleteAll().
     *
     * @param carrito  el carrito que se quiere vaciar.
     */
    public void vaciarCarrito(@NonNull Carrito carrito) {

        // Obtener todos los productos del carrito
        java.util.List<DetalleCarrito> detalles = detalleCarritoRepository.findByCarrito(carrito);

        // Eliminar todos los detalles de una sola vez
        detalleCarritoRepository.deleteAll(detalles);
    }
}
