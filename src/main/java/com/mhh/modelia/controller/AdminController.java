package com.mhh.modelia.controller;

import com.mhh.modelia.dto.EstadoRequest;
import com.mhh.modelia.dto.PedidoResponse;
import com.mhh.modelia.dto.PerfilResponse;
import com.mhh.modelia.dto.ProductoRequest;
import com.mhh.modelia.dto.ProductoResponse;
import com.mhh.modelia.entity.Categoria;
import com.mhh.modelia.entity.Pedido;
import com.mhh.modelia.entity.Producto;
import com.mhh.modelia.repository.CategoriaRepository;
import com.mhh.modelia.service.CategoriaService;
import com.mhh.modelia.service.PedidoService;
import com.mhh.modelia.service.ProductoService;
import com.mhh.modelia.service.UsuarioService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@Tag(name = "Admin", description = "Panel de administracion, requiere rol ADMIN")
@SecurityRequirement(name = "bearerAuth")
public class AdminController {

    private final ProductoService productoService;
    private final CategoriaService categoriaService;
    private final PedidoService pedidoService;
    private final UsuarioService usuarioService;

    public AdminController(ProductoService productoService,
                           CategoriaService categoriaService,
                           PedidoService pedidoService,
                           CategoriaRepository categoriaRepository, UsuarioService usuarioService) {
        this.productoService = productoService;
        this.categoriaService = categoriaService;
        this.pedidoService = pedidoService;
        this.usuarioService = usuarioService;
    }

    // ── Productos ──────────────────────────────────────────────

    @PostMapping("/productos")
    @Operation(summary = "Crear nuevo producto")
    public ResponseEntity<ProductoResponse> createProducto(
            @Valid @RequestBody ProductoRequest request) {
        Categoria categoria = categoriaService.findById(request.getCategoriaId());

        Producto producto = new Producto();
        producto.setNombre(request.getNombre());
        producto.setDescripcion(request.getDescripcion());
        producto.setPrecio(request.getPrecio());
        producto.setStock(request.getStock());
        producto.setStockMinimo(request.getStockMinimo());
        producto.setImagenUrl(request.getImagenUrl());
        producto.setModeloGlbUrl(request.getModeloGlbUrl());
        producto.setCategoria(categoria);

        Producto guardado = productoService.save(producto);
        Producto conCategoria = productoService.findById(guardado.getId());
        return ResponseEntity.status(201).body(ProductoResponse.from(conCategoria));
    }

    @PutMapping("/productos/{id}")
    @Operation(summary = "Editar producto existente")
    public ResponseEntity<ProductoResponse> updateProducto(
            @PathVariable Long id,
            @Valid @RequestBody ProductoRequest request) {
        Producto producto = productoService.findById(id);
        Categoria categoria = categoriaService.findById(request.getCategoriaId());

        producto.setNombre(request.getNombre());
        producto.setDescripcion(request.getDescripcion());
        producto.setPrecio(request.getPrecio());
        producto.setStock(request.getStock());
        producto.setStockMinimo(request.getStockMinimo());
        producto.setImagenUrl(request.getImagenUrl());
        producto.setModeloGlbUrl(request.getModeloGlbUrl());
        producto.setCategoria(categoria);

        Producto guardado = productoService.save(producto);
        Producto conCategoria = productoService.findById(guardado.getId());
        return ResponseEntity.ok(ProductoResponse.from(conCategoria));
    }

    @DeleteMapping("/productos/{id}")
    @Operation(summary = "Borrado logico de producto")
    public ResponseEntity<Void> deleteProducto(@PathVariable Long id) {
        productoService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // ── Categorias ─────────────────────────────────────────────

    @PostMapping("/categorias")
    @Operation(summary = "Crear nueva categoria")
    public ResponseEntity<Categoria> createCategoria(
            @RequestBody Categoria categoria) {
        return ResponseEntity.status(201)
                .body(categoriaService.save(categoria));
    }

    @DeleteMapping("/categorias/{id}")
    @Operation(summary = "Borrado logico de categoria")
    public ResponseEntity<Void> deleteCategoria(@PathVariable Long id) {
        categoriaService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // ── Pedidos ────────────────────────────────────────────────

    @GetMapping("/pedidos")
    public ResponseEntity<List<PedidoResponse>> getAllPedidos() {
        return ResponseEntity.ok(
                pedidoService.findAll().stream()
                    .map(PedidoResponse::from)
                    .toList()
        );
    }

    @PutMapping("/pedidos/{id}/estado")
    @Operation(summary = "Cambiar estado de un pedido")
    public ResponseEntity<Pedido> cambiarEstado(
            @PathVariable Long id,
            @RequestBody EstadoRequest request) {
        Pedido.Estado nuevoEstado = Pedido.Estado.valueOf(request.getEstado());
        return ResponseEntity.ok(pedidoService.cambiarEstado(id, nuevoEstado));
    }
    
 // ── Usuarios ───────────────────────────────────────────────

    @GetMapping("/usuarios")
    @Operation(summary = "Listar todos los usuarios")
    ResponseEntity<List<PerfilResponse>> getAllUsuarios() {
        return ResponseEntity.ok(
                usuarioService.findAll()
                        .stream()
                        .map(PerfilResponse::from)
                        .toList()
        );
    }

    @PutMapping("/usuarios/{id}/toggle-activo")
    @Operation(summary = "Activar o desactivar un usuario")
    ResponseEntity<PerfilResponse> toggleActivo(@PathVariable Long id) {
        return ResponseEntity.ok(usuarioService.toggleActivo(id));
    }

    @PutMapping("/usuarios/{id}/rol")
    @Operation(summary = "Cambiar rol de un usuario")
    ResponseEntity<PerfilResponse> cambiarRol(
            @PathVariable Long id,
            @RequestBody EstadoRequest request) {
        return ResponseEntity.ok(usuarioService.cambiarRol(id, request.getEstado()));
    }
    
    @PutMapping("/productos/{id}/destacado")
    public ResponseEntity<ProductoResponse> toggleDestacado(@PathVariable Long id) {
        return ResponseEntity.ok(
            ProductoResponse.from(productoService.toggleDestacado(id))
        );
    }
}