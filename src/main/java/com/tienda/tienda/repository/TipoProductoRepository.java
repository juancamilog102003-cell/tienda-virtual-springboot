package com.tienda.tienda.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.tienda.tienda.model.TipoProducto;

public interface TipoProductoRepository extends JpaRepository<TipoProducto, Long> {}
