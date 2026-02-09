package com.audrey.homepage.entity;

import jakarta.persistence.*;
import lombok.Data;

/**
 * 联系方式实体类
 * 存储教授的联系信息、办公地点、社交媒体链接等
 */
@Entity
@Table(name = "contact_info")
@Data
public class ContactInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 所属教授（一对一关系）
     */
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "professor_id", nullable = false, unique = true)
    private Professor professor;

    /**
     * 办公室地点
     */
    @Column(length = 200)
    private String officeLocation;

    /**
     * 办公室电话
     */
    @Column(length = 50)
    private String officePhone;

    /**
     * 办公时间
     */
    @Column(length = 200)
    private String officeHours;

    /**
     * 微信二维码URL
     */
    @Column(length = 500)
    private String wechatQrcode;

    /**
     * Google Scholar链接
     */
    @Column(length = 500)
    private String googleScholarUrl;

    /**
     * ResearchGate链接
     */
    @Column(length = 500)
    private String researchgateUrl;

    /**
     * LinkedIn链接
     */
    @Column(length = 500)
    private String linkedinUrl;

    /**
     * ORCID链接
     */
    @Column(length = 500)
    private String orcidUrl;

    /**
     * 地图详细地址（从地图API获取）
     */
    @Column(length = 500)
    private String mapAddress;

    /**
     * 纬度（用于地图定位）
     */
    @Column
    private Double latitude;

    /**
     * 经度（用于地图定位）
     */
    @Column
    private Double longitude;

    /**
     * 地图缩放级别
     */
    @Column
    private Integer mapZoom = 15;

    /**
     * 其他联系方式（JSON格式存储）
     */
    @Column(columnDefinition = "TEXT")
    private String otherContacts;
}
