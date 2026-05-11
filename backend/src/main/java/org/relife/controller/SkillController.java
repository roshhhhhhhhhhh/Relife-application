package org.relife.controller;

import java.util.List;

import org.relife.entity.Skill;
import org.relife.repository.SkillRepository;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/skills")
public class SkillController {

    private final SkillRepository repo;

    public SkillController(SkillRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    public List<Skill> getAll() {
        return repo.findAll();
    }
}
