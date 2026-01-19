package com.audrey.homepage.service;

import com.audrey.homepage.entity.ResearchProject;
import com.audrey.homepage.entity.ResearchProject.ProjectStatus;
import com.audrey.homepage.repository.ResearchProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ResearchProjectService {

    @Autowired
    private ResearchProjectRepository researchProjectRepository;

    public List<ResearchProject> getAllProjects() {
        return researchProjectRepository.findAll();
    }

    public Optional<ResearchProject> getProjectById(Long id) {
        return researchProjectRepository.findById(id);
    }

    public List<ResearchProject> getProjectsByProfessorId(Long professorId) {
        return researchProjectRepository.findByProfessorIdOrderByStartDateDesc(professorId);
    }

    public List<ResearchProject> getProjectsByProfessorIdAndStatus(Long professorId, ProjectStatus status) {
        return researchProjectRepository.findByProfessorIdAndStatus(professorId, status);
    }

    @Transactional
    public ResearchProject saveProject(ResearchProject project) {
        return researchProjectRepository.save(project);
    }

    @Transactional
    public void deleteProject(Long id) {
        researchProjectRepository.deleteById(id);
    }
}
