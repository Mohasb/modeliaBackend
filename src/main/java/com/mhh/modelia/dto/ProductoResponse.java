package com.mhh.modelia.dto;

import com.mhh.modelia.entity.Producto;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class ProductoResponse {

    private Long id;
    private String nombre;
    private String descripcion;
    private BigDecimal precio;
    private Integer stock;
    private String imagenUrl;
    private String modeloGlbUrl;
    private boolean tieneAr;
    private Long categoriaId;
    private String categoriaNombre;
    private Boolean destacado;

    public static ProductoResponse from(Producto p) {
        ProductoResponse dto = new ProductoResponse();
        dto.setId(p.getId());
        dto.setNombre(p.getNombre());
        dto.setDescripcion(p.getDescripcion());
        dto.setPrecio(p.getPrecio());
        dto.setStock(p.getStock());
        dto.setImagenUrl(p.getImagenUrl());
        dto.setModeloGlbUrl(p.getModeloGlbUrl());
        dto.setTieneAr(p.getModeloGlbUrl() != null && !p.getModeloGlbUrl().isBlank());
        dto.setDestacado(p.getDestacado());
        if (p.getCategoria() != null) {
            dto.setCategoriaId(p.getCategoria().getId());
            dto.setCategoriaNombre(p.getCategoria().getNombre());
        }
        return dto;
    }
}