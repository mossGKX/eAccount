package com.ucsmy.core.utils;

import com.ucsmy.core.exception.ServiceException;
import com.ucsmy.core.bean.ManageGenAttr;
import com.ucsmy.core.bean.ManageGenCode;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.internal.types.JavaTypeResolverDefaultImpl;
import org.mybatis.generator.internal.util.JavaBeansUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.ui.freemarker.SpringTemplateLoader;

import javax.sql.DataSource;
import java.io.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 代码生成工具类
 */
public class GenUtils {

    private static Logger logger = LoggerFactory.getLogger(GenUtils.class);
    private static final Configuration configuration = new Configuration(Configuration.VERSION_2_3_23);
    private static final String rootCatalog;// 项目根路径
    private static final String templatePath = "classpath:/codeGenTemplates/"; // 模板路径

    private static final String javaCatalog = "src/main/java/";// java代码路径
    private static final String mapperCatalog = "src/main/resources/sql/mapper/";// java代码路径
    private static final String pageCatalog = "src/main/resources/react/src/";// react代码代码路径
    private static final String TRUE = "1";
    private static final String FALSE = "0";
    private static final String TAG = "(~~~~~~~)";
    private static final String TAG_PATH = "/";

    private static final String MAPPER = "mapper";
    private static final String DAO = "dao";
    private static final String SERVICE = "service";
    private static final String SERVICE_IMPL = "serviceImpl";
    private static final String CONTROLLER = "controller";
    private static final String ENTITY = "entity";
    private static final String REACT_MODEL = "reactModel";
    private static final String REACT_VIEW = "ReactView";
    private static final String REACT_FORM = "reactForm";

    static {
        configuration.setTemplateLoader(new SpringTemplateLoader(new PathMatchingResourcePatternResolver(), templatePath));
        rootCatalog = new File(GenUtils.class.getResource("/").getFile()).getParentFile().getParentFile().getPath().concat("/");
    }

    public static void genCode(ManageGenCode code) {
        // 处理数据
        Map<String, Object> data = new HashMap<>();
        Map<String, Object> react = new HashMap<>();
        String uri = code.getUri();
        String modelPath = getModelPath(uri);// view页面引用model路径
        react.put("path", uri.replaceAll("[^/]*", "."));// sys/config生成如：../..
        react.put("modelPath", modelPath);
        react.put("namespace", StringUtils.getCamelCaseString(code.getUri(), Boolean.FALSE));// sys/config生成如：sysConfig
        data.put("_$", "$");
        data.put("_jj", "#");
        data.put("code", code);
        data.put("react", react);

        List<ManageGenAttr> attrs = new ArrayList<>();
        code.getAttrs().stream().filter(a -> TRUE.equals(a.getInsert())).forEach(attrs::add);
        data.put("insert", attrs);// 插入
        attrs = new ArrayList<>();
        code.getAttrs().stream().filter(a -> TRUE.equals(a.getUpdate())).forEach(attrs::add);
        data.put("update", attrs);// 修改
        attrs = new ArrayList<>();
        code.getAttrs().stream().filter(a -> TRUE.equals(a.getInsert()) || TRUE.equals(a.getUpdate())).forEach(attrs::add);
        data.put("insertAndUpdate", attrs);// 插入和修改
        attrs = new ArrayList<>();
        code.getAttrs().stream().filter(a -> TRUE.equals(a.getList())).forEach(attrs::add);
        data.put("list", attrs);// 页面列表
        attrs = new ArrayList<>();
        code.getAttrs().stream().filter(a -> TRUE.equals(a.getQuery())).forEach(attrs::add);
        data.put("query", attrs);// 查询

        String javaPath = javaCatalog.concat(code.getPackageName().replace(".", "/")).concat("/");
        String reactViewPath = pageCatalog.concat("components/").concat(uri).concat("/");
        genTemplateData(MAPPER, data, GenUtils.mapperCatalog.concat(code.getEntityName()).concat(".xml"));
        genTemplateData(DAO, data, javaPath.concat("dao/").concat(code.getEntityName()).concat("Dao.java"));
        genTemplateData(SERVICE, data, javaPath.concat("service/").concat(code.getEntityName()).concat("Service.java"));
        genTemplateData(SERVICE_IMPL, data, javaPath.concat("service/impl/").concat(code.getEntityName()).concat("ServiceImpl.java"));
        genTemplateData(CONTROLLER, data, javaPath.concat("web/").concat(code.getEntityName()).concat("Controller.java"));
        genTemplateData(ENTITY, data, javaPath.concat("entity/").concat(code.getEntityName()).concat(".java"));
        genTemplateData(REACT_MODEL, data, pageCatalog.concat("models/").concat(modelPath).concat(".jsx"));
        genTemplateData(REACT_VIEW, data, reactViewPath.concat("View.jsx"));
        genTemplateData(REACT_FORM, data, reactViewPath.concat("Form.jsx"));
    }

    public static void genTemplateData(String templateStr, Map<String, Object> data, String outPath) {
        FileWriter fw = null;
        BufferedWriter bw = null;
        try {
            File file = new File(rootCatalog.concat(outPath));
            if(file.exists())// 已存在的文件不进行覆盖，如需要重新生成，请删除文件
                return;

            if(!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }

            // 生成输出文件
            StringWriter stringWriter = new StringWriter();
            Template template = configuration.getTemplate(templateStr.concat(".ftl"));
            template.process(data, stringWriter);
            fw = new FileWriter(file);
            bw = new BufferedWriter(fw);
            template.process(data, bw);
        } catch (Exception e) {
            if(fw != null) {
                try {
                    fw.close();
                } catch (IOException e1) {
                    logger.error("关闭FileWriter异常", e1);
                }
            }
            if(bw != null) {
                try {
                    bw.flush();
                    bw.close();
                } catch (IOException e1) {
                    logger.error("关闭BufferedWriter异常", e1);
                }
            }
            throw new ServiceException("生产模板文件异常", e);
        }

    }

    public static List<ManageGenAttr> getTableColumns(DataSource dataSource, String database, ManageGenCode entity) {
        List<ManageGenAttr> attrs = new ArrayList<>();
        ResultSet rs = null;
        try {
            Connection connection = dataSource.getConnection();
            rs = connection.getMetaData().getColumns (connection.getCatalog(), database, entity.getDataTable(), "%");
            int i = 0;
            while (rs.next()) {
                String jdbcName = rs.getString("COLUMN_NAME");
                String des = rs.getString("REMARKS");
                int jdbcType = rs.getInt("DATA_TYPE");
                boolean isId = "id".equals(jdbcName);
                JavaTypeResolverDefaultImpl javaTypeResolver = new JavaTypeResolverDefaultImpl();
                IntrospectedColumn column = new IntrospectedColumn();
                column.setJdbcType(jdbcType);
                FullyQualifiedJavaType javaType = javaTypeResolver.calculateJavaType(column);
                attrs.add(ManageGenAttr.builder()
                        .id(StringUtils.getUUID())
                        .jdbcName(jdbcName)
                        .javaName(StringUtils.getCamelCaseString(jdbcName, Boolean.FALSE))
                        .des(des == null ? "" : des)
                        .jdbcType(jdbcType)
                        .javaType("java.lang".equals(javaType.getPackageName()) ? javaType.getShortName() : javaType.toString())
                        .insert(TRUE)
                        .update(isId ? FALSE : TRUE)
                        .list(isId ? FALSE : TRUE)
                        .query(FALSE)
                        .weight(i)
                        .codeId(entity.getId())
                        .build());
                i++;
            }

        } catch (SQLException e) {
            logger.error("数据库异常", e);
            throw new ServiceException("数据库异常");
        } finally {
            try {
                if(rs != null)
                    rs.close();
            } catch (SQLException e) {
                logger.error("ResultSet关闭异常", e);
            }
        }
        return attrs;
    }

    private static String getModelPath(String uri) {
        int index = uri.lastIndexOf('/');
        if(index == -1)
            return StringUtils.firstLetterUpperCase(uri);
        return uri.substring(0, index + 1).concat(StringUtils.firstLetterUpperCase(uri.substring(index + 1)));
    }

}
