package com.mhh.modelia.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class PerfilRequest {

    @NotBlank
    @Size(min = 2, max = 100)
    private String nombre;

    private String direccion;
}