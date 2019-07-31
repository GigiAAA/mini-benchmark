package com.Riri.cases;

import com.Riri.annotations.Benchmark;
import com.Riri.annotations.Measurement;

@Measurement(iterations = 50000,group = 5)
public class StringConcatCase {
    private void doNothing(){}
    @Benchmark
    public String testStringBuilderAdd(){
        doNothing();
        StringBuilder sb=new StringBuilder();
        for(int i=0;i<10;i++){
            sb.append(i);
        }
        return sb.toString();
    }
    @Benchmark
    public String testStringAdd(){
        doNothing();
        String s="";
        for(int i=0;i<10;i++){
            s+=i;
        }
        return s;
    }
}
