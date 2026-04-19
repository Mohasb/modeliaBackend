package com.mhh.modelia.service;

import com.mhh.modelia.entity.Producto;
import com.mhh.modelia.repository.ProductoRepository;

import jakarta.transaction.Transactional;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductoService {

    private final ProductoRepository productoRepository;

    public ProductoService(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    public List<Producto> findAll(Long categoriaId, String nombre) {
        if (categoriaId != null) {
            return productoRepository.findByCategoriaIdAndActivoTrue(categoriaId);
        }
        if (nombre != null && !nombre.isBlank()) {
            return productoRepository
                    .findByNombreContainingIgnoreCaseAndActivoTrue(nombre);
        }
        return productoRepository.findByActivoTrue();
    }

    public Producto findById(Long id) {
        return productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
    }

    public Producto save(Producto producto) {
        return productoRepository.save(producto);
    }

    public void delete(Long id) {
        Producto producto = findById(id);
        producto.setActivo(false);
        productoRepository.save(producto);
    }
    
    public List<Producto> findDestacados() {
        return productoRepository.findByDestacadoTrueOrderByCreatedAtDesc();
    }

    @Transactional
    public Producto toggleDestacado(Long id) {
        Producto p = productoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        p.setDestacado(!p.getDestacado());
        return productoRepository.save(p);
    }
}

/*package com.mhh.modelia.service;

import com.mhh.modelia.entity.Producto;
import com.mhh.modelia.repository.ProductoRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@Transactional(Transactional.TxType.SUPPORTS) // sesión abierta en todas las llamadas
public class ProductoService {

    private final ProductoRepository productoRepository;

    public ProductoService(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    public List<Producto> findAll(Long categoriaId, String nombre) {
        if (categoriaId != null) {
            return productoRepository.findByCategoriaIdAndActivoTrue(categoriaId);
        }
        if (nombre != null && !nombre.isBlank()) {
            return productoRepository
                    .findByNombreContainingIgnoreCaseAndActivoTrue(nombre);
        }
        return productoRepository.findByActivoTrue();
    }

    public Producto findById(Long id) {
        return productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
    }

    public Producto save(Producto producto) {
        return productoRepository.save(producto);
    }

    public void delete(Long id) {
        Producto producto = findById(id);
        producto.setActivo(false);
        productoRepository.save(producto);
    }

    public List<Producto> findDestacados() {
        return productoRepository.findByDestacadoTrueOrderByCreatedAtDesc();
    }

    @Transactional(Transactional.TxType.REQUIRED)
    public Producto toggleDestacado(Long id) {
        Producto p = productoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        p.setDestacado(!p.getDestacado());
        return productoRepository.save(p);
    }
}*/