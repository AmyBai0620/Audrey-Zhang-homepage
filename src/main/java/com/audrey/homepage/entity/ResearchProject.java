package com.audrey.homepage.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

/**
 * 研究项目实体类
 */
@Entity
@Table(name = "research_project")
@Data
public class ResearchProject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "professor_id", nullable = false)
    private Professor professor;

    /**
     * 项目名称
     */
    @Column(nullable = false, length = 300)
    private String title;

    /**
     * 项目描述
     */
    @Column(columnDefinition = "TEXT")
    private String description;

    /**
     * 资助来源（如：国家自然科学基金）
     */
    @Column(length = 200)
    private String fundingSource;

    /**
     * 项目金额
     */
    @Column
    private Double fundingAmount;

    /**
     * 开始日期
     */
    @Column
    private LocalDate startDate;

    /**
     * 结束日期
     */
    @Column
    private LocalDate endDate;

    /**
     * 项目状态
     */
    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private ProjectStatus status;

    public enum ProjectStatus {
        ONGOING,    // 进行中
        COMPLETED,  // 已完成
        PLANNED     // 计划中
    }
}
