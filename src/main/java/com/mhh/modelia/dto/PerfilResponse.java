package com.mhh.modelia.dto;

import com.mhh.modelia.entity.Usuario;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class PerfilResponse {

    private Long id;
    private String nombre;
    private String email;
    private String rol;
    private String direccion;
    private LocalDateTime createdAt;
    private boolean activo;

    public static PerfilResponse from(Usuario u) {
        PerfilResponse dto = new PerfilResponse();
        dto.setId(u.getId());
        dto.setNombre(u.getNombre());
        dto.setEmail(u.getEmail());
        dto.setRol(u.getRol().name());
        dto.setDireccion(u.getDireccion());
        dto.setCreatedAt(u.getCreatedAt());
        dto.setActivo(u.isActivo());
        return dto;
    }
}