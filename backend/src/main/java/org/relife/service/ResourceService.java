package org.relife.service;

import org.relife.dto.ResourceDTO;
import org.relife.entity.Resource;
import org.relife.entity.User;
import org.relife.repository.ResourceRepository;
import org.relife.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ResourceService {

    private final ResourceRepository resourceRepository;
    private final UserRepository userRepository;

    public ResourceService(ResourceRepository resourceRepository, UserRepository userRepository) {
        this.resourceRepository = resourceRepository;
        this.userRepository = userRepository;
    }

    public List<ResourceDTO> findAll() {
        return resourceRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public List<ResourceDTO> findByCategory(String category) {
        return resourceRepository.findByCategory(category).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public List<ResourceDTO> findByCity(String city) {
        return resourceRepository.findByCity(city).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public ResourceDTO findById(Integer id) {
        return resourceRepository.findById(id)
                .map(this::toDTO)
                .orElseThrow(() -> new RuntimeException("Resource not found"));
    }

    public ResourceDTO create(Resource resource) {
        Resource saved = resourceRepository.save(resource);
        return toDTO(saved);
    }

    public ResourceDTO update(Resource resource) {
        if (!resourceRepository.existsById(resource.getResourceId())) {
            throw new RuntimeException("Resource not found");
        }
        Resource saved = resourceRepository.save(resource);
        return toDTO(saved);
    }

    public void delete(Integer id) {
        resourceRepository.deleteById(id);
    }

    private ResourceDTO toDTO(Resource r) {
        ResourceDTO dto = new ResourceDTO();
        dto.setResourceId(r.getResourceId());
        dto.setUserId(r.getUserId());
        dto.setTitle(r.getTitle());
        dto.setDescription(r.getDescription());
        dto.setCategory(r.getCategory());
        dto.setResourceType(r.getResourceType());
        dto.setCity(r.getCity());
        dto.setStatus(r.getStatus());
        dto.setUrgency(r.getUrgency());
        dto.setImageUrl(r.getImageUrl());
        dto.setCreatedAt(r.getCreatedAt());
        userRepository.findById(r.getUserId())
                .map(User::getFullName)
                .ifPresent(dto::setOwnerName);
        return dto;
    }
}
