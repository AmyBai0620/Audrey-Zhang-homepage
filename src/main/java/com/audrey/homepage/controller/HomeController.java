package com.audrey.homepage.controller;

import com.audrey.homepage.service.ProfessorService;
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
                    return "professor-detail";  // 返回 templates/professor-detail.html
                })
                .orElse("error");  // 如果没找到，返回错误页面
    }
}
