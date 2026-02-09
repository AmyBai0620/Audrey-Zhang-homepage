package com.audrey.homepage.util;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * 文件上传工具类
 *
 * 功能：
 * 1. 保存上传的文件到指定目录
 * 2. 生成唯一的文件名（避免冲突）
 * 3. 验证文件类型（只允许图片）
 * 4. 删除文件
 *
 * @author Claude
 * @date 2026-01-20
 */
public class FileUploadUtil {

    /**
     * 允许上传的图片类型（MIME类型）
     * MIME类型是文件的真实类型，不是扩展名
     */
    private static final List<String> ALLOWED_IMAGE_TYPES = Arrays.asList(
            "image/jpeg",    // .jpg, .jpeg
            "image/png",     // .png
            "image/gif",     // .gif
            "image/webp"     // .webp (现代图片格式)
    );

    /**
     * 允许上传的PDF类型
     */
    private static final String ALLOWED_PDF_TYPE = "application/pdf";

    /**
     * 最大文件大小（5MB = 5 * 1024 * 1024 字节）- 用于图片
     */
    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024;

    /**
     * 最大PDF文件大小（20MB）
     */
    private static final long MAX_PDF_SIZE = 20 * 1024 * 1024;

    /**
     * 保存上传的文件（用于图片）
     *
     * @param file 上传的文件
     * @param uploadDir 上传目录（如：uploads/avatars）
     * @param prefix 文件名前缀（如：教授ID）
     * @return 保存后的文件相对路径（如：/uploads/avatars/1_uuid.jpg）
     * @throws IOException 文件操作异常
     * @throws IllegalArgumentException 文件验证失败
     */
    public static String saveFile(MultipartFile file, String uploadDir, String prefix) throws IOException {
        // 1. 验证文件（图片）
        validateFile(file);

        // 2. 保存文件
        return saveFileInternal(file, uploadDir, prefix);
    }

    /**
     * 保存上传的PDF文件
     *
     * @param file 上传的文件
     * @param uploadDir 上传目录（如：uploads/pdfs）
     * @param prefix 文件名前缀（如：publication_1）
     * @return 保存后的文件相对路径
     * @throws IOException 文件操作异常
     * @throws IllegalArgumentException 文件验证失败
     */
    public static String savePdfFile(MultipartFile file, String uploadDir, String prefix) throws IOException {
        // 1. 验证PDF文件
        validatePdfFile(file);

        // 2. 保存文件
        return saveFileInternal(file, uploadDir, prefix);
    }

    /**
     * 内部方法：保存文件的通用逻辑
     */
    private static String saveFileInternal(MultipartFile file, String uploadDir, String prefix) throws IOException {
        // 1. 创建上传目录（如果不存在）
        File directory = new File(uploadDir);
        if (!directory.exists()) {
            directory.mkdirs();  // 创建多级目录
        }

        // 2. 生成唯一文件名
        String originalFilename = file.getOriginalFilename();
        String extension = getFileExtension(originalFilename);
        String newFilename = generateUniqueFilename(prefix, extension);

        // 3. 保存文件
        Path filePath = Paths.get(uploadDir, newFilename);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        // 4. 返回相对路径（用于存储到数据库和前台访问）
        return "/" + uploadDir + "/" + newFilename;
    }

    /**
     * 验证上传的文件（图片）
     *
     * @param file 上传的文件
     * @throws IllegalArgumentException 验证失败时抛出异常
     */
    private static void validateFile(MultipartFile file) {
        // 检查文件是否为空
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("文件不能为空");
        }

        // 检查文件大小
        if (file.getSize() > MAX_FILE_SIZE) {
            throw new IllegalArgumentException("文件大小不能超过5MB");
        }

        // 检查文件类型（MIME类型）
        String contentType = file.getContentType();
        if (contentType == null || !ALLOWED_IMAGE_TYPES.contains(contentType)) {
            throw new IllegalArgumentException("只允许上传图片文件（JPG、PNG、GIF、WebP）");
        }
    }

    /**
     * 验证上传的PDF文件
     *
     * @param file 上传的文件
     * @throws IllegalArgumentException 验证失败时抛出异常
     */
    private static void validatePdfFile(MultipartFile file) {
        // 检查文件是否为空
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("文件不能为空");
        }

        // 检查文件大小
        if (file.getSize() > MAX_PDF_SIZE) {
            throw new IllegalArgumentException("PDF文件大小不能超过20MB");
        }

        // 检查文件类型
        String contentType = file.getContentType();
        if (contentType == null || !contentType.equals(ALLOWED_PDF_TYPE)) {
            throw new IllegalArgumentException("只允许上传PDF格式的文件");
        }
    }

    /**
     * 生成唯一的文件名
     * 格式：prefix_uuid.extension
     * 示例：1_a1b2c3d4.jpg
     *
     * @param prefix 前缀（如：教授ID）
     * @param extension 文件扩展名（如：.jpg）
     * @return 唯一文件名
     */
    private static String generateUniqueFilename(String prefix, String extension) {
        // UUID：通用唯一识别码（Universally Unique Identifier）
        // 生成一个几乎不可能重复的随机字符串
        String uuid = UUID.randomUUID().toString().replace("-", "");

        // 也可以使用时间戳
        // long timestamp = System.currentTimeMillis();
        // return prefix + "_" + timestamp + extension;

        return prefix + "_" + uuid + extension;
    }

    /**
     * 获取文件扩展名
     *
     * @param filename 文件名（如：photo.jpg）
     * @return 扩展名（如：.jpg）
     */
    private static String getFileExtension(String filename) {
        if (filename == null || filename.isEmpty()) {
            return "";
        }

        int lastDotIndex = filename.lastIndexOf(".");
        if (lastDotIndex == -1) {
            return "";  // 没有扩展名
        }

        return filename.substring(lastDotIndex);  // 包括点号
    }

    /**
     * 删除文件
     *
     * @param filePath 文件路径（如：/uploads/avatars/1_uuid.jpg）
     * @return 是否删除成功
     */
    public static boolean deleteFile(String filePath) {
        if (filePath == null || filePath.isEmpty()) {
            return false;
        }

        try {
            // 移除开头的斜杠
            if (filePath.startsWith("/")) {
                filePath = filePath.substring(1);
            }

            File file = new File(filePath);
            if (file.exists()) {
                return file.delete();
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 获取文件大小的可读格式
     *
     * @param size 文件大小（字节）
     * @return 可读格式（如：1.5 MB）
     */
    public static String getReadableFileSize(long size) {
        if (size < 1024) {
            return size + " B";
        } else if (size < 1024 * 1024) {
            return String.format("%.2f KB", size / 1024.0);
        } else {
            return String.format("%.2f MB", size / (1024.0 * 1024.0));
        }
    }
}
