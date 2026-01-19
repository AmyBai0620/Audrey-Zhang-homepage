package com.audrey.homepage.service;

import com.audrey.homepage.entity.Education;
import com.audrey.homepage.repository.EducationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class EducationService {

    @Autowired
    private EducationRepository educationRepository;

    public List<Education> getAllEducations() {
        return educationRepository.findAll();
    }

    public Optional<Education> getEducationById(Long id) {
        return educationRepository.findById(id);
    }

    public List<Education> getEducationsByProfessorId(Long professorId) {
        return educationRepository.findByProfessorIdOrderByEndYearDesc(professorId);
    }

    @Transactional
    public Education saveEducation(Education education) {
        return educationRepository.save(education);
    }

    @Transactional
    public void deleteEducation(Long id) {
        educationRepository.deleteById(id);
    }
}
