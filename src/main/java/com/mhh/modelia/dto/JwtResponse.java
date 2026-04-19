package com.mhh.modelia.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class JwtResponse {

    private String accessToken;
    private String refreshToken;
    private String tipo = "Bearer";
    private Long id;
    private String nombre;
    private String email;
    private String rol;

    public JwtResponse(String accessToken, String refreshToken,
                       Long id, String nombre, String email, String rol) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.tipo = "Bearer";
        this.id = id;
        this.nombre = nombre;
        this.email = email;
        this.rol = rol;
    }
}