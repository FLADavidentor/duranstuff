package ni.edu.uam.gestion_productos.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ni.edu.uam.gestion_productos.dto.LoginRequest;
import ni.edu.uam.gestion_productos.dto.LoginResponse;
import ni.edu.uam.gestion_productos.service.TokenService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager manager;
    private final TokenService tokens;

    public AuthController(AuthenticationManager manager, TokenService tokens) {
        this.manager = manager;
        this.tokens = tokens;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest datos) {

        Authentication auth = manager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        datos.username(), datos.password()));

        return ResponseEntity.ok(
                new LoginResponse(tokens.crearToken(auth), "Bearer"));
    }

    // Reto adicional: requiere un JWT valido, no acepta solicitudes anonimas.
    @GetMapping("/perfil")
    public Map<String, Object> perfil(Authentication auth) {

        List<String> roles = auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .filter(a -> a.startsWith("ROLE_"))
                .map(a -> a.substring(5))
                .toList();

        return Map.of(
                "usuario", auth.getName(),
                "roles", roles);
    }
}
