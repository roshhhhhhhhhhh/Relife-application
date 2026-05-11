package org.relife.controller;

import jakarta.servlet.http.HttpSession;
import org.relife.dto.ResourceDTO;
import org.relife.dto.UserDTO;
import org.relife.entity.Resource;
import org.relife.service.MatchingService;
import org.relife.service.ResourceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/resources")
public class ResourceController {

    private final ResourceService resourceService;
    private final MatchingService matchingService;

    public ResourceController(ResourceService resourceService, MatchingService matchingService) {
        this.resourceService = resourceService;
        this.matchingService = matchingService;
    }

    @GetMapping
    public List<ResourceDTO> list(
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) Integer urgency) {
        if (city != null || category != null || urgency != null) {
            return matchingService.getMatches(city, category, urgency);
        }
        return resourceService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResourceDTO> get(@PathVariable Integer id) {
        try {
            return ResponseEntity.ok(resourceService.findById(id));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<ResourceDTO> create(@RequestBody Resource resource, HttpSession session) {
        UserDTO user = (UserDTO) session.getAttribute("user");
        if (user == null) {
            return ResponseEntity.status(401).build();
        }
        resource.setUserId(user.getUserId());
        return ResponseEntity.ok(resourceService.create(resource));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResourceDTO> update(@PathVariable Integer id, @RequestBody Resource resource, HttpSession session) {
        UserDTO user = (UserDTO) session.getAttribute("user");
        if (user == null) {
            return ResponseEntity.status(401).build();
        }
        resource.setResourceId(id);
        resource.setUserId(user.getUserId());
        return ResponseEntity.ok(resourceService.update(resource));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id, HttpSession session) {
        UserDTO user = (UserDTO) session.getAttribute("user");
        if (user == null) {
            return ResponseEntity.status(401).build();
        }
        resourceService.delete(id);
        return ResponseEntity.ok().build();
    }
}
