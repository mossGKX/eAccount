package com.ucsmy.core.tool.interceptor;

import com.ucsmy.core.tool.log.log4j2.LogCommUtil;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;

import java.util.Properties;

/**
 * 拦截Mybatis执行SQL，用于特定节点的日志展示；<br>
 * 只有在service层设置了@Logger的方法并且设置printSql = true才会进行日志输出SQL；<br>
 * 这里只拦截update方法，因为query方法被分页拦截器拦截了<br>
 * Created by ucs_gaokx on 2017/5/5.
 */
@Intercepts({
        @Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class}),
})
public class MybatisLogInterceptor implements Interceptor {

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        LogCommUtil.getSqlLog(invocation);
        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {
        // do nothing
    }
}
