package com.kcjs.cloud.exception;

import com.kcjs.cloud.exception.BusinessException;
import com.kcjs.cloud.result.Result;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public Result<?> handleException(Exception ex) {
        ex.printStackTrace();

        String extractedMsg = ExceptionMessageExtractor.extractBusinessMessage(ex.getMessage());

        Throwable cause = ex.getCause();
        while (extractedMsg == null && cause != null) {
            extractedMsg = ExceptionMessageExtractor.extractBusinessMessage(cause.getMessage());
            cause = cause.getCause();
        }

        if (extractedMsg != null) {
            return Result.fail(extractedMsg);
        }

        return Result.fail("系统繁忙，请稍后再试！");
    }

}