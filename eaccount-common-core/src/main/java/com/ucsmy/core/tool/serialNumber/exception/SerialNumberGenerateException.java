package com.ucsmy.core.tool.serialNumber.exception;

/**
 * 流水号生成异常类
 *
 * @author ucs_gaokx
 * @since 2017/9/14
 */

public class SerialNumberGenerateException extends RuntimeException {

    private static final long serialVersionUID = 8343152887020806391L;

    public SerialNumberGenerateException() {
    }

    public SerialNumberGenerateException(String message) {
        super(message);
    }

    public SerialNumberGenerateException(String message, Throwable cause) {
        super(message, cause);
    }

    public SerialNumberGenerateException(Throwable cause) {
        super(cause);
    }
}