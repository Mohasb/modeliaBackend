package com.mhh.modelia.controller;

import com.mhh.modelia.dto.PerfilRequest;
import com.mhh.modelia.dto.PerfilResponse;
import com.mhh.modelia.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/usuario")
@Tag(name = "Usuario", description = "Perfil del cliente autenticado")
@SecurityRequirement(name = "bearerAuth")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping("/perfil")
    @Operation(summary = "Obtener perfil del usuario autenticado")
    ResponseEntity<PerfilResponse> getPerfil(
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(
                usuarioService.getPerfil(userDetails.getUsername()));
    }

    @PutMapping("/perfil")
    @Operation(summary = "Actualizar nombre y direccion del usuario")
    ResponseEntity<PerfilResponse> updatePerfil(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody PerfilRequest request) {
        return ResponseEntity.ok(
                usuarioService.updatePerfil(userDetails.getUsername(), request));
    }
}