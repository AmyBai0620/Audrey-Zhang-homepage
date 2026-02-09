package com.audrey.homepage.controller;

import com.audrey.homepage.entity.Professor;
import com.audrey.homepage.entity.Publication;
import com.audrey.homepage.service.ProfessorService;
import com.audrey.homepage.service.PublicationService;
import com.audrey.homepage.util.FileUploadUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

/**
 * 文件上传控制器
 *
 * 功能：
 * 1. 处理教授头像上传
 * 2. 处理教授头像删除
 * 3. 处理论文PDF上传和删除
 *
 * @author Claude
 * @date 2026-01-20
 */
@Controller
@RequestMapping("/api/upload")
public class FileUploadController {

    @Autowired
    private ProfessorService professorService;

    @Autowired
    private PublicationService publicationService;

    /**
     * 上传目录配置
     * 注意：这个路径是相对于项目根目录的
     */
    private static final String AVATAR_UPLOAD_DIR = "uploads/avatars";
    private static final String PDF_UPLOAD_DIR = "uploads/pdfs";

    /**
     * 上传教授头像
     *
     * 请求方式：POST
     * 请求URL：/api/upload/avatar/{professorId}
     * 请求参数：file（MultipartFile类型）
     *
     * @param professorId 教授ID
     * @param file 上传的文件
     * @return JSON响应（成功或失败信息）
     */
    @PostMapping("/avatar/{professorId}")
    @ResponseBody  // 返回JSON，不是HTML页面
    public ResponseEntity<Map<String, Object>> uploadAvatar(
            @PathVariable Long professorId,
            @RequestParam("file") MultipartFile file) {

        Map<String, Object> response = new HashMap<>();

        try {
            // 1. 查找教授
            Professor professor = professorService.getProfessorById(professorId)
                    .orElseThrow(() -> new RuntimeException("找不到ID为 " + professorId + " 的教授"));

            // 2. 删除旧头像（如果存在）
            if (professor.getAvatarUrl() != null && !professor.getAvatarUrl().isEmpty()) {
                FileUploadUtil.deleteFile(professor.getAvatarUrl());
            }

            // 3. 保存新头像
            String avatarUrl = FileUploadUtil.saveFile(
                    file,
                    AVATAR_UPLOAD_DIR,
                    String.valueOf(professorId)  // 使用教授ID作为文件名前缀
            );

            // 4. 更新数据库
            professor.setAvatarUrl(avatarUrl);
            professorService.saveProfessor(professor);

            // 5. 返回成功响应
            response.put("success", true);
            response.put("message", "头像上传成功");
            response.put("avatarUrl", avatarUrl);

            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            // 文件验证失败（文件类型、大小等）
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);

        } catch (Exception e) {
            // 其他异常（文件保存失败、数据库错误等）
            e.printStackTrace();
            response.put("success", false);
            response.put("message", "上传失败：" + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    /**
     * 删除教授头像
     *
     * 请求方式：DELETE
     * 请求URL：/api/upload/avatar/{professorId}
     *
     * @param professorId 教授ID
     * @return JSON响应
     */
    @DeleteMapping("/avatar/{professorId}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> deleteAvatar(@PathVariable Long professorId) {
        Map<String, Object> response = new HashMap<>();

        try {
            // 1. 查找教授
            Professor professor = professorService.getProfessorById(professorId)
                    .orElseThrow(() -> new RuntimeException("找不到ID为 " + professorId + " 的教授"));

            // 2. 删除文件
            if (professor.getAvatarUrl() != null && !professor.getAvatarUrl().isEmpty()) {
                FileUploadUtil.deleteFile(professor.getAvatarUrl());
            }

            // 3. 更新数据库
            professor.setAvatarUrl(null);
            professorService.saveProfessor(professor);

            // 4. 返回成功响应
            response.put("success", true);
            response.put("message", "头像删除成功");

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            e.printStackTrace();
            response.put("success", false);
            response.put("message", "删除失败：" + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    /**
     * 上传论文PDF
     *
     * 请求方式：POST
     * 请求URL：/api/upload/pdf/{publicationId}
     * 请求参数：file（MultipartFile类型）
     *
     * @param publicationId 论文ID
     * @param file 上传的PDF文件
     * @return JSON响应（成功或失败信息）
     */
    @PostMapping("/pdf/{publicationId}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> uploadPdf(
            @PathVariable Long publicationId,
            @RequestParam("file") MultipartFile file) {

        Map<String, Object> response = new HashMap<>();

        try {
            // 1. 验证文件类型
            if (!file.getContentType().equals("application/pdf")) {
                throw new IllegalArgumentException("只允许上传PDF格式的文件");
            }

            // 2. 查找论文
            Publication publication = publicationService.getPublicationById(publicationId)
                    .orElseThrow(() -> new RuntimeException("找不到ID为 " + publicationId + " 的论文"));

            // 3. 删除旧PDF（如果存在）
            if (publication.getPdfUrl() != null && !publication.getPdfUrl().isEmpty()) {
                FileUploadUtil.deleteFile(publication.getPdfUrl());
            }

            // 4. 保存新PDF
            String pdfUrl = FileUploadUtil.savePdfFile(
                    file,
                    PDF_UPLOAD_DIR,
                    "publication_" + publicationId  // 使用论文ID作为文件名前缀
            );

            // 5. 更新数据库
            publication.setPdfUrl(pdfUrl);
            publicationService.savePublication(publication);

            // 6. 返回成功响应
            response.put("success", true);
            response.put("message", "PDF上传成功");
            response.put("pdfUrl", pdfUrl);

            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            // 文件验证失败
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);

        } catch (Exception e) {
            // 其他异常
            e.printStackTrace();
            response.put("success", false);
            response.put("message", "上传失败：" + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    /**
     * 删除论文PDF
     *
     * 请求方式：DELETE
     * 请求URL：/api/upload/pdf/{publicationId}
     *
     * @param publicationId 论文ID
     * @return JSON响应
     */
    @DeleteMapping("/pdf/{publicationId}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> deletePdf(@PathVariable Long publicationId) {
        Map<String, Object> response = new HashMap<>();

        try {
            // 1. 查找论文
            Publication publication = publicationService.getPublicationById(publicationId)
                    .orElseThrow(() -> new RuntimeException("找不到ID为 " + publicationId + " 的论文"));

            // 2. 删除文件
            if (publication.getPdfUrl() != null && !publication.getPdfUrl().isEmpty()) {
                FileUploadUtil.deleteFile(publication.getPdfUrl());
            }

            // 3. 更新数据库
            publication.setPdfUrl(null);
            publicationService.savePublication(publication);

            // 4. 返回成功响应
            response.put("success", true);
            response.put("message", "PDF删除成功");

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            e.printStackTrace();
            response.put("success", false);
            response.put("message", "删除失败：" + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }
}
