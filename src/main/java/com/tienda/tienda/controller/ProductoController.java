package com.tienda.tienda.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import com.tienda.tienda.model.Producto;
import com.tienda.tienda.repository.ProductoRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import com.tienda.tienda.service.ProductoService;

import com.tienda.tienda.repository.TipoProductoRepository;


@Controller
public class ProductoController {

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private TipoProductoRepository tipoProductoRepository;

    @Autowired
    private ProductoService service;

    @GetMapping({"/", "/productos"})
    public String productos(Model model) {
        model.addAttribute("productos", productoRepository.findAll());
        return "index";
    }

    @GetMapping("/productos/nuevo")
    public String formularioProducto(Model model) {
        model.addAttribute("producto", new Producto());
        model.addAttribute("tipos", tipoProductoRepository.findAll());
        return "formulario";
    }

    @PostMapping("/productos/guardar")
    public String guardar(@ModelAttribute Producto producto, @RequestParam("imagenFile") MultipartFile imagenFile) throws IOException {
        if (!imagenFile.isEmpty()) {
            String imagenUrl = service.guardarImagen(imagenFile);
            producto.setImagenUrl(imagenUrl);
        } else if (producto.getId() != null) {
            Producto existing = service.obtenerProductoPorId(producto.getId());
            if (existing != null) {
                producto.setImagenUrl(existing.getImagenUrl());
            }
        }
        service.guardarProducto(producto);
        return "redirect:/productos";
    }

    @GetMapping("/productos/editar/{id}")
    public String editar(@PathVariable @NonNull Long id, Model model) {
        model.addAttribute("producto", service.obtenerProductoPorId(id));
        model.addAttribute("tipos", tipoProductoRepository.findAll());
        return "formulario";
    }

    @GetMapping("/productos/eliminar/{id}")
    public String eliminar(@PathVariable @NonNull Long id) {
        service.eliminar(id);
        return "redirect:/productos";
    }
}
