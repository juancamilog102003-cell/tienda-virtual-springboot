package com.tienda.tienda.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tienda.tienda.model.Producto;

public interface ProductoRepository extends JpaRepository<Producto, Long> {

}
