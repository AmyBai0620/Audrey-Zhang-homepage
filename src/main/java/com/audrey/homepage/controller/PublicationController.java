package com.audrey.homepage.controller;

import com.audrey.homepage.entity.Publication;
import com.audrey.homepage.service.PublicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Publication控制器
 */
@RestController
@RequestMapping("/api/publications")
@CrossOrigin(origins = "*")
public class PublicationController {

    @Autowired
    private PublicationService publicationService;

    /**
     * 获取所有论文
     * GET /api/publications
     */
    @GetMapping
    public ResponseEntity<List<Publication>> getAllPublications() {
        List<Publication> publications = publicationService.getAllPublications();
        return ResponseEntity.ok(publications);
    }

    /**
     * 根据ID获取论文
     * GET /api/publications/1
     */
    @GetMapping("/{id}")
    public ResponseEntity<Publication> getPublicationById(@PathVariable Long id) {
        return publicationService.getPublicationById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * 根据教授ID获取所有论文
     * GET /api/publications/professor/1
     */
    @GetMapping("/professor/{professorId}")
    public ResponseEntity<List<Publication>> getPublicationsByProfessorId(@PathVariable Long professorId) {
        List<Publication> publications = publicationService.getPublicationsByProfessorIdOrderByYear(professorId);
        return ResponseEntity.ok(publications);
    }

    /**
     * 统计某个教授的论文数量
     * GET /api/publications/professor/1/count
     */
    @GetMapping("/professor/{professorId}/count")
    public ResponseEntity<Long> countByProfessorId(@PathVariable Long professorId) {
        long count = publicationService.countByProfessorId(professorId);
        return ResponseEntity.ok(count);
    }

    /**
     * 创建新论文
     * POST /api/publications
     */
    @PostMapping
    public ResponseEntity<Publication> createPublication(@RequestBody Publication publication) {
        Publication savedPublication = publicationService.savePublication(publication);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedPublication);
    }

    /**
     * 更新论文
     * PUT /api/publications/1
     */
    @PutMapping("/{id}")
    public ResponseEntity<Publication> updatePublication(
            @PathVariable Long id,
            @RequestBody Publication publication) {

        if (!publicationService.getPublicationById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }

        publication.setId(id);
        Publication updatedPublication = publicationService.savePublication(publication);
        return ResponseEntity.ok(updatedPublication);
    }

    /**
     * 删除论文
     * DELETE /api/publications/1
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePublication(@PathVariable Long id) {
        if (!publicationService.getPublicationById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }

        publicationService.deletePublication(id);
        return ResponseEntity.noContent().build();
    }
}
