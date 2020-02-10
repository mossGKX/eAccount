package com.ucsmy.eaccount.config;

import com.ucsmy.core.tool.interceptor.MybatisLogInterceptor;
import com.ucsmy.core.tool.interceptor.MybatisPageInterceptor;
import com.ucsmy.core.tool.serialNumber.interceptor.MybatisSerialNumberExceptionInterceptor;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.boot.autoconfigure.MybatisProperties;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

/**
 * Mybatis配置
 * @author gaokx
 */
@Configuration
public class MybatisConfig {

    public  static  String basePackage = "com.ucsmy";

    @Bean("sqlSessionFactory")
    public SqlSessionFactory sqlSessionFactoryBean(DataSource dataSource, MybatisProperties properties) throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);

        // 分页拦截器
        MybatisPageInterceptor interceptor = new MybatisPageInterceptor();
        // 日志拦截器
        MybatisLogInterceptor logInterceptor = new MybatisLogInterceptor();
        // 流水号异常处理拦截器
        MybatisSerialNumberExceptionInterceptor serialNumberInterceptor = new MybatisSerialNumberExceptionInterceptor();
        bean.setPlugins(new Interceptor[] { interceptor, logInterceptor, serialNumberInterceptor });

        bean.setMapperLocations(properties.resolveMapperLocations());
        return bean.getObject();
    }

    @Bean
    public MapperScannerConfigurer configurer() {
        MapperScannerConfigurer configurer = new MapperScannerConfigurer();
        configurer.setAnnotationClass(Repository.class);
        configurer.setBasePackage(basePackage);
        return configurer;
    }
}
