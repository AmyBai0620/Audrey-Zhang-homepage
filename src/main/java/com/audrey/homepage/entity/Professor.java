package com.audrey.homepage.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.util.ArrayList;
import java.util.List;

/**
 * 教授信息实体类
 * 这个类对应数据库中的professor表
 */
@Entity
@Table(name = "professor")
@Data
public class Professor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 教授姓名
     */
    @Column(nullable = false, length = 50)
    private String name;

    /**
     * 职称（如：副教授、教授）
     */
    @Column(length = 50)
    private String title;

    /**
     * 所在学校
     */
    @Column(length = 100)
    private String university;

    /**
     * 所在院系
     */
    @Column(length = 100)
    private String department;

    /**
     * 研究方向
     */
    @Column(length = 500)
    private String researchInterests;

    /**
     * 电子邮箱
     */
    @Column(length = 100)
    private String email;

    /**
     * 个人简介
     */
    @Column(columnDefinition = "TEXT")
    private String biography;

    /**
     * 一对多关系：一个教授有多篇论文
     * mappedBy指向Publication类中的professor字段
     * cascade表示级联操作：删除教授时，相关论文也会被删除
     */
    @OneToMany(mappedBy = "professor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Publication> publications = new ArrayList<>();

    /**
     * 一对多关系：一个教授有多个教育背景
     */
    @OneToMany(mappedBy = "professor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Education> educations = new ArrayList<>();

    /**
     * 一对多关系：一个教授有多个研究项目
     */
    @OneToMany(mappedBy = "professor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ResearchProject> researchProjects = new ArrayList<>();

    /**
     * 一对多关系：一个教授教授多门课程
     */
    @OneToMany(mappedBy = "professor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TeachingCourse> teachingCourses = new ArrayList<>();

    /**
     * 一对多关系：一个教授有多个奖项
     */
    @OneToMany(mappedBy = "professor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Award> awards = new ArrayList<>();
}
