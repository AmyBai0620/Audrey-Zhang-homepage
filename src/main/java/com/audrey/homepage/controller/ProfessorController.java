package com.audrey.homepage.controller;

import com.audrey.homepage.entity.Professor;
import com.audrey.homepage.service.ProfessorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Professor控制器
 *
 * @RestController 注解：
 * - 组合了 @Controller 和 @ResponseBody
 * - 所有方法返回的数据会自动转换为JSON格式
 * - 适合构建RESTful API
 *
 * @RequestMapping("/api/professors")：
 * - 设置基础路径
 * - 所有方法的URL都会以 /api/professors 开头
 */
@RestController
@RequestMapping("/api/professors")
@CrossOrigin(origins = "*")  // 允许跨域请求（前端开发时需要）
public class ProfessorController {

    @Autowired
    private ProfessorService professorService;

    /**
     * 获取所有教授
     * GET /api/professors
     *
     * @GetMapping 等同于 @RequestMapping(method = RequestMethod.GET)
     */
    @GetMapping
    public ResponseEntity<List<Professor>> getAllProfessors() {
        List<Professor> professors = professorService.getAllProfessors();
        return ResponseEntity.ok(professors);
    }

    /**
     * 根据ID获取教授
     * GET /api/professors/1
     *
     * @PathVariable 从URL路径中获取参数
     * 例如：/api/professors/1 中的 1 会被映射到 id 参数
     */
    @GetMapping("/{id}")
    public ResponseEntity<Professor> getProfessorById(@PathVariable Long id) {
        return professorService.getProfessorById(id)
                .map(ResponseEntity::ok)  // 如果找到，返回200 OK
                .orElse(ResponseEntity.notFound().build());  // 如果没找到，返回404 Not Found
    }

    /**
     * 根据邮箱获取教授
     * GET /api/professors/email/test@example.com
     */
    @GetMapping("/email/{email}")
    public ResponseEntity<Professor> getProfessorByEmail(@PathVariable String email) {
        return professorService.getProfessorByEmail(email)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * 创建新教授
     * POST /api/professors
     *
     * @RequestBody 从HTTP请求体中获取JSON数据，自动转换为Professor对象
     *
     * 请求示例：
     * POST /api/professors
     * Content-Type: application/json
     * {
     *   "name": "张三",
     *   "title": "副教授",
     *   "university": "贵州财经大学",
     *   "email": "zhangsan@gufe.edu.cn"
     * }
     */
    @PostMapping
    public ResponseEntity<Professor> createProfessor(@RequestBody Professor professor) {
        Professor savedProfessor = professorService.saveProfessor(professor);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedProfessor);  // 返回201 Created
    }

    /**
     * 更新教授信息
     * PUT /api/professors/1
     */
    @PutMapping("/{id}")
    public ResponseEntity<Professor> updateProfessor(
            @PathVariable Long id,
            @RequestBody Professor professor) {

        if (!professorService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        professor.setId(id);  // 确保ID正确
        Professor updatedProfessor = professorService.saveProfessor(professor);
        return ResponseEntity.ok(updatedProfessor);
    }

    /**
     * 删除教授
     * DELETE /api/professors/1
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProfessor(@PathVariable Long id) {
        if (!professorService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        professorService.deleteProfessor(id);
        return ResponseEntity.noContent().build();  // 返回204 No Content
    }

    /**
     * 统计教授数量
     * GET /api/professors/count
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countProfessors() {
        long count = professorService.count();
        return ResponseEntity.ok(count);
    }
}
