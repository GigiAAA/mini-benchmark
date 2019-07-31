package com.Riri.annotations;

//预热默认值为0，表示若方法不使用该注解就表明不需要预热
public @interface WarmUp {
    int iterations() default 0;
}
