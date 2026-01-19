package com.audrey.homepage.controller;

import com.audrey.homepage.entity.Award;
import com.audrey.homepage.service.AwardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/awards")
@CrossOrigin(origins = "*")
public class AwardController {

    @Autowired
    private AwardService awardService;

    @GetMapping
    public ResponseEntity<List<Award>> getAllAwards() {
        return ResponseEntity.ok(awardService.getAllAwards());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Award> getAwardById(@PathVariable Long id) {
        return awardService.getAwardById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/professor/{professorId}")
    public ResponseEntity<List<Award>> getAwardsByProfessorId(@PathVariable Long professorId) {
        return ResponseEntity.ok(awardService.getAwardsByProfessorId(professorId));
    }

    @PostMapping
    public ResponseEntity<Award> createAward(@RequestBody Award award) {
        return ResponseEntity.status(HttpStatus.CREATED).body(awardService.saveAward(award));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Award> updateAward(@PathVariable Long id, @RequestBody Award award) {
        if (!awardService.getAwardById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        award.setId(id);
        return ResponseEntity.ok(awardService.saveAward(award));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAward(@PathVariable Long id) {
        if (!awardService.getAwardById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        awardService.deleteAward(id);
        return ResponseEntity.noContent().build();
    }
}
