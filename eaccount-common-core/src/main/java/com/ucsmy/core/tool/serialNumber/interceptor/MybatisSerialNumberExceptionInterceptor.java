package com.ucsmy.core.tool.serialNumber.interceptor;

import com.ucsmy.core.tool.serialNumber.utils.SerialNumberContextHelper;
import com.ucsmy.core.tool.serialNumber.generator.AbstractSerialNumberGenerator;
import com.ucsmy.core.utils.BeanUtil;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;

import java.lang.reflect.InvocationTargetException;
import java.util.Properties;

/**
 * 新增对Executor.update的重复主键错误处理，检查冲突的主键是否使用了流水号模式生成； <br>
 * 如果使用了流水号模式生成造成的主键冲突，要进行错误纠正处理，从备份里恢复redis的流水号记录同时使用备份流水号替换冲突的主键。 <br>
 * 使用需要在Mybatis配置里添加该拦截器。<br>
 * Modified by ucs on 2017/5/27. <br>
 */
@Intercepts({
        @Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class}),
})
public class MybatisSerialNumberExceptionInterceptor implements Interceptor {

    /**
     * 主键重复时的错误标识段
     */
    private static final String ERROR_MESSAGE_DUPLICATE_PRIMARY = "for key 'PRIMARY'";

    /**
     * 流水号主键长度
     */
    private static final int SERIALNUMBER_LENGTH = 18;

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        return handlerDuplicateKeyException(invocation);
    }

    private Object handlerDuplicateKeyException(Invocation invocation) throws Throwable {
        Object result = null;
        try {
            result = invocation.proceed();
        } catch (InvocationTargetException e) {
            String errorMessage = e.getTargetException().getMessage();
            if (errorMessage.contains(ERROR_MESSAGE_DUPLICATE_PRIMARY)) {
                // 获取到的errorMessage格式类似于：Duplicate entry '990017052600000001' for key 'PRIMARY'
                System.err.println(errorMessage);
                // 第一个'符号的位置
                int firstIndex = errorMessage.indexOf('\'');
                // 获取主键的入参
                String primaryKey = errorMessage.substring(firstIndex + 1
                        , errorMessage.indexOf('\'', firstIndex + 1));
                // 只处理主键为流水号类型的数据
                if (checkPrimaryKey(primaryKey)) {
                    // 替换主键的入参
                    replacePrimaryKeyValue(invocation, primaryKey);
                    // 再次执行
                    result = invocation.proceed();
                }
            } else {
                throw e;
            }
        }
        return result;
    }

    /**
     * 检查主键是否为流水号，特征：长度必须为18位
     *
     * @param primaryKey
     * @return
     */
    private boolean checkPrimaryKey(String primaryKey) {
        return primaryKey.length() == SERIALNUMBER_LENGTH;
    }

    /**
     * 替换重复的主键值为备用值
     *
     * @param invocation
     */
    private void replacePrimaryKeyValue(Invocation invocation, String primaryKey) {
        Object[] args = invocation.getArgs();
        // 生成备用的流水号
        AbstractSerialNumberGenerator serialNumberGenerator = SerialNumberContextHelper.getBackupSerialNumberGenerator();
        String targetKey = serialNumberGenerator.get();
        Object parameters = args[1];
        BeanUtil.replaceKeyByReflectField(parameters, primaryKey, targetKey);
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
