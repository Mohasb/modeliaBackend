package com.mhh.modelia.dto;

import com.mhh.modelia.entity.Pedido;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class PedidoResponse {
    private Long id;
    private String estado;
    private BigDecimal total;
    private String direccionEnvio;
    private String notas;
    private LocalDateTime createdAt;
    private List<ItemResponse> items;

    @Data
    public static class ItemResponse {
        private Long id;
        private Long productoId;
        private String nombreProducto;
        private Integer cantidad;
        private BigDecimal precioUnitario;
        private BigDecimal subtotal;
    }

    public static PedidoResponse from(Pedido p) {
        PedidoResponse dto = new PedidoResponse();
        dto.setId(p.getId());
        dto.setEstado(p.getEstado().name());
        dto.setTotal(p.getTotal());
        dto.setDireccionEnvio(p.getDireccionEnvio());
        dto.setNotas(p.getNotas());
        dto.setCreatedAt(p.getCreatedAt());
        dto.setItems(p.getItems() == null ? List.of() :
            p.getItems().stream().map(item -> {
                ItemResponse ir = new ItemResponse();
                ir.setId(item.getId());
                ir.setProductoId(item.getProducto().getId());
                ir.setNombreProducto(item.getNombreProducto());
                ir.setCantidad(item.getCantidad());
                ir.setPrecioUnitario(item.getPrecioUnitario());
                ir.setSubtotal(item.getPrecioUnitario()
                    .multiply(java.math.BigDecimal.valueOf(item.getCantidad())));
                return ir;
            }).collect(Collectors.toList())
        );
        return dto;
    }
}