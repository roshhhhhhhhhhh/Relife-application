package org.relife.controller;

import jakarta.servlet.http.HttpSession;
import org.relife.dto.UserDTO;
import org.relife.entity.Request;
import org.relife.repository.RequestRepository;
import org.relife.repository.ResourceRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/requests")
public class RequestController {

    private final RequestRepository requestRepository;
    private final ResourceRepository resourceRepository;

    public RequestController(RequestRepository requestRepository, ResourceRepository resourceRepository) {
        this.requestRepository = requestRepository;
        this.resourceRepository = resourceRepository;
    }

    @GetMapping
    public List<Map<String, Object>> list(HttpSession session) {
        UserDTO user = (UserDTO) session.getAttribute("user");
        if (user == null) {
            return List.of();
        }
        return requestRepository.findByRequesterId(user.getUserId()).stream()
                .map(r -> {
                    var res = resourceRepository.findById(r.getResourceId());
                    return Map.<String, Object>of(
                            "requestId", r.getRequestId(),
                            "resourceId", r.getResourceId(),
                            "message", r.getMessage() != null ? r.getMessage() : "",
                            "status", r.getStatus(),
                            "resourceTitle", res.map(resource -> resource.getTitle()).orElse(""),
                            "createdAt", r.getCreatedAt().toString()
                    );
                })
                .collect(Collectors.toList());
    }

    @PostMapping
    public ResponseEntity<Request> create(@RequestBody Request request, HttpSession session) {
        UserDTO user = (UserDTO) session.getAttribute("user");
        if (user == null) {
            return ResponseEntity.status(401).build();
        }
        request.setRequesterId(user.getUserId());
        return ResponseEntity.ok(requestRepository.save(request));
    }
}
