package org.relife.controller;

import jakarta.servlet.http.HttpSession;
import org.relife.dto.LoginRequest;
import org.relife.dto.LoginResponse;
import org.relife.dto.UserDTO;
import org.relife.entity.User;
import org.relife.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request, HttpSession session) {
        LoginResponse response = authService.login(request);
        if (response.isSuccess()) {
            session.setAttribute("user", response.getUser());
        }
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user, HttpSession session) {
        try {
            UserDTO dto = authService.register(user);
            session.setAttribute("user", dto);
            return ResponseEntity.ok(dto);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(java.util.Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/session")
    public ResponseEntity<UserDTO> getSession(HttpSession session) {
        UserDTO user = (UserDTO) session.getAttribute("user");
        return ResponseEntity.ok(user);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpSession session) {
        session.invalidate();
        return ResponseEntity.ok().build();
    }
}
