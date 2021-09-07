package com.wpy.annotation;

import java.lang.annotation.*;

/**
 * @author 13940
 */
@Target({ElementType.PARAMETER,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SysLogs {

    /**
     * 操作行为
     */
    String value() default "";
}
