package com.audrey.homepage.repository;

import com.audrey.homepage.entity.ResearchProject;
import com.audrey.homepage.entity.ResearchProject.ProjectStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * ResearchProject实体的Repository接口
 */
@Repository
public interface ResearchProjectRepository extends JpaRepository<ResearchProject, Long> {

    /**
     * 根据教授ID查询所有研究项目
     */
    List<ResearchProject> findByProfessorId(Long professorId);

    /**
     * 根据教授ID和项目状态查询
     */
    List<ResearchProject> findByProfessorIdAndStatus(Long professorId, ProjectStatus status);

    /**
     * 根据教授ID查询项目，按开始日期降序排序
     */
    List<ResearchProject> findByProfessorIdOrderByStartDateDesc(Long professorId);
}
