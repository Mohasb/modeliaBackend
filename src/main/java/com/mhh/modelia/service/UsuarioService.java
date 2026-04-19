package com.mhh.modelia.service;

import com.mhh.modelia.dto.PerfilRequest;
import com.mhh.modelia.dto.PerfilResponse;
import com.mhh.modelia.entity.Usuario;
import com.mhh.modelia.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }
    @Transactional(readOnly = true)
    public PerfilResponse getPerfil(String email) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        return PerfilResponse.from(usuario);
    }

    @Transactional
    public PerfilResponse updatePerfil(String email, PerfilRequest request) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        usuario.setNombre(request.getNombre());
        usuario.setDireccion(request.getDireccion());
        return PerfilResponse.from(usuarioRepository.save(usuario));
    }

    public List<Usuario> findAll() {
        return usuarioRepository.findAll();
    }

    @Transactional
    public PerfilResponse toggleActivo(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        usuario.setActivo(!usuario.isActivo());
        return PerfilResponse.from(usuarioRepository.save(usuario));
    }

    @Transactional
    public PerfilResponse cambiarRol(Long id, String rol) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        usuario.setRol(Usuario.Rol.valueOf(rol));
        return PerfilResponse.from(usuarioRepository.save(usuario));
    }
}