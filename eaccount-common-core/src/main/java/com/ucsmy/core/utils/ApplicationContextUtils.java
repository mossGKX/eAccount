package com.ucsmy.core.utils;

import org.springframework.context.ApplicationContext;

public class ApplicationContextUtils {

    private static ApplicationContext context = null;

    public static ApplicationContext getContext() {
        return context;
    }

    public static void setApplicationContext(ApplicationContext applicationContext) {
        if(context == null) {
            context = applicationContext;
        }
    }

    public static Object getBean(String name) {
        if (context.containsBean(name)) {
            return context.getBean(name);
        }
        return null;
    }

    public static <T> T getBean(String name, Class<T> requiredType) {
        return context.getBean(name, requiredType);
    }

    public static <T> T getBean(Class<T> requiredType) {
        return context.getBean(requiredType);
    }
}
