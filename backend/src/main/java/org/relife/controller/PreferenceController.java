package org.relife.controller;

import java.util.List;

import org.relife.entity.Preference;
import org.relife.repository.PreferenceRepository;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/preferences")
public class PreferenceController {

    private final PreferenceRepository repo;

    public PreferenceController(PreferenceRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    public List<Preference> getAll() {
        return repo.findAll();
    }
}
