package com.audrey.homepage.util;

import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 搜索高亮工具类
 * 用于在搜索结果中高亮显示关键词
 *
 * @author Claude
 * @date 2026-02-09
 */
@Component("searchHighlight")
public class SearchHighlightUtil {

    /**
     * 高亮显示文本中的关键词
     *
     * @param text 原始文本
     * @param keyword 关键词
     * @return 高亮后的HTML文本
     */
    public String highlight(String text, String keyword) {
        // 如果文本或关键词为空，直接返回原文本
        if (text == null || text.isEmpty() || keyword == null || keyword.trim().isEmpty()) {
            return text != null ? text : "";
        }

        // 转义HTML特殊字符
        text = escapeHtml(text);

        try {
            // 使用正则表达式进行不区分大小写的匹配和替换
            // Pattern.CASE_INSENSITIVE 表示不区分大小写
            // Pattern.LITERAL 表示将关键词作为字面量，不解析正则表达式特殊字符
            String escapedKeyword = Pattern.quote(keyword.trim());
            Pattern pattern = Pattern.compile(escapedKeyword, Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(text);

            // 替换匹配的关键词为高亮HTML
            StringBuffer result = new StringBuffer();
            while (matcher.find()) {
                matcher.appendReplacement(result,
                        "<span class=\"highlight\">" + matcher.group() + "</span>");
            }
            matcher.appendTail(result);

            return result.toString();
        } catch (Exception e) {
            // 如果出现异常，返回原文本
            return text;
        }
    }

    /**
     * 转义HTML特殊字符
     *
     * @param text 原始文本
     * @return 转义后的文本
     */
    private String escapeHtml(String text) {
        if (text == null) {
            return "";
        }
        return text.replace("&", "&amp;")
                   .replace("<", "&lt;")
                   .replace(">", "&gt;")
                   .replace("\"", "&quot;")
                   .replace("'", "&#39;");
    }
}
