package com.audrey.homepage.repository;

import com.audrey.homepage.entity.Publication;
import com.audrey.homepage.entity.Publication.PublicationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Publication实体的Repository接口
 */
@Repository
public interface PublicationRepository extends JpaRepository<Publication, Long> {

    /**
     * 根据教授ID查询所有论文
     * SELECT * FROM publication WHERE professor_id = ?
     */
    List<Publication> findByProfessorId(Long professorId);

    /**
     * 根据教授ID和年份查询论文
     * SELECT * FROM publication WHERE professor_id = ? AND year = ?
     */
    List<Publication> findByProfessorIdAndYear(Long professorId, Integer year);

    /**
     * 根据教授ID查询论文，按年份降序排序
     * SELECT * FROM publication WHERE professor_id = ? ORDER BY year DESC
     */
    List<Publication> findByProfessorIdOrderByYearDesc(Long professorId);

    /**
     * 根据教授ID和出版物类型查询
     */
    List<Publication> findByProfessorIdAndPublicationType(Long professorId, PublicationType type);

    /**
     * 统计某个教授的论文数量
     * SELECT COUNT(*) FROM publication WHERE professor_id = ?
     */
    long countByProfessorId(Long professorId);
}
