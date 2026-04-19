package com.mhh.modelia.repository;

import com.mhh.modelia.entity.Producto;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface ProductoRepository extends JpaRepository<Producto, Long> {

    @EntityGraph(attributePaths = {"categoria"})
    List<Producto> findByActivoTrue();

    @EntityGraph(attributePaths = {"categoria"})
    List<Producto> findByCategoriaIdAndActivoTrue(Long categoriaId);

    @EntityGraph(attributePaths = {"categoria"})
    List<Producto> findByNombreContainingIgnoreCaseAndActivoTrue(String nombre);

    @EntityGraph(attributePaths = {"categoria"})
    Optional<Producto> findById(Long id);
    
    @EntityGraph(attributePaths = {"categoria"})
    List<Producto> findByDestacadoTrueOrderByCreatedAtDesc();
}