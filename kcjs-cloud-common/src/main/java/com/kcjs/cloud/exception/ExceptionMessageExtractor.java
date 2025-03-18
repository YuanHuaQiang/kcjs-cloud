package com.kcjs.cloud.exception;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ExceptionMessageExtractor {

    public static String extractBusinessMessage(String message) {
        if (message == null) {
            return null;
        }

        // 匹配 com.kcjs.cloud.exception.BusinessException: 后的内容
        Pattern pattern = Pattern.compile("BusinessException:\\s*(.*)");
        Matcher matcher = pattern.matcher(message);

        if (matcher.find()) {
            return matcher.group(1); // 提取到的内容
        }

        return null; // 没有匹配到
    }
}