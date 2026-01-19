package com.audrey.homepage.service;

import com.audrey.homepage.entity.Publication;
import com.audrey.homepage.entity.Publication.PublicationType;
import com.audrey.homepage.repository.PublicationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Publication业务逻辑服务类
 */
@Service
public class PublicationService {

    @Autowired
    private PublicationRepository publicationRepository;

    /**
     * 查询所有论文
     */
    public List<Publication> getAllPublications() {
        return publicationRepository.findAll();
    }

    /**
     * 根据ID查询论文
     */
    public Optional<Publication> getPublicationById(Long id) {
        return publicationRepository.findById(id);
    }

    /**
     * 根据教授ID查询所有论文
     */
    public List<Publication> getPublicationsByProfessorId(Long professorId) {
        return publicationRepository.findByProfessorId(professorId);
    }

    /**
     * 根据教授ID查询论文，按年份降序排序
     */
    public List<Publication> getPublicationsByProfessorIdOrderByYear(Long professorId) {
        return publicationRepository.findByProfessorIdOrderByYearDesc(professorId);
    }

    /**
     * 根据教授ID和出版物类型查询
     */
    public List<Publication> getPublicationsByProfessorIdAndType(Long professorId, PublicationType type) {
        return publicationRepository.findByProfessorIdAndPublicationType(professorId, type);
    }

    /**
     * 统计某个教授的论文数量
     */
    public long countByProfessorId(Long professorId) {
        return publicationRepository.countByProfessorId(professorId);
    }

    /**
     * 保存或更新论文
     */
    @Transactional
    public Publication savePublication(Publication publication) {
        return publicationRepository.save(publication);
    }

    /**
     * 批量保存论文
     */
    @Transactional
    public List<Publication> saveAllPublications(List<Publication> publications) {
        return publicationRepository.saveAll(publications);
    }

    /**
     * 删除论文
     */
    @Transactional
    public void deletePublication(Long id) {
        publicationRepository.deleteById(id);
    }
}
