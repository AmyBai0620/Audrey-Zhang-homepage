package com.audrey.homepage.repository;

import com.audrey.homepage.entity.ContactInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 联系方式数据访问接口
 */
@Repository
public interface ContactInfoRepository extends JpaRepository<ContactInfo, Long> {

    /**
     * 根据教授ID查找联系方式
     */
    Optional<ContactInfo> findByProfessorId(Long professorId);

    /**
     * 检查教授是否已有联系方式
     */
    boolean existsByProfessorId(Long professorId);

    /**
     * 根据教授ID删除联系方式
     */
    void deleteByProfessorId(Long professorId);
}
