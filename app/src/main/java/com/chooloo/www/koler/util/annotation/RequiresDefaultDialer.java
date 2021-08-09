package com.chooloo.www.koler.util.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.CLASS;

@Retention(CLASS)
@Target({ANNOTATION_TYPE, METHOD, CONSTRUCTOR, FIELD, PARAMETER})
public @interface RequiresDefaultDialer {
    String value() default "";

    String[] allOf() default {};

    String[] anyOf() default {};

    boolean conditional() default false;

    @Target({FIELD, METHOD, PARAMETER})
    @interface Read {
        RequiresDefaultDialer value() default @RequiresDefaultDialer;
    }

    @Target({FIELD, METHOD, PARAMETER})
    @interface Write {
        RequiresDefaultDialer value() default @RequiresDefaultDialer;
    }
}
