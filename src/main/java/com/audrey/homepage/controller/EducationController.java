package com.audrey.homepage.controller;

import com.audrey.homepage.entity.Education;
import com.audrey.homepage.service.EducationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/educations")
@CrossOrigin(origins = "*")
public class EducationController {

    @Autowired
    private EducationService educationService;

    @GetMapping
    public ResponseEntity<List<Education>> getAllEducations() {
        return ResponseEntity.ok(educationService.getAllEducations());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Education> getEducationById(@PathVariable Long id) {
        return educationService.getEducationById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/professor/{professorId}")
    public ResponseEntity<List<Education>> getEducationsByProfessorId(@PathVariable Long professorId) {
        return ResponseEntity.ok(educationService.getEducationsByProfessorId(professorId));
    }

    @PostMapping
    public ResponseEntity<Education> createEducation(@RequestBody Education education) {
        return ResponseEntity.status(HttpStatus.CREATED).body(educationService.saveEducation(education));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Education> updateEducation(@PathVariable Long id, @RequestBody Education education) {
        if (!educationService.getEducationById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        education.setId(id);
        return ResponseEntity.ok(educationService.saveEducation(education));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEducation(@PathVariable Long id) {
        if (!educationService.getEducationById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        educationService.deleteEducation(id);
        return ResponseEntity.noContent().build();
    }
}
