package com.example.demo.audit;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Auditable {
    String entityType(); // DEVICE, LICENSE, ASSIGNMENT
    String action();     // CREATE, UPDATE, DELETE
}