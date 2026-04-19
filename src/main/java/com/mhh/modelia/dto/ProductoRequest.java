package com.mhh.modelia.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductoRequest {

    @NotBlank
    private String nombre;

    private String descripcion;

    @NotNull
    @Positive
    private BigDecimal precio;

    @NotNull
    private Long categoriaId;

    private Integer stock = 0;
    private Integer stockMinimo = 5;
    private String imagenUrl;
    private String modeloGlbUrl;
}