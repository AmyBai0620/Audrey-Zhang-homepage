package com.audrey.homepage.entity;

import jakarta.persistence.*;
import lombok.Data;

/**
 * 教学课程实体类
 */
@Entity
@Table(name = "teaching_course")
@Data
public class TeachingCourse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "professor_id", nullable = false)
    private Professor professor;

    /**
     * 课程名称
     */
    @Column(nullable = false, length = 200)
    private String courseName;

    /**
     * 课程代码
     */
    @Column(length = 50)
    private String courseCode;

    /**
     * 学期（如：春季、秋季）
     */
    @Column(length = 50)
    private String semester;

    /**
     * 年份
     */
    @Column
    private Integer year;

    /**
     * 课程描述
     */
    @Column(columnDefinition = "TEXT")
    private String description;

    /**
     * 课程大纲（富文本内容）
     */
    @Column(columnDefinition = "TEXT")
    private String syllabus;

    /**
     * 课程资料（JSON格式存储文件列表）
     * 格式示例：[{"name":"lecture1.pdf","url":"/uploads/materials/xxx.pdf","size":1024000}]
     */
    @Column(columnDefinition = "TEXT")
    private String materials;

    /**
     * 学分
     */
    @Column
    private Double credits;
}
