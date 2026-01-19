package com.audrey.homepage.repository;

import com.audrey.homepage.entity.TeachingCourse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * TeachingCourse实体的Repository接口
 */
@Repository
public interface TeachingCourseRepository extends JpaRepository<TeachingCourse, Long> {

    /**
     * 根据教授ID查询所有课程
     */
    List<TeachingCourse> findByProfessorId(Long professorId);

    /**
     * 根据教授ID和年份查询课程
     */
    List<TeachingCourse> findByProfessorIdAndYear(Long professorId, Integer year);

    /**
     * 根据教授ID查询课程，按年份和学期排序
     */
    List<TeachingCourse> findByProfessorIdOrderByYearDescSemesterAsc(Long professorId);
}
