package com.audrey.homepage.repository;

import com.audrey.homepage.entity.Award;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Award实体的Repository接口
 */
@Repository
public interface AwardRepository extends JpaRepository<Award, Long> {

    /**
     * 根据教授ID查询所有奖项
     */
    List<Award> findByProfessorId(Long professorId);

    /**
     * 根据教授ID查询奖项，按年份降序排序
     */
    List<Award> findByProfessorIdOrderByYearDesc(Long professorId);
}
