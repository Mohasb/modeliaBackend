package com.mhh.modelia.service;

import com.mhh.modelia.dto.PedidoRequest;
import com.mhh.modelia.entity.*;
import com.mhh.modelia.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final ProductoRepository productoRepository;
    private final UsuarioRepository usuarioRepository;

    public PedidoService(PedidoRepository pedidoRepository,
                         ProductoRepository productoRepository,
                         UsuarioRepository usuarioRepository) {
        this.pedidoRepository = pedidoRepository;
        this.productoRepository = productoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public List<Pedido> findByUsuario(Long usuarioId) {
        return pedidoRepository.findByUsuarioIdOrderByCreatedAtDesc(usuarioId);
    }

    public List<Pedido> findAll() {
        return pedidoRepository.findAll();
    }
    public Long getUsuarioIdByEmail(String email) {
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"))
                .getId();
    }

    @Transactional
    public Pedido create(PedidoRequest request, String email) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        List<PedidoItem> items = new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;

        for (PedidoRequest.ItemRequest itemReq : request.getItems()) {
            Producto producto = productoRepository.findById(itemReq.getProductoId())
                    .orElseThrow(() -> new RuntimeException(
                            "Producto no encontrado: " + itemReq.getProductoId()));

            if (producto.getStock() < itemReq.getCantidad()) {
                throw new RuntimeException(
                        "Stock insuficiente para: " + producto.getNombre());
            }

            // Descontar stock
            producto.setStock(producto.getStock() - itemReq.getCantidad());
            productoRepository.save(producto);

            // Crear item con snapshot de nombre y precio
            PedidoItem item = new PedidoItem();
            item.setProducto(producto);
            item.setNombreProducto(producto.getNombre());
            item.setCantidad(itemReq.getCantidad());
            item.setPrecioUnitario(producto.getPrecio());

            total = total.add(
                    producto.getPrecio()
                            .multiply(BigDecimal.valueOf(itemReq.getCantidad()))
            );
            items.add(item);
        }

        Pedido pedido = new Pedido();
        pedido.setUsuario(usuario);
        pedido.setDireccionEnvio(request.getDireccionEnvio());
        pedido.setNotas(request.getNotas());
        pedido.setTotal(total);
        pedido.setEstado(Pedido.Estado.PENDIENTE);
        pedido.setDireccionEnvio(
        	    request.getDireccionEnvio() != null && !request.getDireccionEnvio().isBlank()
        	        ? request.getDireccionEnvio()
        	        : "Direccion pendiente de confirmar"
        	);

        // Enlazar items al pedido
        items.forEach(item -> item.setPedido(pedido));
        pedido.setItems(items);

        return pedidoRepository.save(pedido);
    }

    @Transactional
    public Pedido cambiarEstado(Long pedidoId, Pedido.Estado nuevoEstado) {
        Pedido pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));
        pedido.setEstado(nuevoEstado);
        return pedidoRepository.save(pedido);
    }
}