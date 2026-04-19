package com.mhh.modelia.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import java.util.List;

@Data
public class PedidoRequest {

    private String direccionEnvio;
    private String notas;

    @NotEmpty
    private List<ItemRequest> items;

    @Data
    public static class ItemRequest {
        private Long productoId;
        private Integer cantidad;
    }
}