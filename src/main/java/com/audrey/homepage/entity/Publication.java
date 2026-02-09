package com.audrey.homepage.entity;

import jakarta.persistence.*;
import lombok.Data;

/**
 * 学术论文/出版物实体类
 * 对应数据库中的publication表
 */
@Entity
@Table(name = "publication")
@Data
public class Publication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 所属教授ID（外键）
     * 多对一关系：多篇论文属于一个教授
     */
    @ManyToOne(fetch = FetchType.LAZY)  // 懒加载，提高性能
    @JoinColumn(name = "professor_id", nullable = false)  // 外键列名
    private Professor professor;

    /**
     * 论文标题
     */
    @Column(nullable = false, length = 500)
    private String title;

    /**
     * 作者列表（用逗号分隔）
     */
    @Column(length = 500)
    private String authors;

    /**
     * 期刊或会议名称
     */
    @Column(length = 200)
    private String journal;

    /**
     * 发表年份
     */
    @Column
    private Integer year;

    /**
     * 卷号
     */
    @Column(length = 50)
    private String volume;

    /**
     * 页码
     */
    @Column(length = 50)
    private String pages;

    /**
     * DOI号（数字对象唯一标识符）
     */
    @Column(length = 100)
    private String doi;

    /**
     * 论文链接
     */
    @Column(length = 500)
    private String url;

    /**
     * PDF文件URL
     */
    @Column(length = 500)
    private String pdfUrl;

    /**
     * 出版物类型：JOURNAL（期刊）、CONFERENCE（会议）、BOOK（书籍）
     */
    @Enumerated(EnumType.STRING)  // 以字符串形式存储枚举
    @Column(length = 20)
    private PublicationType publicationType;

    /**
     * 出版物类型枚举
     */
    public enum PublicationType {
        JOURNAL,      // 期刊论文
        CONFERENCE,   // 会议论文
        BOOK,         // 书籍
        BOOK_CHAPTER  // 书籍章节
    }
}
