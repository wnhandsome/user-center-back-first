package com.w6n.usercenter;

import org.junit.jupiter.api.Test;

import java.util.regex.Pattern;

public class Demo {

    @Test
    public void t1(){
        String validPattern = "^[a-zA-Z0-9]{4,20}$";
        boolean b = Pattern.matches(validPattern,"123456qweqwe");
        System.out.println(b);

        System.out.println("123".equals(null));
    }

}
