package com.ucsmy.core.tool.serialNumber.exception;

/**
 * 流水号配置异常类
 *
 * @author ucs_gaokx
 * @since 2017/9/14
 */

public class SerialNumberConfigException extends RuntimeException {

    private static final long serialVersionUID = 1032282297812672284L;

    public SerialNumberConfigException() {
    }

    public SerialNumberConfigException(String message) {
        super(message);
    }

    public SerialNumberConfigException(String message, Throwable cause) {
        super(message, cause);
    }

    public SerialNumberConfigException(Throwable cause) {
        super(cause);
    }
}