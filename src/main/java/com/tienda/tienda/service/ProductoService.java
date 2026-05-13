package com.tienda.tienda.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tienda.tienda.model.Producto;
import com.tienda.tienda.repository.ProductoRepository;

@Service
public class ProductoService {

    @Autowired
    private ProductoRepository repository;

    public List<Producto> listarProductos() {
        return repository.findAll();
    }

    public void guardarProducto(Producto producto) {
        repository.save(producto);
    }

    public Producto obtenerProductoPorId(Long id) {
        return repository.findById(id).orElse(null);
    } 

    public void eliminar(Long id) {
        repository.deleteById(id);
    }
}
