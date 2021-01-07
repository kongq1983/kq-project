package com.kq.common.extension;

import java.lang.annotation.*;

/**
 * @author kq
 * @date 2021-01-06 17:08
 * @since 2020-0630
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface SPI {

    /**
     * default extension name
     */
    String value() default "";

}