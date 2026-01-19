package com.audrey.homepage.controller;

import com.audrey.homepage.entity.TeachingCourse;
import com.audrey.homepage.service.TeachingCourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/teaching-courses")
@CrossOrigin(origins = "*")
public class TeachingCourseController {

    @Autowired
    private TeachingCourseService teachingCourseService;

    @GetMapping
    public ResponseEntity<List<TeachingCourse>> getAllCourses() {
        return ResponseEntity.ok(teachingCourseService.getAllCourses());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TeachingCourse> getCourseById(@PathVariable Long id) {
        return teachingCourseService.getCourseById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/professor/{professorId}")
    public ResponseEntity<List<TeachingCourse>> getCoursesByProfessorId(@PathVariable Long professorId) {
        return ResponseEntity.ok(teachingCourseService.getCoursesByProfessorId(professorId));
    }

    @PostMapping
    public ResponseEntity<TeachingCourse> createCourse(@RequestBody TeachingCourse course) {
        return ResponseEntity.status(HttpStatus.CREATED).body(teachingCourseService.saveCourse(course));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TeachingCourse> updateCourse(@PathVariable Long id, @RequestBody TeachingCourse course) {
        if (!teachingCourseService.getCourseById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        course.setId(id);
        return ResponseEntity.ok(teachingCourseService.saveCourse(course));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCourse(@PathVariable Long id) {
        if (!teachingCourseService.getCourseById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        teachingCourseService.deleteCourse(id);
        return ResponseEntity.noContent().build();
    }
}
