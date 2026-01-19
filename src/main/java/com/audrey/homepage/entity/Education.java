package com.audrey.homepage.entity;

import jakarta.persistence.*;
import lombok.Data;

/**
 * 教育背景实体类
 */
@Entity
@Table(name = "education")
@Data
public class Education {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "professor_id", nullable = false)
    private Professor professor;

    /**
     * 学位类型
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private DegreeType degree;

    /**
     * 专业
     */
    @Column(nullable = false, length = 200)
    private String major;

    /**
     * 学校名称
     */
    @Column(nullable = false, length = 200)
    private String university;

    /**
     * 开始年份
     */
    @Column
    private Integer startYear;

    /**
     * 结束年份
     */
    @Column
    private Integer endYear;

    /**
     * 学位类型枚举
     */
    public enum DegreeType {
        BACHELOR,   // 学士
        MASTER,     // 硕士
        PHD,        // 博士
        POSTDOC     // 博士后
    }
}
