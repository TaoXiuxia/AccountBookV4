package com.xiutao;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class BootUp {
    public static void main(String[] args) {
        try {
            ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:mybatis-spring.xml");
            context.start();
            System.out.println("Dubbo provider is start.....");
            synchronized (BootUp.class) {
                try {
                    BootUp.class.wait();
                } catch (Throwable e) {

                }

            }
        }catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}
