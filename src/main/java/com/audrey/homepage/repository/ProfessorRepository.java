package com.audrey.homepage.repository;

import com.audrey.homepage.entity.Professor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Professor实体的Repository接口
 *
 * JpaRepository<Professor, Long> 说明：
 * - Professor: 实体类型
 * - Long: 主键类型
 *
 * 继承JpaRepository后，自动获得以下方法：
 * - save(professor)          保存或更新教授信息
 * - findById(id)             根据ID查询
 * - findAll()                查询所有教授
 * - deleteById(id)           根据ID删除
 * - count()                  统计教授数量
 * 等等...
 */
@Repository  // 标记为Repository组件，Spring会自动扫描并创建实例
public interface ProfessorRepository extends JpaRepository<Professor, Long> {

    /**
     * 根据邮箱查询教授
     * Spring Data JPA会根据方法名自动生成SQL：
     * SELECT * FROM professor WHERE email = ?
     */
    Optional<Professor> findByEmail(String email);

    /**
     * 根据姓名查询教授
     * SELECT * FROM professor WHERE name = ?
     */
    Optional<Professor> findByName(String name);

    /**
     * 根据学校和院系查询教授列表
     * SELECT * FROM professor WHERE university = ? AND department = ?
     */
    java.util.List<Professor> findByUniversityAndDepartment(String university, String department);
}
