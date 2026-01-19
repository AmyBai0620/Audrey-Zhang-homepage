package com.audrey.homepage.controller;

import com.audrey.homepage.entity.ResearchProject;
import com.audrey.homepage.service.ResearchProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/research-projects")
@CrossOrigin(origins = "*")
public class ResearchProjectController {

    @Autowired
    private ResearchProjectService researchProjectService;

    @GetMapping
    public ResponseEntity<List<ResearchProject>> getAllProjects() {
        return ResponseEntity.ok(researchProjectService.getAllProjects());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResearchProject> getProjectById(@PathVariable Long id) {
        return researchProjectService.getProjectById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/professor/{professorId}")
    public ResponseEntity<List<ResearchProject>> getProjectsByProfessorId(@PathVariable Long professorId) {
        return ResponseEntity.ok(researchProjectService.getProjectsByProfessorId(professorId));
    }

    @PostMapping
    public ResponseEntity<ResearchProject> createProject(@RequestBody ResearchProject project) {
        return ResponseEntity.status(HttpStatus.CREATED).body(researchProjectService.saveProject(project));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResearchProject> updateProject(@PathVariable Long id, @RequestBody ResearchProject project) {
        if (!researchProjectService.getProjectById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        project.setId(id);
        return ResponseEntity.ok(researchProjectService.saveProject(project));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable Long id) {
        if (!researchProjectService.getProjectById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        researchProjectService.deleteProject(id);
        return ResponseEntity.noContent().build();
    }
}
