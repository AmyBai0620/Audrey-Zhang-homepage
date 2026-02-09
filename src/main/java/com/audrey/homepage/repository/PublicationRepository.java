package com.audrey.homepage.repository;

import com.audrey.homepage.entity.Publication;
import com.audrey.homepage.entity.Publication.PublicationType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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

    /**
     * 综合搜索论文（支持标题、作者、期刊搜索，支持年份和类型筛选）
     * 使用JPQL查询，支持分页
     *
     * @param keyword 关键词（搜索标题、作者、期刊）
     * @param year 年份（可选）
     * @param type 类型（可选）
     * @param pageable 分页参数
     * @return 分页的论文列表
     */
    @Query("SELECT p FROM Publication p WHERE " +
            "(:keyword IS NULL OR :keyword = '' OR " +
            "LOWER(p.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(p.authors) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(p.journal) LIKE LOWER(CONCAT('%', :keyword, '%'))) AND " +
            "(:year IS NULL OR p.year = :year) AND " +
            "(:type IS NULL OR p.publicationType = :type) " +
            "ORDER BY p.year DESC, p.id DESC")
    Page<Publication> searchPublications(
            @Param("keyword") String keyword,
            @Param("year") Integer year,
            @Param("type") PublicationType type,
            Pageable pageable
    );

    /**
     * 获取所有年份列表（用于筛选器）
     * 返回按年份降序排列的不重复年份列表
     */
    @Query("SELECT DISTINCT p.year FROM Publication p WHERE p.year IS NOT NULL ORDER BY p.year DESC")
    List<Integer> findAllDistinctYears();
}
