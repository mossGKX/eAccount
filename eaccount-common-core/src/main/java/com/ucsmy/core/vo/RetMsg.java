package com.ucsmy.core.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

/**
 * Created by 钟廷员 on 2016/9/18.
 */
@Data
@Builder
public class RetMsg<T> {
    private int code;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String msg;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T data;

    public boolean isSuccess() {
        return this.code == Type.SUCCESS.value;
    }

    public boolean isError() {
        return !isSuccess();
    }

    public static RetMsg success() {
        return RetMsg.ret(Type.SUCCESS);
    }

    public static RetMsg success(String msg) {
        return RetMsg.ret(Type.SUCCESS, msg);
    }

    public static <T> RetMsg success(T data) {
        return RetMsg.ret(Type.SUCCESS, data);
    }

    public static <T> RetMsg success(String msg, T data) {
        return RetMsg.ret(Type.SUCCESS, msg, data);
    }

    public static RetMsg error() {
        return RetMsg.ret(Type.ERROR);
    }

    public static RetMsg error(String msg) {
        return RetMsg.ret(Type.ERROR, msg);
    }

    public static <T> RetMsg error(T data) {
        return RetMsg.ret(Type.ERROR, data);
    }

    public static <T> RetMsg error(String msg, T data) {
        return RetMsg.ret(Type.ERROR, msg, data);
    }

    public static RetMsg ret(Type type, String msg) {
        return RetMsg.builder()
                .code(type.value)
                .msg(msg)
                .build();
    }

    public static RetMsg ret(Type type) {
        return RetMsg.builder().code(type.value).build();
    }

    public static <T> RetMsg ret(Type type, T data) {
        return RetMsg.builder()
                .code(type.value)
                .data(data)
                .build();
    }

    public static <T> RetMsg ret(Type type, String msg, T data) {
        return RetMsg.builder()
                .code(type.value)
                .msg(msg)
                .data(data)
                .build();
    }

    public static enum Type {
        SUCCESS(0),
        ERROR(-1),
        ONE(1),
        TWO(2),
        THREE(3),
        FOUR(4),
        FIVE(5);

        int value;

        Type(int value) {
            this.value = value;
        }
    }
}
