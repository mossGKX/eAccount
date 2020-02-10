package com.ucsmy.core.tool.interceptor;

import com.ucsmy.core.tool.interceptor.domain.PageInfo;
import com.ucsmy.core.tool.interceptor.domain.Pageable;
import com.ucsmy.core.tool.interceptor.utils.MSUtils;
import com.ucsmy.core.tool.log.log4j2.LogCommUtil;
import org.apache.ibatis.binding.MapperMethod;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;


@Intercepts({
        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}),
        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class, CacheKey.class, BoundSql.class})
})
public class MybatisPageInterceptor implements Interceptor {

    private final static Field additionalParametersField;

    static {
        try {
            //反射获取 BoundSql 中的 additionalParameters 属性
            additionalParametersField = BoundSql.class.getDeclaredField("additionalParameters");
            additionalParametersField.setAccessible(true);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        // 2017.5.5 增加日志SQL输出，详见MybatisLogInterceptor
        LogCommUtil.getSqlLog(invocation);

        Object[] args = invocation.getArgs();
        Object parameter = args[1];

        Pageable pageable;
        if((pageable = isPageable(parameter)) == null)
            return invocation.proceed();

        MappedStatement statement = (MappedStatement) args[0];
        RowBounds rowBounds = (RowBounds) args[2];
        ResultHandler resultHandler = (ResultHandler) args[3];
        Executor executor = (Executor) invocation.getTarget();

        List<PageInfo> result = new ArrayList<>();
        PageInfo info = new PageInfo();
        result.add(info);

        BoundSql boundSql;
        CacheKey cacheKey;
        //由于逻辑关系，只会进入一次
        if(args.length == 4){// 4 个参数时
            boundSql = statement.getBoundSql(parameter);
            cacheKey = executor.createCacheKey(statement, parameter, rowBounds, boundSql);
        } else {// 6 个参数时
            cacheKey = (CacheKey) args[4];
            boundSql = (BoundSql) args[5];
        }

        //反射获取动态参数
        Map<String, Object> additionalParameters = (Map<String, Object>) additionalParametersField.get(boundSql);

        // 获取 count sql
        String countSql = getCountSql(boundSql.getSql());
        BoundSql countBoundSql = new BoundSql(statement.getConfiguration(), countSql, boundSql.getParameterMappings(), parameter);
        // 当使用动态 SQL 时，可能会产生临时的参数，这些参数需要手动设置到新的 BoundSql 中
        for (String key : additionalParameters.keySet()) {
            countBoundSql.setAdditionalParameter(key, additionalParameters.get(key));
        }
        // 创建 count 查询的缓存 key
        CacheKey countKey = executor.createCacheKey(statement, parameter, RowBounds.DEFAULT, countBoundSql);
        // countKey.update(MSUtils.COUNT);
        // 根据当前的 statement 创建一个返回值为 Long 类型的 ms
        MappedStatement countMs = MSUtils.newCountMappedStatement(statement);
        // 执行 count 查询
        Object countResultList = executor.query(countMs, parameter, RowBounds.DEFAULT, resultHandler, countKey, countBoundSql);
        Long count = (Long) ((List) countResultList).get(0);

        if(count > 0) {
            String pageSql = getPageSql(boundSql.getSql(), pageable);
            BoundSql pageBoundSql = new BoundSql(statement.getConfiguration(), pageSql, boundSql.getParameterMappings(), parameter);
            // 设置动态参数
            for (String key : additionalParameters.keySet()) {
                pageBoundSql.setAdditionalParameter(key, additionalParameters.get(key));
            }
            info.setData(executor.query(statement, parameter, RowBounds.DEFAULT, resultHandler, cacheKey, pageBoundSql));
        } else {
            info.setData(new ArrayList());
        }

        info.setCount(count);
        info.init(pageable);
        return result;
    }

    private Pageable isPageable(Object parameter) {
        if(parameter == null)
            return null;
        if(Pageable.class.isAssignableFrom(parameter.getClass()))
            return (Pageable) parameter;
        if(!MapperMethod.ParamMap.class.isAssignableFrom(parameter.getClass()))
            return null;

        MapperMethod.ParamMap<Object> parameters = (MapperMethod.ParamMap) parameter;
        for(Map.Entry<String, Object> entry : parameters.entrySet()) {
            if(entry.getValue() != null && Pageable.class.isAssignableFrom(entry.getValue().getClass()))
                return (Pageable) entry.getValue();
        }
        return null;
    }

    private String getPageSql(String sql, Pageable pageable) {
        return sql + " LIMIT " + pageable.getOffset() + ", " + pageable.getPageSize();
    }

    private String getCountSql(String sql) {
        return "SELECT COUNT(1) FROM (" + sql + ") c";
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {
    }

    public static void main(String[] args) {
        String select = "SELECT * FROM ( SELECT * FROM manage_gen_code LIMIT 0, 1 ) CODE LEFT JOIN manage_gen_attr attr ON CODE .id = attr.code_id";

    }
}
