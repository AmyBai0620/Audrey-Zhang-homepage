package com.audrey.homepage.controller;

import com.audrey.homepage.entity.ContactInfo;
import com.audrey.homepage.entity.Professor;
import com.audrey.homepage.entity.Publication;
import com.audrey.homepage.entity.TeachingCourse;
import com.audrey.homepage.service.ContactInfoService;
import com.audrey.homepage.service.ProfessorService;
import com.audrey.homepage.service.PublicationService;
import com.audrey.homepage.service.TeachingCourseService;
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
 * 4. 处理微信二维码上传和删除
 * 5. 处理课程资料上传和删除
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

    @Autowired
    private ContactInfoService contactInfoService;

    @Autowired
    private TeachingCourseService teachingCourseService;

    /**
     * 上传目录配置
     * 注意：这个路径是相对于项目根目录的
     */
    private static final String AVATAR_UPLOAD_DIR = "uploads/avatars";
    private static final String PDF_UPLOAD_DIR = "uploads/pdfs";
    private static final String QRCODE_UPLOAD_DIR = "uploads/qrcodes";
    private static final String MATERIALS_UPLOAD_DIR = "uploads/materials";

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

    /**
     * 上传微信二维码
     *
     * 请求方式：POST
     * 请求URL：/api/upload/qrcode/{contactInfoId}
     * 请求参数：file（MultipartFile类型）
     *
     * @param contactInfoId 联系信息ID
     * @param file 上传的二维码图片
     * @return JSON响应（成功或失败信息）
     */
    @PostMapping("/qrcode/{contactInfoId}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> uploadQrcode(
            @PathVariable Long contactInfoId,
            @RequestParam("file") MultipartFile file) {

        Map<String, Object> response = new HashMap<>();

        try {
            // 1. 查找联系信息
            ContactInfo contactInfo = contactInfoService.getContactInfoById(contactInfoId)
                    .orElseThrow(() -> new RuntimeException("找不到ID为 " + contactInfoId + " 的联系信息"));

            // 2. 删除旧二维码（如果存在）
            if (contactInfo.getWechatQrcode() != null && !contactInfo.getWechatQrcode().isEmpty()) {
                FileUploadUtil.deleteFile(contactInfo.getWechatQrcode());
            }

            // 3. 保存新二维码
            String qrcodeUrl = FileUploadUtil.saveFile(
                    file,
                    QRCODE_UPLOAD_DIR,
                    "qrcode_" + contactInfoId  // 使用联系信息ID作为文件名前缀
            );

            // 4. 更新数据库
            contactInfo.setWechatQrcode(qrcodeUrl);
            contactInfoService.saveContactInfo(contactInfo);

            // 5. 返回成功响应
            response.put("success", true);
            response.put("message", "二维码上传成功");
            response.put("qrcodeUrl", qrcodeUrl);

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
     * 删除微信二维码
     *
     * 请求方式：DELETE
     * 请求URL：/api/upload/qrcode/{contactInfoId}
     *
     * @param contactInfoId 联系信息ID
     * @return JSON响应
     */
    @DeleteMapping("/qrcode/{contactInfoId}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> deleteQrcode(@PathVariable Long contactInfoId) {
        Map<String, Object> response = new HashMap<>();

        try {
            // 1. 查找联系信息
            ContactInfo contactInfo = contactInfoService.getContactInfoById(contactInfoId)
                    .orElseThrow(() -> new RuntimeException("找不到ID为 " + contactInfoId + " 的联系信息"));

            // 2. 删除文件
            if (contactInfo.getWechatQrcode() != null && !contactInfo.getWechatQrcode().isEmpty()) {
                FileUploadUtil.deleteFile(contactInfo.getWechatQrcode());
            }

            // 3. 更新数据库
            contactInfo.setWechatQrcode(null);
            contactInfoService.saveContactInfo(contactInfo);

            // 4. 返回成功响应
            response.put("success", true);
            response.put("message", "二维码删除成功");

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            e.printStackTrace();
            response.put("success", false);
            response.put("message", "删除失败：" + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    /**
     * 上传课程资料
     *
     * 请求方式：POST
     * 请求URL：/api/upload/material/{courseId}
     * 请求参数：file（MultipartFile类型）
     *
     * @param courseId 课程ID
     * @param file 上传的文件（PDF、PPT、Word等）
     * @return JSON响应（成功或失败信息）
     */
    @PostMapping("/material/{courseId}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> uploadMaterial(
            @PathVariable Long courseId,
            @RequestParam("file") MultipartFile file) {

        Map<String, Object> response = new HashMap<>();

        try {
            // 1. 验证文件类型
            String contentType = file.getContentType();
            if (contentType == null || (!contentType.equals("application/pdf")
                    && !contentType.equals("application/vnd.ms-powerpoint")
                    && !contentType.equals("application/vnd.openxmlformats-officedocument.presentationml.presentation")
                    && !contentType.equals("application/msword")
                    && !contentType.equals("application/vnd.openxmlformats-officedocument.wordprocessingml.document"))) {
                throw new IllegalArgumentException("只允许上传PDF、PPT、Word格式的文件");
            }

            // 2. 验证文件大小（20MB）
            final long MAX_SIZE = 20 * 1024 * 1024;
            if (file.getSize() > MAX_SIZE) {
                throw new IllegalArgumentException("文件大小不能超过20MB");
            }

            // 3. 查找课程
            TeachingCourse course = teachingCourseService.getTeachingCourseById(courseId)
                    .orElseThrow(() -> new RuntimeException("找不到ID为 " + courseId + " 的课程"));

            // 4. 保存文件
            String fileUrl = FileUploadUtil.saveMaterialFile(
                    file,
                    MATERIALS_UPLOAD_DIR,
                    "course_" + courseId  // 使用课程ID作为文件名前缀
            );

            // 5. 返回成功响应（不更新数据库，由前端JavaScript处理）
            response.put("success", true);
            response.put("message", "资料上传成功");
            response.put("fileUrl", fileUrl);
            response.put("fileName", file.getOriginalFilename());
            response.put("fileSize", file.getSize());

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
     * 删除课程资料
     *
     * 请求方式：DELETE
     * 请求URL：/api/upload/material/{courseId}
     * 请求体：JSON {"fileUrl": "/uploads/materials/xxx.pdf"}
     *
     * @param courseId 课程ID
     * @param requestBody 包含fileUrl的JSON对象
     * @return JSON响应
     */
    @DeleteMapping("/material/{courseId}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> deleteMaterial(
            @PathVariable Long courseId,
            @RequestBody Map<String, String> requestBody) {

        Map<String, Object> response = new HashMap<>();

        try {
            // 1. 获取要删除的文件URL
            String fileUrl = requestBody.get("fileUrl");
            if (fileUrl == null || fileUrl.isEmpty()) {
                throw new IllegalArgumentException("文件URL不能为空");
            }

            // 2. 查找课程（验证课程存在）
            TeachingCourse course = teachingCourseService.getTeachingCourseById(courseId)
                    .orElseThrow(() -> new RuntimeException("找不到ID为 " + courseId + " 的课程"));

            // 3. 删除文件
            FileUploadUtil.deleteFile(fileUrl);

            // 4. 返回成功响应
            response.put("success", true);
            response.put("message", "资料删除成功");

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            e.printStackTrace();
            response.put("success", false);
            response.put("message", "删除失败：" + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }
}
