package com.Riri.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

//Retention为修饰注解的注解-->指定注解存在的状态
@Retention(RetentionPolicy.RUNTIME)
//Target为修饰注解的注解-->主要作用为指定自定义注解可修饰的内容类型，如可修饰类、方法等(枚举)
@Target({ElementType.TYPE,ElementType.METHOD})
public @interface Measurement {
    //一组实验调用方法多少次
    int iterations();
    //一共进行多少组实验
    int group();
}
