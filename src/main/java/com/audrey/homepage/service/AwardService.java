package com.audrey.homepage.service;

import com.audrey.homepage.entity.Award;
import com.audrey.homepage.repository.AwardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class AwardService {

    @Autowired
    private AwardRepository awardRepository;

    public List<Award> getAllAwards() {
        return awardRepository.findAll();
    }

    public Optional<Award> getAwardById(Long id) {
        return awardRepository.findById(id);
    }

    public List<Award> getAwardsByProfessorId(Long professorId) {
        return awardRepository.findByProfessorIdOrderByYearDesc(professorId);
    }

    @Transactional
    public Award saveAward(Award award) {
        return awardRepository.save(award);
    }

    @Transactional
    public void deleteAward(Long id) {
        awardRepository.deleteById(id);
    }
}
