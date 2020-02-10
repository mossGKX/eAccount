package com.ucsmy.core.tool.log.log4j2;

import com.ucsmy.core.bean.LoginUser;
import com.ucsmy.core.bean.ManageLogInfo;
import com.ucsmy.core.utils.JsonUtils;
import com.ucsmy.core.utils.SpringSecurityUtils;
import com.ucsmy.core.utils.StringUtils;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.ThreadContext;
import org.aspectj.lang.Signature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.util.*;

/**
 * Log4j2通用方法
 * Created by ucs_gaokx on 2017/5/4.
 */
public class LogCommUtil {

    private static final Logger log = LoggerFactory.getLogger(LogCommUtil.class);

    // mybatis Executor里对查询和增删改操作的方法名称
    private static final String METHOD_QUERY = "query";
    private static final String METHOD_UPDATE = "update";
    // Log输出的信息标题
    public static final String OPERATE_METHOD = "【操作方法】";
    public static final String OPERATE_NAME = "【操作名称】";
    public static final String OPERATE_TYPE = "【操作类型】";
    public static final String IP_INFO = "【IP信息】";
    public static final String USER_INFO = "【操作员信息】";
    public static final String LOG_SQL = "【执行SQL】";

    // local variable
    private static final String UNKNOWN = "unknown";
    private static final String LOCAL_HOST = "127.0.0.1";

    // database custom column
    public static final String COL_IP_ADDRESS = "ip_address";
    public static final String COL_USER_ID = "user_id";
    public static final String COL_USER_NAME = "user_name";
    public static final String MSG_IP_ADDRESS = "ipAddress";
    public static final String MSG_USER_ID = "userId";
    public static final String MSG_USER_NAME = "userName";

    /**
     * 系统换行符
     */
    public static final String LINE_SEPERATOR = System.getProperty("line.separator");

    /**
     * 方法参数开始标志
     */
    private static final String PARAM_BEGIN = "(";

    /**
     * 方法参数结束标志
     */
    private static final String PARAM_END = ")";

    /**
     * 方法参数分隔标志
     */
    private static final String PARAM_SPILT = ",";

    /**
     * 方法名路径前缀
     */
    private static final String PATH_BEGIN = "com.";

    private LogCommUtil()
    {

    }

    /**
     * 生成ThreadContext里的 是否输出SQL信息 这条信息对应的KEY值
     * @param serviceSessionId
     * @return
     */
    public static String getSQLOutputKey(String serviceSessionId) {
        return serviceSessionId + "SQLOutput";
    }

    /**
     * 生成ThreadContext里的 是否输出到数据库 这条信息对应的KEY值
     * @param serviceSessionId
     * @return
     */
    public static String getDBOutputKey(String serviceSessionId) {
        return serviceSessionId + "DBOutput";
    }

    /**
     * 生成ThreadContext里的 是否输出返回值 这条信息对应的KEY值
     * @param serviceSessionId
     * @return
     */
    public static String getPrintOutputKey(String serviceSessionId) {
        return serviceSessionId + "printOutput";
    }

    /**
     * 生成ThreadContext里的 LogSQL信息 这条信息对应的KEY值
     * @param serviceSessionId
     * @return
     */
    public static String getLogSQLKey(String serviceSessionId) {
        return serviceSessionId + "LogSQL";
    }

    /**
     * 获取当前请求的ServiceSessionId <br>
     * 2017/5/18 修改：在一个Service方法里调用多次其他层有日志输出的service，需要记录对列编号
     * @return
     */
    public static String getServiceSessionId() {
        String sessionId = getRequestSessionId();
        String idList = ThreadContext.get(sessionId + "List");
        if (StringUtils.isEmpty(idList)) {
            return null;
        } else {
            String[] ids = idList.split(PARAM_SPILT);
            Integer maxId = Integer.parseInt(ids[ids.length - 1]);
            return sessionId + "no" + maxId;
        }
    }

    /**
     * 获取新的ServiceSessionId
     * @return
     */
    public static String getNewServiceSessionId() {
        String sessionId = getRequestSessionId();
        String idList = ThreadContext.get(sessionId + "List");
        if (StringUtils.isEmpty(idList)) {
            idList = "0";
            ThreadContext.put(getSessionIdListKey(sessionId), idList);
            return sessionId + "no" + "0";
        } else {
            String[] ids = idList.split(PARAM_SPILT);
            Integer maxId = Integer.parseInt(ids[ids.length - 1]);
            Integer id = maxId + 1;
            idList = idList + "," + id;
            ThreadContext.put(getSessionIdListKey(sessionId), idList);
            return sessionId + "no" + id;
        }
    }

    /**
     * 获取请求中的sessionId
     * @return
     */
    public static String getRequestSessionId() {
        String sessionId = "default";
        Object requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes != null) {
            HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
            HttpSession session = request.getSession();
            if (session != null) {
                sessionId = session.getId();
            }
        }
        return sessionId;
    }

    /**
     * 清除上下文中serviceSessionId相关信息
     * @param serviceSessionId
     */
    public static void removeThreadContext(String serviceSessionId) {
        // TODO
        ThreadContext.remove(serviceSessionId);
        ThreadContext.remove(getSQLOutputKey(serviceSessionId));
        ThreadContext.remove(getDBOutputKey(serviceSessionId));
        ThreadContext.remove(getPrintOutputKey(serviceSessionId));
        ThreadContext.remove(getLogSQLKey(serviceSessionId));

        String sessionId = getRequestSessionId();
        String idList = ThreadContext.get(getSessionIdListKey(sessionId));
        if (StringUtils.isNotEmpty(idList)) {
            String[] ids = idList.split(PARAM_SPILT);
            for (int i = 0; i < ids.length; i++) {
                if (ids[i].equals(serviceSessionId)) {
                    System.arraycopy(ids, i + 1, ids, i, ids.length - i);
                    break;
                }
            }
            if (ids.length > 0) {
                StringBuilder modifyIds = new StringBuilder();
                for (String id : ids) {
                    if (modifyIds.length() > 0) {
                        modifyIds.append(PARAM_SPILT).append(id);
                    } else {
                        modifyIds.append(id);
                    }
                }
                ThreadContext.put(getSessionIdListKey(sessionId), modifyIds.toString());
            } else {
                ThreadContext.remove(getSessionIdListKey(sessionId));
            }
        }
    }

    /**
     * 截取拦截的方法名
     * @param signature
     * @return
     */
    public static String getMethodName(Signature signature) {
        String pointSignature = signature.toString();
        String name = signature.getName();
        int begin = pointSignature.indexOf(PATH_BEGIN);
        int end = pointSignature.indexOf(name);
        return pointSignature.substring(begin, end + name.length());
    }

    /**
     * 获取拦截方法的入参列表，格式：
     * type - 参数类型
     * value - 参数值
     * @return
     */
    public static String getInputParamList(Signature signature, Object[] values) {
        // 获取入参类型
        String pointSignature = signature.toString();
        int begin = pointSignature.indexOf(PARAM_BEGIN);
        int end = pointSignature.indexOf(PARAM_END);
        String subString = pointSignature.substring(begin + 1, end);
        String[] paramType = subString.split(PARAM_SPILT);
        List<Map<String, Object>> inParamList = new ArrayList<>();
        if (StringUtils.isNotEmpty(subString)) {
            for (int i = 0; i < paramType.length; i++) {
                Map<String, Object> param = new HashMap<>();
                param.put("type", paramType[i]);
                param.put("value", JsonUtils.formatObjectToJson(values[i]));
                inParamList.add(param);
            }
        }
        StringBuilder input = new StringBuilder();
        input.append(LINE_SEPERATOR).append("【入参列表】").append(inParamList);
        return input.toString();
    }

    /**
     * 获取拦截方法的输出内容
     * @param signature
     * @param value
     * @return
     */
    public static String getOutputParam(Signature signature, Object value) {
        if (value != null) {
            Map<String, Object> outputDate = new HashMap<>();
            String outputType = signature.toString().substring(0, signature.toString().indexOf(" "));
            outputDate.put("type", outputType);
            outputDate.put("value", JsonUtils.formatObjectToJson(value));
            StringBuilder output = new StringBuilder();
            output.append(LINE_SEPERATOR).append("【输出数据】").append(outputDate);
            return output.toString();
        }
        return null;
    }

    /**
     * 获取操作用户的信息
     * @return
     */
    public static String getUserInfo() {
        Map<String, Object> userJson = new HashMap<>();
        LoginUser user = SpringSecurityUtils.getUserByContext();
        if (user != null) {
            userJson.put(MSG_USER_ID, user.getId());
            userJson.put(MSG_USER_NAME, user.getUsername());
        }
        return JsonUtils.formatObjectToJson(userJson);
    }

    /**
     * 获取Ip地址
     * @return
     */
    public static String getIpAddress() {
        Object requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes != null) {
            HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
            String ipAddress = request.getHeader("x-forwarded-for");
            if(ipAddress == null || ipAddress.length() == 0 || UNKNOWN.equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getHeader("Proxy-Client-IP");
            }
            if(ipAddress == null || ipAddress.length() == 0 || UNKNOWN.equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getHeader("WL-Proxy-Client-IP");
            }
            if(ipAddress == null || ipAddress.length() == 0 || UNKNOWN.equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getRemoteAddr();
            }
            if (LOCAL_HOST.equals(ipAddress)) {
                try {
                    ipAddress = InetAddress.getLocalHost().getHostAddress();
                } catch (UnknownHostException e) {
                    ipAddress = "";
                }
            }
            return ipAddress;
        }
        return null;
    }

    /**
     * 获取存入数据库的实体对象
     * @param info 输出到日志的信息，需要过滤IP地址和用户信息
     * @return
     */
    public static ManageLogInfo getManageLogInfo(String info) {
        // 过滤info信息
        info = info.replaceAll(IP_INFO + ".*[\\t\\n\\r]", "");
        info = info.replaceAll(USER_INFO + ".*[\\t\\n\\r]", "");
        info = info.replaceAll("[\t\n\r]", "");
        ManageLogInfo logInfo = new ManageLogInfo();
        logInfo.setId(StringUtils.getUUID());
        logInfo.setCreateTime(new Date());
        logInfo.setIpAddress(getIpAddress());
        LoginUser user = SpringSecurityUtils.getUserByContext();
        if (user != null) {
            logInfo.setUserId(user.getId());
            logInfo.setUserName(user.getUsername());
        }
        logInfo.setLogLevel(Level.INFO.toString());
        logInfo.setMessage(info);
        return logInfo;
    }

    /**
     * 从拦截器里获取的方法里提取执行sql
     * @param invocation
     * @return
     */
    public static void getSqlLog(Invocation invocation) {
        // 判断ThreadContext里是否有SessionIdSql
        String sessionId = getServiceSessionId();
        // 提取方法
        String methodName = invocation.getMethod().getName();
        MappedStatement mappedStatement = null;
        BoundSql boundSql = null;
        if (Boolean.valueOf(ThreadContext.get(getSQLOutputKey(sessionId))) && StringUtils.isNotEmpty(ThreadContext.get(sessionId))) {
            Object[] args = invocation.getArgs();
            mappedStatement = (MappedStatement) args[0];
            if (METHOD_UPDATE.equals(methodName)) {
                Object parameters = args[1];
                boundSql = mappedStatement.getBoundSql(parameters);
            } else if (METHOD_QUERY.equals(methodName)) {
                Object parameters = null;
                if (args.length > 2) {
                    parameters = args[1];
                }
                if (args.length == 4) {
                    boundSql = mappedStatement.getBoundSql(parameters);
                } else if (args.length == 6){
                    boundSql = (BoundSql) args[5];
                }
            }
        }
        if (mappedStatement != null) {
            // 添加ThreadContext中，因为如果执行SQL发生了主键冲突，Interceptor会拦截错误，使用乱序流水号生成主键再执行一次
            ThreadContext.put(getLogSQLKey(sessionId), printSql(mappedStatement.getConfiguration(), boundSql));
        }
    }

    /**
     * 转换特定类型的参数值
     * @param parameter
     * @return
     */
    private static String getParameterValue(Object parameter) {
        String value;
        if (parameter instanceof String) {
            // 对$符号做特殊处理，如果$符号后面没有跟整数，会抛出Illegal group reference
            if ("$".equals(String.valueOf(parameter))) {
                parameter = "\\" + parameter;
            }
            value = "'" + parameter.toString() + "'";
        } else if (parameter instanceof Date) {
            DateFormat formatter = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT, Locale.CHINA);
            value = "'" + formatter.format(new Date(((Date) parameter).getTime())) + "'";
        } else {
            if (parameter != null) {
                value = parameter.toString();
            } else {
                value = "''";
            }
        }
        return value;
    }

    /**
     * 打印SQL
     * @param configuration
     * @param boundSql
     * @return
     */
    private static String printSql(Configuration configuration, BoundSql boundSql) {
        Object parameterObject = boundSql.getParameterObject();
        List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
        String sql = boundSql.getSql().replaceAll("[\\s]+", " ");
        if (!parameterMappings.isEmpty() && parameterObject != null) {
            TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry();
            if (typeHandlerRegistry.hasTypeHandler(parameterObject.getClass())) {
                sql = sql.replaceFirst("\\?", getParameterValue(parameterObject));
            } else {
                MetaObject metaObject = configuration.newMetaObject(parameterObject);
                for (ParameterMapping parameterMapping : parameterMappings) {
                    String propertyName = parameterMapping.getProperty();
                    if (metaObject.hasGetter(propertyName)) {
                        Object obj = metaObject.getValue(propertyName);
                        sql = sql.replaceFirst("\\?", getParameterValue(obj));
                    } else if (boundSql.hasAdditionalParameter(propertyName)) {
                        Object obj = boundSql.getAdditionalParameter(propertyName);
                        sql = sql.replaceFirst("\\?", getParameterValue(obj));
                    }
                }
            }
        }
        return sql;
    }

    /**
     * 生成ThreadContext中 sessionIdList 的Key值
     * @param sessionId
     */
    private static String getSessionIdListKey(String sessionId) {
        return sessionId + "List";
    }
}
