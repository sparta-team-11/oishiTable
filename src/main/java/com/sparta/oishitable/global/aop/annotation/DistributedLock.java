package com.sparta.oishitable.global.aop.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface DistributedLock {

    String key();

    long waitTime() default 5L;

    long leaseTime() default 3L;

    TimeUnit unit() default TimeUnit.SECONDS;
}
