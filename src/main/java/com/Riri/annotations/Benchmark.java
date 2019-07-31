package com.Riri.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

//定义注解的语法规则：在接口前加@
//该注解类似于Override注解，无方法，被该注解标注的方法表明是要进行测试的方法
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Benchmark {

}
