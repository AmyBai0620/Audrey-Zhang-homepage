package com.audrey.homepage.service;

import com.audrey.homepage.entity.TeachingCourse;
import com.audrey.homepage.repository.TeachingCourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class TeachingCourseService {

    @Autowired
    private TeachingCourseRepository teachingCourseRepository;

    public List<TeachingCourse> getAllCourses() {
        return teachingCourseRepository.findAll();
    }

    public Optional<TeachingCourse> getCourseById(Long id) {
        return teachingCourseRepository.findById(id);
    }

    public List<TeachingCourse> getCoursesByProfessorId(Long professorId) {
        return teachingCourseRepository.findByProfessorIdOrderByYearDescSemesterAsc(professorId);
    }

    @Transactional
    public TeachingCourse saveCourse(TeachingCourse course) {
        return teachingCourseRepository.save(course);
    }

    @Transactional
    public void deleteCourse(Long id) {
        teachingCourseRepository.deleteById(id);
    }
}
