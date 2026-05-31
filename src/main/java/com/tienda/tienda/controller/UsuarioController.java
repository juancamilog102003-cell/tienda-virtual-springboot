package com.tienda.tienda.controller;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.tienda.tienda.model.Carrito;
import com.tienda.tienda.model.DetalleCarrito;
import com.tienda.tienda.model.DetallePedido;
import com.tienda.tienda.model.Favorito;
import com.tienda.tienda.model.Pedido;
import com.tienda.tienda.model.Producto;
import com.tienda.tienda.model.Usuario;
import com.tienda.tienda.service.CarritoService;
import com.tienda.tienda.service.DetallePedidoService;
import com.tienda.tienda.service.FavoritoService;
import com.tienda.tienda.service.PedidoService;
import com.tienda.tienda.service.ProductoService;
import com.tienda.tienda.service.UsuarioService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PathVariable;

/*
 * Controlador que maneja el registro de nuevos usuarios.
 *
 * @Controller  ->  indica que esta clase es un controlador
 *                  de Spring MVC (devuelve vistas, no JSON).
 */
@Controller
public class UsuarioController {

    /*
     * Inyectamos el servicio para poder guardar y consultar usuarios.
     */
    @Autowired
    private UsuarioService usuarioService;

    /*
     * Inyectamos el servicio de Producto para poder obtener
     * la lista de productos y mostrarla en la vista tienda.html.
     */
    @Autowired
    private ProductoService productoService;

    /*
     * Inyectamos el servicio de Carrito para poder agregar
     * productos al carrito del cliente.
     */
    @Autowired
    private CarritoService carritoService;

    /*
     * Inyectamos el servicio de Pedido para guardar la compra.
     */
    @Autowired
    private PedidoService pedidoService;

    /*
     * Inyectamos el servicio de DetallePedido para guardar
     * cada producto del pedido.
     */
    @Autowired
    private DetallePedidoService detallePedidoService;

    /*
     * Inyectamos el servicio de Favorito para manejar
     * los productos favoritos del usuario.
     */
    @Autowired
    private FavoritoService favoritoService;

    /*
     * Muestra el formulario de inicio de sesión.
     */
    @GetMapping("/login")
    public String mostrarFormularioLogin(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "login";
    }

    /*
     * Procesa el inicio de sesión enviado desde el formulario.
     *
     * @ModelAttribute Usuario  ->  Spring llena este objeto con los
     *                              datos del formulario (correo y password).
     *                              NO usamos @Valid porque aquí no queremos
     *                              las validaciones de JPA (@NotBlank, etc.),
     *                              sino nuestra propia lógica manual.
     *
     * HttpSession session      ->  objeto que nos permite guardar datos
     *                              en la sesión HTTP del usuario.
     *                              Mientras el navegador mantenga la sesión
     *                              (por cookie JSESSIONID), los datos persistirán.
     *
     * 1. Buscamos el usuario por correo en la base de datos.
     * 2. Si NO existe o la contraseña NO coincide -> volvemos al login con error.
     * 3. Si todo está bien -> guardamos el usuario en la sesión y redirigimos.
     */
    @PostMapping("/login")
    public String procesarLogin(
            @ModelAttribute Usuario usuario,
            HttpSession session,
            Model model) {

        // ---------------------------------------------------------------
        // Paso 1: Buscar usuario por correo en la base de datos
        // ---------------------------------------------------------------
        // Llamamos al servicio que internamente usa el repositorio
        // y ejecuta la consulta: SELECT * FROM usuarios WHERE correo = ?
        Usuario usuarioBD = usuarioService.buscarPorCorreo(usuario.getCorreo());

        // ---------------------------------------------------------------
        // Paso 2: Verificar credenciales (comparación manual)
        // ---------------------------------------------------------------
        // Si el usuario NO existe (null) o la contraseña no coincide,
        // agregamos un mensaje de error al modelo y regresamos al formulario.
        if (usuarioBD == null || !usuarioBD.getPassword().equals(usuario.getPassword())) {

            // Agregamos el mensaje de error para que el formulario lo muestre
            // en el bloque <div th:if="${error}"> de login.html
            model.addAttribute("error", "Correo o contraseña incorrectos");

            // Devolvemos "login" para que Thymeleaf renderice
            // nuevamente la página login.html con el error visible.
            return "login";
        }

        // ---------------------------------------------------------------
        // Paso 3: Guardar usuario en la sesión HTTP
        // ---------------------------------------------------------------
        // session.setAttribute("clave", valor)  ->  guarda un objeto en
        // la sesión del usuario. Mientras la sesión esté activa, podremos
        // recuperarlo desde cualquier controlador con:
        //   session.getAttribute("usuario");
        session.setAttribute("usuario", usuarioBD);

        // ---------------------------------------------------------------
        // Paso 4: Redirigir según el rol del usuario
        // ---------------------------------------------------------------
        // Si el rol es "ADMIN"  ->  va al panel de administración
        //                           (listado de productos).
        // Si el rol es "CLIENTE" ->  va a la vista de tienda.
        if ("ADMIN".equals(usuarioBD.getRol())) {
            return "redirect:/productos";
        } else {
            return "redirect:/tienda";
        }
    }

    /*
     * Muestra la vista de tienda para usuarios con rol CLIENTE.
     *
     * Después del login exitoso, el controlador redirige a /tienda
     * y Spring busca este método para renderizar tienda.html.
     *
     * @GetMapping("/tienda")  ->  cuando el usuario visita /tienda
     *                              se ejecuta este método y retorna
     *                              el nombre de la vista (tienda.html).
     */
    @GetMapping("/tienda")
    public String mostrarTienda(HttpSession session, Model model) {

        // Obtener usuario de sesión
        Usuario usuario = (Usuario) session.getAttribute("usuario");

        // Si el usuario no ha iniciado sesión, lo enviamos al login
        if (usuario == null) {
            return "redirect:/login";
        }

        /*
         * Obtenemos todos los productos registrados en la base de datos
         * usando el método listarProductos() del ProductoService.
         * Luego los agregamos al modelo con el nombre "productos"
         * para que la vista tienda.html pueda recorrerlos.
         */
        model.addAttribute("productos", productoService.listarProductos());
        return "tienda";
    }

    /*
     * Agrega un producto al carrito del cliente.
     *
     * @PathVariable productoId  ->  el id del producto que viene en la URL.
     *                                Ejemplo: /carrito/agregar/3  ->  productoId = 3
     *
     * HttpSession session       ->  obtenemos la sesión actual del navegador.
     *
     * Pasos:
     *   1. Obtener el usuario guardado en la sesión.
     *   2. Buscar el producto por su id en la base de datos.
     *   3. Llamar al servicio para agregar el producto al carrito.
     *   4. Redirigir de vuelta a la tienda.
     */
    @PostMapping("/carrito/agregar/{productoId}")
    public String agregarAlCarrito(
            @PathVariable Long productoId,
            HttpSession session) {

        // Obtener usuario de sesión
        Usuario usuario = (Usuario) session.getAttribute("usuario");

        // Si el usuario no ha iniciado sesión, lo enviamos al login
        if (usuario == null) {
            return "redirect:/login";
        }

        // Buscar el producto por su id en la base de datos
        Producto producto = productoService.obtenerProductoPorId(productoId);

        // Si el producto no existe (id inválido), redirigimos a la tienda
        if (producto == null) {
            return "redirect:/tienda";
        }

        // Agregar producto al carrito (crea o reusa el carrito del usuario)
        carritoService.agregarProductoAlCarrito(usuario, producto);

        // Redirigir a la tienda para que el cliente siga viendo productos
        return "redirect:/tienda";
    }

    /*
     * Muestra el carrito de compras del usuario que está en sesión.
     *
     * Pasos:
     *   1. Obtener el usuario desde la sesión HTTP.
     *   2. Buscar el carrito asociado a ese usuario.
     *   3. Obtener todos los productos (detalles) del carrito.
     *   4. Calcular el total general del carrito.
     *   5. Enviar los detalles y el total a la vista carrito.html.
     */
    @GetMapping("/carrito")
    public String mostrarCarrito(HttpSession session, Model model) {

        // Obtener usuario de sesión
        Usuario usuario = (Usuario) session.getAttribute("usuario");

        // Si el usuario no ha iniciado sesión, lo enviamos al login
        if (usuario == null) {
            return "redirect:/login";
        }

        // Buscar carrito del usuario (sin crear uno nuevo)
        Carrito carrito = carritoService.obtenerOCrearCarrito(usuario);

        // Obtener todos los productos agregados al carrito
        java.util.List<DetalleCarrito> detalles = carritoService.obtenerDetallesDelCarrito(carrito);

        // Calcular total del carrito (precio * cantidad de cada producto)
        double total = carritoService.calcularTotal(detalles);

        // Leer y eliminar el mensaje de éxito de la sesión
        // para que solo se muestre una vez después de finalizar compra
        String mensajeExito = (String) session.getAttribute("mensajeExito");
        if (mensajeExito != null) {
            model.addAttribute("mensajeExito", mensajeExito);
            session.removeAttribute("mensajeExito");
        }

        // Enviar datos a la vista
        model.addAttribute("carrito", carrito);
        model.addAttribute("detalles", detalles);
        model.addAttribute("total", total);

        return "carrito";
    }

    /*
     * Elimina un producto del carrito del usuario.
     *
     * @PathVariable detalleId  ->  el id del DetalleCarrito a eliminar.
     *                              Ejemplo: /carrito/eliminar/5  ->  elimina el detalle 5.
     *
     * HttpSession session       ->  obtenemos la sesión para verificar
     *                                que el usuario esté logueado.
     */
    @PostMapping("/carrito/eliminar/{detalleId}")
    public String eliminarDelCarrito(
            @PathVariable Long detalleId,
            HttpSession session) {

        // Verificar que el usuario haya iniciado sesión
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null) {
            return "redirect:/login";
        }

        // Eliminar el producto del carrito por su id de detalle
        carritoService.eliminarDetalleDelCarrito(detalleId);

        // Redirigir de vuelta al carrito para ver los cambios
        return "redirect:/carrito";
    }

    // ------------------------------------------------------------------
    // Finalizar compra: convierte el carrito en un pedido
    // ------------------------------------------------------------------
    /*
     * Procesa la finalización de la compra.
     *
     * Pasos:
     *   1. Obtener el usuario de la sesión.
     *   2. Obtener el carrito del usuario.
     *   3. Obtener todos los productos del carrito.
     *   4. Crear un nuevo Pedido con la fecha actual y el total.
     *   5. Recorrer cada DetalleCarrito y crear un DetallePedido.
     *   6. Guardar el Pedido y los DetallePedido en la base de datos.
     *   7. Vaciar el carrito (eliminar todos los DetalleCarrito).
     *   8. Mostrar mensaje de compra exitosa.
     *
     * @return redirección al carrito con mensaje de éxito.
     */
    @PostMapping("/carrito/finalizar")
    public String finalizarCompra(HttpSession session) {

        // ---------------------------------------------------------------
        // Paso 1: Obtener usuario de sesión
        // ---------------------------------------------------------------
        Usuario usuario = (Usuario) session.getAttribute("usuario");

        // Si el usuario no ha iniciado sesión, lo enviamos al login
        if (usuario == null) {
            return "redirect:/login";
        }

        // ---------------------------------------------------------------
        // Paso 2: Obtener el carrito del usuario
        // ---------------------------------------------------------------
        Carrito carrito = carritoService.obtenerOCrearCarrito(usuario);

        // ---------------------------------------------------------------
        // Paso 3: Obtener todos los productos del carrito
        // ---------------------------------------------------------------
        java.util.List<DetalleCarrito> detallesCarrito = carritoService.obtenerDetallesDelCarrito(carrito);

        // Si el carrito está vacío, redirigimos sin hacer nada
        if (detallesCarrito.isEmpty()) {
            return "redirect:/carrito";
        }

        // ---------------------------------------------------------------
        // Paso 4: Calcular el total de la compra
        // ---------------------------------------------------------------
        double total = carritoService.calcularTotal(detallesCarrito);

        // ---------------------------------------------------------------
        // Paso 5: Crear pedido
        // ---------------------------------------------------------------
        Pedido pedido = new Pedido();
        pedido.setUsuario(usuario);
        pedido.setFechaPedido(LocalDateTime.now());
        pedido.setTotal(total);

        // Guardar el pedido primero para que tenga un ID generado
        pedidoService.guardarPedido(pedido);

        // ---------------------------------------------------------------
        // Paso 6: Recorrer el carrito y crear cada detalle del pedido
        // ---------------------------------------------------------------
        for (DetalleCarrito detalleCarrito : detallesCarrito) {

            // Crear detalle del pedido
            DetallePedido detallePedido = new DetallePedido();
            detallePedido.setPedido(pedido);
            detallePedido.setProducto(detalleCarrito.getProducto());
            detallePedido.setCantidad(detalleCarrito.getCantidad());

            // Calcular subtotal = precio del producto * cantidad
            double subtotal = detalleCarrito.getProducto().getPrecio() * detalleCarrito.getCantidad();
            detallePedido.setSubtotal(subtotal);

            // Guardar cada detalle del pedido
            detallePedidoService.guardarDetalle(detallePedido);
        }

        // ---------------------------------------------------------------
        // Paso 7: Vaciar carrito
        // ---------------------------------------------------------------
        carritoService.vaciarCarrito(carrito);

        // ---------------------------------------------------------------
        // Paso 8: Mostrar mensaje de compra exitosa
        // ---------------------------------------------------------------
        // Guardamos un mensaje en la sesión para mostrarlo en la vista
        session.setAttribute("mensajeExito", "¡Compra realizada con éxito! Gracias por tu compra.");

        return "redirect:/carrito";
    }

    // ------------------------------------------------------------------
    // Mis Compras: muestra el historial de pedidos del usuario
    // ------------------------------------------------------------------
    /*
     * Muestra la lista de pedidos realizados por el usuario.
     *
     * Pasos:
     *   1. Obtener el usuario de la sesión.
     *   2. Buscar todos los pedidos asociados a ese usuario.
     *   3. Enviar la lista de pedidos a la vista mis-compras.html.
     *
     * @return la vista mis-compras.html con los pedidos del usuario.
     */
    @GetMapping("/mis-compras")
    public String mostrarMisCompras(HttpSession session, Model model) {

        // Obtener usuario de sesión
        Usuario usuario = (Usuario) session.getAttribute("usuario");

        // Si el usuario no ha iniciado sesión, lo enviamos al login
        if (usuario == null) {
            return "redirect:/login";
        }

        // Buscar pedidos del usuario
        java.util.List<Pedido> pedidos = pedidoService.buscarPedidosPorUsuario(usuario);

        // Mostrar historial de compras
        model.addAttribute("pedidos", pedidos);

        return "mis-compras";
    }

    // ------------------------------------------------------------------
    // Favoritos: agregar, listar y eliminar productos favoritos
    // ------------------------------------------------------------------

    /*
     * Agrega un producto a favoritos del usuario.
     *
     * Pasos:
     *   1. Obtener el usuario de la sesión.
     *   2. Buscar el producto por su id.
     *   3. Verificar si ya existe el favorito (evitar duplicados).
     *   4. Si no existe, crear un nuevo Favorito y guardarlo.
     *   5. Redirigir a la tienda.
     */
    @PostMapping("/favoritos/agregar/{productoId}")
    public String agregarFavorito(
            @PathVariable Long productoId,
            HttpSession session) {

        // Obtener usuario de sesión
        Usuario usuario = (Usuario) session.getAttribute("usuario");

        // Si el usuario no ha iniciado sesión, lo enviamos al login
        if (usuario == null) {
            return "redirect:/login";
        }

        // Buscar el producto por su id
        Producto producto = productoService.obtenerProductoPorId(productoId);

        // Si el producto no existe, redirigimos a la tienda
        if (producto == null) {
            return "redirect:/tienda";
        }

        // Verificar si el usuario ya tiene este producto como favorito
        // (evitar duplicados en la tabla favorito)
        if (!favoritoService.existeFavorito(usuario, producto)) {

            // Crear objeto Favorito
            Favorito favorito = new Favorito();

            // Asociar usuario y producto
            favorito.setUsuario(usuario);
            favorito.setProducto(producto);

            // Guardar usando FavoritoService
            favoritoService.guardarFavorito(favorito);
        }

        return "redirect:/tienda";
    }

    /*
     * Muestra la lista de productos favoritos del usuario.
     *
     * Pasos:
     *   1. Obtener el usuario de la sesión.
     *   2. Buscar favoritos del usuario usando FavoritoService.
     *   3. Enviar la lista a la vista mis-favoritos.html.
     */
    @GetMapping("/mis-favoritos")
    public String mostrarMisFavoritos(HttpSession session, Model model) {

        // Obtener usuario de sesión
        Usuario usuario = (Usuario) session.getAttribute("usuario");

        // Si el usuario no ha iniciado sesión, lo enviamos al login
        if (usuario == null) {
            return "redirect:/login";
        }

        // Buscar favoritos del usuario
        java.util.List<Favorito> favoritos = favoritoService.listarFavoritosPorUsuario(usuario);

        // Mostrar lista de favoritos
        model.addAttribute("favoritos", favoritos);

        return "mis-favoritos";
    }

    /*
     * Elimina un producto de favoritos.
     *
     * @PathVariable favoritoId  ->  el id del Favorito a eliminar.
     *
     * Pasos:
     *   1. Buscar favorito por id.
     *   2. Eliminar usando FavoritoService.
     *   3. Redirigir a /mis-favoritos.
     */
    @PostMapping("/favoritos/eliminar/{favoritoId}")
    public String eliminarFavorito(
            @PathVariable Long favoritoId,
            HttpSession session) {

        // Verificar que el usuario haya iniciado sesión
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null) {
            return "redirect:/login";
        }

        // Eliminar favorito por su id
        favoritoService.eliminarFavorito(favoritoId);

        // Redirigir a la lista de favoritos
        return "redirect:/mis-favoritos";
    }

    /*
     * Muestra el formulario de registro.
     *
     * Enviamos un objeto Usuario vacío al formulario para que
     * Thymeleaf pueda enlazar los campos con th:field.
     */
    @GetMapping("/registro")
    public String mostrarFormularioRegistro(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "registro";
    }

    /*
     * Procesa el registro enviado desde el formulario.
     *
     * @Valid        ->  activa las validaciones definidas en Usuario
     *                   (@NotBlank, @Email, @Size, etc.).
     * BindingResult ->  captura los errores de validación para
     *                   mostrarlos en el formulario.
     *
     * Si hay errores de validación -> vuelve al formulario.
     * Si el correo ya existe        -> agrega un error y vuelve.
     * Si todo está bien             -> guarda y redirige al login.
     */
    @PostMapping("/registro")
    public String procesarRegistro(
            @Valid @ModelAttribute Usuario usuario,
            BindingResult result,
            Model model) {

        // Si las validaciones (@NotBlank, @Email, etc.) fallan,
        // devolvemos el formulario para que muestre los errores.
        if (result.hasErrors()) {
            return "registro";
        }

        // Verificar si el correo ya está registrado
        Usuario existente = usuarioService.buscarPorCorreo(usuario.getCorreo());
        if (existente != null) {
            // Agregamos un error manual al campo "correo"
            result.rejectValue("correo", "error.usuario",
                    "Este correo ya está registrado");
            return "registro";
        }

        // ---------------------------------------------------------------
        // Asignar rol CLIENTE automáticamente
        // ---------------------------------------------------------------
        // El formulario de registro NO tiene un campo para el rol.
        // Aquí lo asignamos manualmente para que todo usuario nuevo
        // se registre con el rol "CLIENTE" por defecto.
        usuario.setRol("CLIENTE");

        // Guardar el usuario en la base de datos
        usuarioService.guardarUsuario(usuario);

        // Redirigir al login (aunque aún no exista esa página)
        return "redirect:/login";
    }

    /*
     * Cierra la sesión del usuario.
     *
     * HttpSession session  ->  obtenemos la sesión actual.
     * session.invalidate() ->  elimina todos los datos guardados
     *                          en la sesión (incluyendo al usuario).
     *
     * Después redirigimos al login para que el usuario
     * pueda iniciar sesión nuevamente si lo desea.
     */
    @GetMapping("/logout")
    public String cerrarSesion(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}
