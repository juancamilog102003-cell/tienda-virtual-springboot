package com.tienda.tienda.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.tienda.tienda.model.Producto;
import com.tienda.tienda.repository.ProductoRepository;

@Service
public class ProductoService {

    @Autowired
    private ProductoRepository repository;

    public List<Producto> listarProductos() {
        return repository.findAll();
    }

    public void guardarProducto(@NonNull Producto producto) {
        repository.save(producto);
    }

    public Producto obtenerProductoPorId(@NonNull Long id) {
        return repository.findById(id).orElse(null);
    } 

    public void eliminar(@NonNull Long id) {
        repository.deleteById(id);
    }

    public String guardarImagen(@NonNull MultipartFile file) throws IOException {
        String extension = "";
        String originalFilename = file.getOriginalFilename();
        if (originalFilename != null && originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }
        String filename = UUID.randomUUID().toString() + extension;

        Path uploadPath = Paths.get("uploads/images/");
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        Path filePath = uploadPath.resolve(filename);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        return "/uploads/images/" + filename;
    }
}
