package com.audrey.homepage.entity;

import jakarta.persistence.*;
import lombok.Data;

/**
 * 获奖荣誉实体类
 */
@Entity
@Table(name = "award")
@Data
public class Award {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "professor_id", nullable = false)
    private Professor professor;

    /**
     * 奖项名称
     */
    @Column(nullable = false, length = 300)
    private String title;

    /**
     * 颁发机构
     */
    @Column(length = 200)
    private String organization;

    /**
     * 获奖年份
     */
    @Column
    private Integer year;

    /**
     * 奖项描述
     */
    @Column(columnDefinition = "TEXT")
    private String description;

    /**
     * 奖项级别
     */
    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private AwardLevel level;

    public enum AwardLevel {
        INTERNATIONAL,  // 国际级
        NATIONAL,       // 国家级
        PROVINCIAL,     // 省级
        UNIVERSITY,     // 校级
        OTHER          // 其他
    }
}
