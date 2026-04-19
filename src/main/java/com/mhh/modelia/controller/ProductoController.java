package com.mhh.modelia.controller;

import com.mhh.modelia.dto.ProductoResponse;
import com.mhh.modelia.service.ProductoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/productos")
@Tag(name = "Productos", description = "Catalogo publico de productos")
public class ProductoController {

    private final ProductoService productoService;

    public ProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }

    @GetMapping
    @Operation(summary = "Listar productos, filtrar por categoriaId o nombre")
    public ResponseEntity<List<ProductoResponse>> getAll(
            @RequestParam(required = false) Long categoriaId,
            @RequestParam(required = false) String nombre) {
        return ResponseEntity.ok(
                productoService.findAll(categoriaId, nombre)
                        .stream()
                        .map(ProductoResponse::from)
                        .toList()
        );
    }

    @GetMapping("/{id}")
    @Operation(summary = "Detalle de producto por ID")
    public ResponseEntity<ProductoResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ProductoResponse.from(productoService.findById(id)));
    }
    
    @GetMapping("/destacados")
    public ResponseEntity<List<ProductoResponse>> getDestacados() {
        return ResponseEntity.ok(
            productoService.findDestacados().stream()
                .map(ProductoResponse::from)
                .toList()
        );
    }
}