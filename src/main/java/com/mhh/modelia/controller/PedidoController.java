package com.mhh.modelia.controller;

import com.mhh.modelia.dto.PedidoRequest;
import com.mhh.modelia.dto.PedidoResponse;
import com.mhh.modelia.entity.Pedido;
import com.mhh.modelia.service.PedidoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pedidos")
@Tag(name = "Pedidos", description = "Gestion de pedidos del cliente")
@SecurityRequirement(name = "bearerAuth")
public class PedidoController {

    private final PedidoService pedidoService;

    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @GetMapping("/mis-pedidos")
    @Operation(summary = "Historial de pedidos del usuario autenticado")
    public ResponseEntity<List<PedidoResponse>> misPedidos(
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(
                pedidoService.findByUsuario(
                        pedidoService.getUsuarioIdByEmail(userDetails.getUsername())
                ).stream()
                 .map(PedidoResponse::from)
                 .toList()
        );
    }

    @PostMapping
    public ResponseEntity<PedidoResponse> create(
            @Valid @RequestBody PedidoRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        try {
            Pedido pedido = pedidoService.create(request, userDetails.getUsername());
            PedidoResponse response = PedidoResponse.from(pedido);
            return ResponseEntity.status(201).body(response);
        } catch (Exception e) {
            // Log temporal para ver el error real
            e.printStackTrace();
            throw e;
        }
    }
}