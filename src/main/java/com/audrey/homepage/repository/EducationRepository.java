package com.audrey.homepage.repository;

import com.audrey.homepage.entity.Education;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Education实体的Repository接口
 */
@Repository
public interface EducationRepository extends JpaRepository<Education, Long> {

    /**
     * 根据教授ID查询所有教育背景
     */
    List<Education> findByProfessorId(Long professorId);

    /**
     * 根据教授ID查询教育背景，按结束年份降序排序
     */
    List<Education> findByProfessorIdOrderByEndYearDesc(Long professorId);
}
