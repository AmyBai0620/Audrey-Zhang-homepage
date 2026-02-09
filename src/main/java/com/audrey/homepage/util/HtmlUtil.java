package com.audrey.homepage.util;

import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

/**
 * HTML内容处理工具类
 * 用于在后台管理列表页面友好地显示富文本内容
 *
 * @author Claude
 * @date 2026-02-09
 */
@Component("htmlUtil")
public class HtmlUtil {

    /**
     * 移除HTML标签，只保留纯文本
     *
     * @param html HTML内容
     * @return 纯文本内容
     */
    public static String stripHtmlTags(String html) {
        if (html == null || html.isEmpty()) {
            return "";
        }

        // 移除所有HTML标签
        String text = html.replaceAll("<[^>]*>", "");

        // 替换HTML实体
        text = text.replace("&nbsp;", " ");
        text = text.replace("&lt;", "<");
        text = text.replace("&gt;", ">");
        text = text.replace("&amp;", "&");
        text = text.replace("&quot;", "\"");
        text = text.replace("&#39;", "'");

        // 移除多余的空格和换行
        text = text.replaceAll("\\s+", " ").trim();

        return text;
    }

    /**
     * 截断文本到指定长度，并添加省略号
     *
     * @param text 原始文本
     * @param maxLength 最大长度
     * @return 截断后的文本
     */
    public static String truncate(String text, int maxLength) {
        if (text == null) {
            return "";
        }

        if (text.length() <= maxLength) {
            return text;
        }

        return text.substring(0, maxLength) + "...";
    }

    /**
     * 获取HTML内容的纯文本摘要（移除标签并截断）
     *
     * @param html HTML内容
     * @param maxLength 最大长度
     * @return 纯文本摘要
     */
    public static String getTextSummary(String html, int maxLength) {
        String text = stripHtmlTags(html);
        return truncate(text, maxLength);
    }

    /**
     * Thymeleaf模板中使用的方法（带默认长度）
     */
    public String getSummary(String html) {
        return getTextSummary(html, 100);
    }

    /**
     * Thymeleaf模板中使用的方法（自定义长度）
     */
    public String getSummary(String html, int maxLength) {
        return getTextSummary(html, maxLength);
    }

    /**
     * 检查HTML内容是否为空
     */
    public boolean isEmpty(String html) {
        if (html == null || html.trim().isEmpty()) {
            return true;
        }
        String text = stripHtmlTags(html);
        return text.trim().isEmpty();
    }
}
