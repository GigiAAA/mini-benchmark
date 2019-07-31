package com.Riri;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public class Main{
    public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException, IOException, InvocationTargetException {
        CaseLoader loader=new CaseLoader();
        loader.load().run();
    }
}
