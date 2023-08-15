package org.james.gos.vaccines.common.annotation;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface TokenParam {

    @AliasFor("name")
    String value() default "id";

    @AliasFor("value")
    String name() default "id";
}