package com.tienda.tienda.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import com.tienda.tienda.model.Usuario;
import com.tienda.tienda.repository.UsuarioRepository;

/*
 * Servicio que contiene la lógica para manejar usuarios.
 *
 * @Service  ->  marca esta clase como un componente de servicio
 *               para que Spring la administre automáticamente.
 */
@Service
public class UsuarioService {

    /*
     * Inyección de dependencias: Spring crea una instancia de
     * UsuarioRepository y la asigna aquí automáticamente.
     */
    @Autowired
    private UsuarioRepository usuarioRepository;

    /*
     * Guarda un usuario en la base de datos.
     *
     * Si el usuario ya tiene un id (existente), lo actualiza.
     * Si no tiene id (nuevo), lo inserta.
     */
    public void guardarUsuario(@NonNull Usuario usuario) {
        usuarioRepository.save(usuario);
    }

    /*
     * Busca un usuario por su correo electrónico.
     *
     * Retorna el usuario si lo encuentra, o null si no existe.
     * Usa orElse(null) para evitar un Optional vacío.
     */
    public Usuario buscarPorCorreo(String correo) {
        return usuarioRepository.findByCorreo(correo).orElse(null);
    }
}
