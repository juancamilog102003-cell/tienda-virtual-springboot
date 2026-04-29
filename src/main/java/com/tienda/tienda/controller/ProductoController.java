package com.tienda.tienda.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import com.tienda.tienda.repository.ProductoRepository;
import org.springframework.web.bind.annotation.GetMapping;



@Controller
public class ProductoController {

    private final ProductoRepository productoRepository;
    public ProductoController(ProductoRepository productoRepository){

        this.productoRepository=productoRepository;
    }

    @GetMapping("/productos")
    public String productos (Model model) {
        model.addAttribute("productos", productoRepository.findAll());
        return "index";
    }
    
}
