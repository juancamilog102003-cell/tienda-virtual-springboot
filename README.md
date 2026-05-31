Tienda Virtual - Proyecto Académico Spring Boot

Aplicación web desarrollada con Spring Boot, Thymeleaf y MySQL que simula el funcionamiento de una tienda virtual. El sistema permite a los usuarios registrarse, iniciar sesión, gestionar productos, agregar artículos al carrito, realizar compras, consultar su historial de pedidos y administrar una lista de productos favoritos.

Funcionalidades

Gestión de Usuarios

* Registro de usuarios
* Inicio de sesión
* Gestión de sesiones
* Roles de usuario

Gestión de Productos

* Registrar productos
* Listar productos
* Editar productos
* Eliminar productos
* Visualizar información detallada de productos

Gestión de Tipos de Producto

* Clasificación de productos por categorías
* Relación entre productos y tipos de producto

Carrito de Compras

* Agregar productos al carrito
* Visualizar carrito
* Eliminar productos del carrito
* Calcular total de la compra
* Vaciar carrito al finalizar compra

Gestión de Pedidos

* Finalizar compra
* Generar pedidos automáticamente
* Registrar detalles de cada pedido
* Consultar historial de compras

Gestión de Favoritos

* Agregar productos a favoritos
* Consultar lista de favoritos
* Eliminar productos favoritos
* Evitar favoritos duplicados

Modelo de Base de Datos

La aplicación utiliza MySQL y Spring Data JPA para la persistencia de datos.

Entidades principales

* Usuario
* Producto
* TipoProducto
* Carrito
* DetalleCarrito
* Pedido
* DetallePedido
* Favorito

Relaciones implementadas

* Un TipoProducto tiene muchos Productos.
* Un Usuario puede tener un Carrito.
* Un Carrito contiene múltiples DetalleCarrito.
* Un Usuario puede realizar múltiples Pedidos.
* Un Pedido contiene múltiples DetallePedido.
* Un Usuario puede tener múltiples Favoritos.
* Un Producto puede pertenecer a múltiples Favoritos.

Tecnologías Utilizadas

* Java 25
* Spring Boot
* Spring Data JPA
* Hibernate
* Thymeleaf
* MySQL
* HTML5
* CSS3
* Bootstrap 5
* Maven

Arquitectura del Proyecto

El proyecto sigue el patrón MVC (Model - View - Controller):

* Model: Entidades JPA
* Repository: Acceso a datos
* Service: Lógica de negocio
* Controller: Gestión de solicitudes HTTP
* View: Plantillas Thymeleaf

Cómo Ejecutar el Proyecto

1. Clonar el repositorio:

git clone https://github.com/juancamilog102003-cell/tienda-virtual-springboot.git

2. Crear la base de datos:

CREATE DATABASE tiendavirtual;

3. Configurar las credenciales de MySQL en:

application.properties

4. Ejecutar la aplicación:

mvn spring-boot:run

5. Abrir en el navegador:

http://localhost:8080

Funcionalidades Implementadas para el Proyecto Académico

✓ CRUD de Productos

✓ Gestión de Categorías

✓ Relaciones JPA (OneToMany y ManyToOne)

✓ Persistencia en MySQL

✓ Registro e Inicio de Sesión

✓ Carrito de Compras

✓ Finalización de Compras

✓ Historial de Pedidos

✓ Gestión de Favoritos

✓ Arquitectura MVC

Autores

Juan Camilo Gallego Barbosa

Oscar Díaz

Proyecto Académico - Ingeniería de Sistemas

Spring Boot + MySQL + Thymeleaf