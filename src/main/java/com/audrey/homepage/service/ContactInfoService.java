package com.audrey.homepage.service;

import com.audrey.homepage.entity.ContactInfo;
import com.audrey.homepage.repository.ContactInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * 联系方式业务逻辑服务类
 */
@Service
public class ContactInfoService {

    @Autowired
    private ContactInfoRepository contactInfoRepository;

    /**
     * 查询所有联系方式
     */
    public List<ContactInfo> getAllContactInfos() {
        return contactInfoRepository.findAll();
    }

    /**
     * 根据ID查询联系方式
     */
    public Optional<ContactInfo> getContactInfoById(Long id) {
        return contactInfoRepository.findById(id);
    }

    /**
     * 根据教授ID查询联系方式
     */
    public Optional<ContactInfo> getContactInfoByProfessorId(Long professorId) {
        return contactInfoRepository.findByProfessorId(professorId);
    }

    /**
     * 检查教授是否已有联系方式
     */
    public boolean existsByProfessorId(Long professorId) {
        return contactInfoRepository.existsByProfessorId(professorId);
    }

    /**
     * 保存或更新联系方式
     */
    @Transactional
    public ContactInfo saveContactInfo(ContactInfo contactInfo) {
        return contactInfoRepository.save(contactInfo);
    }

    /**
     * 删除联系方式
     */
    @Transactional
    public void deleteContactInfo(Long id) {
        contactInfoRepository.deleteById(id);
    }

    /**
     * 根据教授ID删除联系方式
     */
    @Transactional
    public void deleteByProfessorId(Long professorId) {
        contactInfoRepository.deleteByProfessorId(professorId);
    }
}
