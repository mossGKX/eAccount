package com.ucsmy.core.tool.log.annotation;

import com.ucsmy.core.tool.log.log4j2.LogOuputTarget;

import java.lang.annotation.*;

/**
 * 在service层的方法上使用，输出Info级别日志，可选输出内容： <br>
 * 1. 操作名称，默认空；<br>
 * 2. 操作类型，默认空；<br>
 * 3. 是否打印操作的入参类型和值，默认true；<br>
 * 4. 是否打印操作的输出类型和值，默认true；<br>
 * 5. 是否打印该方法执行的所有SQL，默认false；<br>
 * 使用方法见ManageConfigServiceImpl<br>
 * Created by ucs_gaokx on 2017/4/10.
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface Logger {
    /**
     * 要执行的操作类型比如：add操作
     **/
    String operationType() default "";

    /**
     * 要执行的具体操作比如：添加用户日志
     **/
    String operationName() default "";

    /**
     * 是否打印操作的入参类型和值
     *
     * @return
     */
    boolean printInput() default true;

    /**
     * 是否打印操作的输出类型和值
     *
     * @return
     */
    boolean printOutput() default true;

    /**
     * 是否打印执行SQL
     * @return
     */
    boolean printSQL() default false;

    /**
     * log输出目标
     * TODO
     * @return
     */
    LogOuputTarget outputTarget() default LogOuputTarget.FILE;

}
