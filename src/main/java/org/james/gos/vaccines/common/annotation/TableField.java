package org.james.gos.vaccines.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**自动填充 标识*/
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface TableField {

    String value();

    FieldFill fill();
}
