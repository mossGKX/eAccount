package com.ucsmy.core.utils;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.FatalBeanException;
import org.springframework.util.ClassUtils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.Set;

/**
 * bean工具类，封装一些常用的操作bean的方法
 * Created by ucs_gaokx on 2017/4/18.
 */
public class BeanUtil {

    /**
     * 使用BeanUtils.copyProperties复制对象属性，过滤值为null的属性
     * @param source
     * @param target
     */
    public static void copyPropertiesIgnoreNull(Object source, Object target) {
        final org.springframework.beans.BeanWrapper src = new BeanWrapperImpl(source);
        java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();
        Set<String> emptyNames = new HashSet<>();
        for(java.beans.PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null) {
                emptyNames.add(pd.getName());
            }
        }
        String[] result = new String[emptyNames.size()];
        emptyNames.toArray(result);
        BeanUtils.copyProperties(source, target, result);
    }

    /**
     * 替换obj里含有某个值为 sourceKey 的字段值为 targetKey <br>
     * Created by ucs on 2017/5/27. <br>
     *
     * @param obj       操作的object
     * @param sourceKey 原来的值
     * @param targetKey 替换的目标值
     */
    public static void replaceKeyByReflectField(Object obj, Object sourceKey, Object targetKey) {
        final BeanWrapper src = new BeanWrapperImpl(obj);
        // 获取所有字段
        PropertyDescriptor[] pds = src.getPropertyDescriptors();
        for (PropertyDescriptor pd : pds) {
            // 获取getter和setter方法，从getter方法里取原来的值与sourceKey匹配
            Method writeMethod = pd.getWriteMethod();
            Method readMethod = pd.getReadMethod();
            if (readMethod != null
                    && writeMethod != null
                    && ClassUtils.isAssignable(writeMethod.getParameterTypes()[0], readMethod.getReturnType())) {
                try {
                    if (!Modifier.isPublic(readMethod.getDeclaringClass().getModifiers())) {
                        readMethod.setAccessible(true);
                    }
                    Object value = readMethod.invoke(obj);
                    if (!Modifier.isPublic(writeMethod.getDeclaringClass().getModifiers())) {
                        writeMethod.setAccessible(true);
                    }
                    if (value.toString().equals(sourceKey.toString())) {
                        writeMethod.invoke(obj, targetKey);
                    }
                } catch (Throwable ex) {
                    throw new FatalBeanException(
                            "Could not replace property '" + pd.getName() + "' with target key '" + targetKey.toString() + "'.", ex);
                }
            }
        }
    }
}
