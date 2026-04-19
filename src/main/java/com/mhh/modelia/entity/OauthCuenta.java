package com.mhh.modelia.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "oauth_cuentas")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OauthCuenta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Column(nullable = false, length = 50)
    private String proveedor;

    @Column(name = "proveedor_user_id", nullable = false, length = 255)
    private String proveedorUserId;

    @Column(name = "email_proveedor", length = 150)
    private String emailProveedor;

    @Column(name = "access_token", columnDefinition = "TEXT")
    private String accessToken;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}