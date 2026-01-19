package com.audrey.homepage.service;

import com.audrey.homepage.entity.Professor;
import com.audrey.homepage.repository.ProfessorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Professor业务逻辑服务类
 *
 * @Service 注解：
 * - 标记为Service组件
 * - Spring会自动扫描并创建实例
 * - 可以被Controller注入使用
 */
@Service
public class ProfessorService {

    /**
     * @Autowired 自动注入
     * Spring会自动找到ProfessorRepository的实现并注入
     * 这就是依赖注入（Dependency Injection, DI）
     */
    @Autowired
    private ProfessorRepository professorRepository;

    /**
     * 查询所有教授
     */
    public List<Professor> getAllProfessors() {
        return professorRepository.findAll();
    }

    /**
     * 根据ID查询教授
     */
    public Optional<Professor> getProfessorById(Long id) {
        return professorRepository.findById(id);
    }

    /**
     * 根据邮箱查询教授
     */
    public Optional<Professor> getProfessorByEmail(String email) {
        return professorRepository.findByEmail(email);
    }

    /**
     * 根据姓名查询教授
     */
    public Optional<Professor> getProfessorByName(String name) {
        return professorRepository.findByName(name);
    }

    /**
     * 保存或更新教授信息
     *
     * @Transactional 事务注解：
     * - 确保数据一致性
     * - 如果方法执行失败，会自动回滚
     * - 如果成功，会自动提交
     */
    @Transactional
    public Professor saveProfessor(Professor professor) {
        return professorRepository.save(professor);
    }

    /**
     * 删除教授
     */
    @Transactional
    public void deleteProfessor(Long id) {
        professorRepository.deleteById(id);
    }

    /**
     * 判断教授是否存在
     */
    public boolean existsById(Long id) {
        return professorRepository.existsById(id);
    }

    /**
     * 统计教授数量
     */
    public long count() {
        return professorRepository.count();
    }
}
