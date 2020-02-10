package com.ucsmy.core.configuration;

import com.ucsmy.core.utils.ApplicationContextUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;

public interface InitializationConfig extends ApplicationContextAware {
    @Override
    default void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ApplicationContextUtils.setApplicationContext(applicationContext);
    }
}
