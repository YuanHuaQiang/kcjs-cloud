package com.kcjs.cloud.exception;

import com.kcjs.cloud.exception.BusinessException;
import com.kcjs.cloud.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Objects;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public Result<?> handleException(Exception ex) {
        log.error("系统异常", ex);

        String extractedMsg = ExceptionMessageExtractor.extractBusinessMessage(ex.getMessage());

        Throwable cause = ex.getCause();
        while (extractedMsg == null && cause != null) {
            extractedMsg = ExceptionMessageExtractor.extractBusinessMessage(cause.getMessage());
            cause = cause.getCause();
        }

        return Result.fail(Objects.requireNonNullElse(extractedMsg, "系统繁忙，请稍后再试！"));
    }

}