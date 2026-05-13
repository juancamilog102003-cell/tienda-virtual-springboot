# Tienda Virtual - Proyecto Académico Spring Boot 

Aplicación web desarrollada para la gestión de productos en una tienda virtual.  
Permite registrar, listar, editar y eliminar productos, además de asociarlos a una categoría o tipo de producto mediante una relación en base de datos.

## Funcionalidades

- Registrar nuevos productos
- Listar productos disponibles
- Editar productos existentes
- Eliminar productos
- Asociar cada producto a un tipo de producto
- Gestión de base de datos relacional con MySQL

## Módulos del sistema

### Gestión de Productos
Permite administrar productos con información como:

- Nombre
- Precio
- Stock
- Descripción
- Imagen
- Tipo de producto

### Gestión de Tipos de Producto
Se implementó una entidad independiente para clasificar productos como:

- Consola
- PC
- Juego
- Accesorio

Cada producto se relaciona con un tipo de producto mediante clave foránea.

## Modelo de Base de Datos

Se implementó una base de datos relacional en MySQL con las siguientes tablas:

### Tabla producto
Contiene la información principal de cada producto.

### Tabla tipo_producto
Contiene las categorías de productos.

Relación:

- Un tipo de producto puede tener muchos productos
- Un producto pertenece a un solo tipo de producto

## Tecnologías utilizadas

- Java 17
- Spring Boot
- Spring Data JPA
- Hibernate
- Thymeleaf
- MySQL
- HTML5
- CSS3
- Bootstrap
- Maven

## Estructura del proyecto

src/main/java/com/tienda/tienda

- controller  
  - ProductoController.java

- model  
  - Producto.java  
  - TipoProducto.java  

- repository  
  - ProductoRepository.java  
  - TipoProductoRepository.java  

- service  
  - ProductoService.java  

src/main/resources/templates

- index.html  
- formulario.html  

## Cómo ejecutar el proyecto

1. Clonar repositorio

```bash
git clone https://github.com/juancamilog102003-cell/tienda-virtual-springboot.git
```

2. Abrir proyecto en Visual Studio Code o IntelliJ IDEA

3. Configurar conexión MySQL en:

```properties
application.properties
```

4. Ejecutar aplicación:

```bash
mvn spring-boot:run
```

5. Abrir navegador:

```bash
http://localhost:8080
```

## Autor

Juan Camilo Gallego Barbosa 
Oscar Diaz 
Proyecto académico - Spring Boot  + MySQL