package org.relife.service;

import org.relife.dto.LoginRequest;
import org.relife.dto.LoginResponse;
import org.relife.dto.UserDTO;
import org.relife.entity.User;
import org.relife.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public LoginResponse login(LoginRequest request) {
        var userOpt = userRepository.findByUsername(request.getUsername());
        if (userOpt.isEmpty()) {
            return new LoginResponse(false, "Invalid username or password", null);
        }
        User user = userOpt.get();
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            return new LoginResponse(false, "Invalid username or password", null);
        }
        return new LoginResponse(true, "Login successful", toDTO(user));
    }

    public UserDTO register(User user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new IllegalArgumentException("Username already exists");
        }
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("Email already registered");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(user.getRole() != null ? user.getRole() : "USER");
        User saved = userRepository.save(user);
        return toDTO(saved);
    }

    public static UserDTO toDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setUserId(user.getUserId());
        dto.setFullName(user.getFullName());
        dto.setDob(user.getDob());
        dto.setGender(user.getGender());
        dto.setEmail(user.getEmail());
        dto.setPhone(user.getPhone());
        dto.setAddress(user.getAddress());
        dto.setCity(user.getCity());
        dto.setUsername(user.getUsername());
        dto.setProfilePicture(user.getProfilePicture());
        dto.setBio(user.getBio());
        dto.setRole(user.getRole());
        return dto;
    }
}
