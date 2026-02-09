package com.audrey.homepage.controller;

import com.audrey.homepage.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 页面控制器
 *
 * @Controller 注解（不是@RestController）：
 * - 返回视图名称（HTML模板）
 * - 配合Thymeleaf模板引擎使用
 */
@Controller
public class HomeController {

    @Autowired
    private ProfessorService professorService;

    @Autowired
    private PublicationService publicationService;

    @Autowired
    private ResearchProjectService researchProjectService;

    @Autowired
    private TeachingCourseService teachingCourseService;

    @Autowired
    private AwardService awardService;

    @Autowired
    private ContactInfoService contactInfoService;

    /**
     * 首页
     * 访问 http://localhost:8080/
     *
     * @param model 用于向视图传递数据
     * @return 视图名称（对应 templates/index.html）
     */
    @GetMapping("/")
    public String home(Model model) {
        // 获取所有教授列表
        model.addAttribute("professors", professorService.getAllProfessors());
        model.addAttribute("professorCount", professorService.count());
        return "index";  // 返回 templates/index.html
    }

    /**
     * 教授详情页
     * 访问 http://localhost:8080/professor/1
     */
    @GetMapping("/professor/{id}")
    public String professorDetail(@PathVariable Long id, Model model) {
        return professorService.getProfessorById(id)
                .map(professor -> {
                    model.addAttribute("professor", professor);
                    // 获取联系方式信息
                    contactInfoService.getContactInfoByProfessorId(id)
                            .ifPresent(contactInfo -> model.addAttribute("contactInfo", contactInfo));
                    return "professor-detail";  // 返回 templates/professor-detail.html
                })
                .orElse("error");  // 如果没找到，返回错误页面
    }

    /**
     * 论文详情页
     * 访问 http://localhost:8080/publication/1
     */
    @GetMapping("/publication/{id}")
    public String publicationDetail(@PathVariable Long id, Model model) {
        return publicationService.getPublicationById(id)
                .map(publication -> {
                    model.addAttribute("publication", publication);
                    return "publication-detail";  // 返回 templates/publication-detail.html
                })
                .orElse("error");
    }

    /**
     * 科研项目详情页
     * 访问 http://localhost:8080/project/1
     */
    @GetMapping("/project/{id}")
    public String projectDetail(@PathVariable Long id, Model model) {
        return researchProjectService.getResearchProjectById(id)
                .map(project -> {
                    model.addAttribute("project", project);
                    return "project-detail";  // 返回 templates/project-detail.html
                })
                .orElse("error");
    }

    /**
     * 教学课程详情页
     * 访问 http://localhost:8080/course/1
     */
    @GetMapping("/course/{id}")
    public String courseDetail(@PathVariable Long id, Model model) {
        return teachingCourseService.getTeachingCourseById(id)
                .map(course -> {
                    model.addAttribute("course", course);
                    return "course-detail";  // 返回 templates/course-detail.html
                })
                .orElse("error");
    }

    /**
     * 获奖荣誉详情页
     * 访问 http://localhost:8080/award/1
     */
    @GetMapping("/award/{id}")
    public String awardDetail(@PathVariable Long id, Model model) {
        return awardService.getAwardById(id)
                .map(award -> {
                    model.addAttribute("award", award);
                    return "award-detail";  // 返回 templates/award-detail.html
                })
                .orElse("error");
    }

}
