package com.Riri;

import com.Riri.annotations.Benchmark;
import com.Riri.annotations.Measurement;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

//用于运行当前测试类
class CaseRunner{
    private static final int DEFAULT_ITERATIONS=10;
    private static final int DEFAULT_GROUP=5;
    private final List<Case> caseList;

    public CaseRunner(List<Case> caseList) {
        this.caseList = caseList;
    }
    //TODO:没有将WarmUp预热考虑进来
    //每组实验开始之前均需预热
    public void run() throws InvocationTargetException, IllegalAccessException {
        for(Case bCase:caseList){
            int iterations=DEFAULT_ITERATIONS;
            int group=DEFAULT_GROUP;
            //先获取类级别的配置
            Measurement classMeasurement=bCase.getClass().getAnnotation(Measurement.class);
            if(classMeasurement!=null){
                iterations=classMeasurement.iterations();
                group=classMeasurement.group();
            }
            //找到对象中哪些方法是需要测试的方法
            Method[] methods=bCase.getClass().getMethods();
            for(Method method:methods){
                Benchmark benchmark=method.getAnnotation(Benchmark.class);
                if(benchmark==null){
                    continue;
                }
                Measurement methodMeasurement=method.getAnnotation(Measurement.class);
                if(methodMeasurement!=null){
                    iterations=methodMeasurement.iterations();
                    group=methodMeasurement.group();
                }
                runCase(bCase,method,iterations,group);
            }
        }
    }

    private void runCase(Case bcase,Method method,int iterations,int group) {
        System.out.println(method.getName());
        for(int i=0;i<group;i++){
            System.out.printf("第%d次实验,",i);
            long t1=System.currentTimeMillis();
            for(int j=0;j<iterations;j++){
                try {
                    method.invoke(bcase);
                } catch (IllegalAccessException e) {
                    System.out.println("查找文件错误");
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
            long t2=System.currentTimeMillis();
            System.out.printf("耗时:%d 毫秒%n",t2-t1);
        }
    }
}
//用于查找下一个要运行的测试类
public class CaseLoader {
    //也可传入包的路径
    public CaseRunner load() throws IOException {
        String pkgDot="com.Riri.cases";
        String pkg="com/Riri/cases";
        List<String> classNameList=new ArrayList<String>();
        //1.根据一个固定的类找到类加载器
        //2.根据加载器找到类文件所在路径
        //3.扫描路径的所有类文件
        ClassLoader classLoader=this.getClass().getClassLoader();
        Enumeration<URL> urls= null;
        urls = classLoader.getResources(pkg);
        while (urls.hasMoreElements()){
            URL url=urls.nextElement();
            if(!url.getProtocol().equals("file")){
                //如果不是*.class文件，暂时不支持
                continue;
            }
            String dirName= null;
            try {
                dirName = URLDecoder.decode(url.getPath(),"UTF-8");
            } catch (UnsupportedEncodingException e) {
                System.out.println("获取文件路径错误");
                e.printStackTrace();
            }
            File dir=new File(dirName);
            if(!dir.isDirectory()){
                //不是目录
                continue;
            }
            //扫描该目录下的所有*.class文件，作为所有的类文件
            File[] files=dir.listFiles();
            if(files==null){
                continue;
            }
            for(File file:files){
                //TODO:没有判断后缀是否是.class
                String fileName=file.getName();
                String className=fileName.substring(0,fileName.length()-6);
                classNameList.add(className);
            }
        }
        List<Case> caseList=new ArrayList<Case>();
        for(String className:classNameList){
            Class<?> cls= null;
            try {
                cls = Class.forName(pkgDot+"."+className);
            } catch (ClassNotFoundException e) {
                System.out.println("遍历case包下的类错误");
                e.printStackTrace();
            }
            //判断这个类是否实现Case，若实现，则将该类实例化加到集合中
            if(hasInterface(cls,Case.class)){
                try {
                    caseList.add((Case) cls.newInstance());
                } catch (InstantiationException e) {
                    System.out.println("实现Case类添加错误");
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }

        return new CaseRunner(caseList);
    }

    private boolean hasInterface(Class<?> cls,Class<?> interfaces){
        Class<?>[] intfs=cls.getInterfaces();
        for(Class<?> i:intfs){
            if(i==interfaces){
                return true;
            }
        }
        return false;
    }
}
