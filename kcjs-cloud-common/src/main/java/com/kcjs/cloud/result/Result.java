package com.kcjs.cloud.result;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Result<T> implements Serializable {

    private Integer code;  // 状态码
    private String message; // 描述信息
    private T data;         // 返回数据

    public static <T> Result<T> success() {
        return new Result<>(200, "操作成功",null);
    }
    public static <T> Result<T> success(T data) {
        return new Result<>(200, "操作成功", data);
    }
    public static <T> Result<T> success(String message) {
        return new Result<>(200, message,null);
    }

    public static <T> Result<T> fail(String message) {
        return new Result<>(500, message, null);
    }
}