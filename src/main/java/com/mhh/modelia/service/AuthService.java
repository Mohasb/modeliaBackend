package com.mhh.modelia.service;

import com.mhh.modelia.dto.*;
import com.mhh.modelia.entity.RefreshToken;
import com.mhh.modelia.entity.Usuario;
import com.mhh.modelia.repository.RefreshTokenRepository;
import com.mhh.modelia.repository.UsuarioRepository;
import com.mhh.modelia.security.JwtUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    @Value("${jwt.refresh-expiration-ms}")
    private long refreshExpirationMs;

    public AuthService(UsuarioRepository usuarioRepository,
                       RefreshTokenRepository refreshTokenRepository,
                       PasswordEncoder passwordEncoder,
                       JwtUtil jwtUtil,
                       AuthenticationManager authenticationManager) {
        this.usuarioRepository = usuarioRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
    }

    @Transactional
    public MensajeResponse register(RegisterRequest request) {
        if (usuarioRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("El email ya está registrado");
        }

        Usuario usuario = new Usuario();
        usuario.setNombre(request.getNombre());
        usuario.setEmail(request.getEmail());
        usuario.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        usuario.setRol(Usuario.Rol.CLIENTE);

        usuarioRepository.save(usuario);

        return new MensajeResponse("Usuario registrado correctamente");
    }

    @Transactional
    public JwtResponse login(LoginRequest request) {
        // Spring Security valida email + password contra la BD
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        Usuario usuario = usuarioRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        
        refreshTokenRepository.deleteByUsuarioId(usuario.getId());

        // Generar access token JWT
        String accessToken = jwtUtil.generarToken(
                usuario.getEmail(),
                usuario.getRol().name()
        );

        // Generar refresh token y guardarlo en BD
        String refreshTokenStr = UUID.randomUUID().toString();
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUsuario(usuario);
        refreshToken.setToken(refreshTokenStr);
        refreshToken.setExpiresAt(
                LocalDateTime.now().plusSeconds(refreshExpirationMs / 1000)
        );
        refreshTokenRepository.save(refreshToken);

        return new JwtResponse(
                accessToken,
                refreshTokenStr,
                usuario.getId(),
                usuario.getNombre(),
                usuario.getEmail(),
                usuario.getRol().name()
        );
    }

    @Transactional
    public JwtResponse refreshToken(RefreshTokenRequest request) {
        RefreshToken refreshToken = refreshTokenRepository
                .findByToken(request.getRefreshToken())
                .orElseThrow(() -> new RuntimeException("Refresh token no válido"));

        if (refreshToken.isRevocado()) {
            throw new RuntimeException("Refresh token revocado");
        }

        if (refreshToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Refresh token expirado");
        }

        Usuario usuario = refreshToken.getUsuario();
        String newAccessToken = jwtUtil.generarToken(
                usuario.getEmail(),
                usuario.getRol().name()
        );

        return new JwtResponse(
                newAccessToken,
                refreshToken.getToken(),
                usuario.getId(),
                usuario.getNombre(),
                usuario.getEmail(),
                usuario.getRol().name()
        );
    }

    @Transactional
    public MensajeResponse logout(RefreshTokenRequest request) {
        refreshTokenRepository.findByToken(request.getRefreshToken())
                .ifPresent(refreshTokenRepository::delete);
        return new MensajeResponse("Sesion cerrada correctamente");
    }
}