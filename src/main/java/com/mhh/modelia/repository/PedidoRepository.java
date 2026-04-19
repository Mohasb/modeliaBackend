package com.mhh.modelia.repository;

import com.mhh.modelia.entity.Pedido;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    @EntityGraph(attributePaths = {
        "usuario",
        "items",
        "items.producto",
        "items.producto.categoria"
    })
    List<Pedido> findByUsuarioIdOrderByCreatedAtDesc(Long usuarioId);

    @EntityGraph(attributePaths = {
        "usuario",
        "items",
        "items.producto",
        "items.producto.categoria"
    })
    List<Pedido> findAll();

    @EntityGraph(attributePaths = {
        "usuario",
        "items",
        "items.producto",
        "items.producto.categoria"
    })
    Optional<Pedido> findById(Long id);
}