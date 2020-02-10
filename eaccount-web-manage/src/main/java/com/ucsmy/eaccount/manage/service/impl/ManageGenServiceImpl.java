package com.ucsmy.eaccount.manage.service.impl;

import com.ucsmy.core.tool.interceptor.domain.PageInfo;
import com.ucsmy.core.tool.interceptor.domain.Pageable;
import com.ucsmy.core.utils.GenUtils;
import com.ucsmy.core.exception.ServiceException;
import com.ucsmy.eaccount.config.properties.UcsmyProperties;
import com.ucsmy.eaccount.manage.dao.ManageGenAttrDao;
import com.ucsmy.eaccount.manage.dao.ManageGenCodeDao;
import com.ucsmy.core.bean.ManageGenAttr;
import com.ucsmy.core.bean.ManageGenCode;
import com.ucsmy.eaccount.manage.ext.AntdPageInfo;
import com.ucsmy.core.vo.RetMsg;
import com.ucsmy.eaccount.manage.ext.SelectInfo;
import com.ucsmy.eaccount.manage.service.ManageGenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class ManageGenServiceImpl extends BasicServiceImpl<ManageGenCode, ManageGenCodeDao> implements ManageGenService {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private ManageGenAttrDao genAttrDao;

    @Autowired
    private DataSourceProperties properties;

    @Autowired
    private UcsmyProperties ucsmyProperties;

    @Autowired
    private ManageGenCodeDao manageGenCodeDao;

    @Override
    protected RetMsg saveHandle(ManageGenCode entity) {
        RetMsg retMsg = super.saveHandle(entity);// 生成ID,并返回

        if(!dao.findAllTables().contains(entity.getDataTable())) {
            return RetMsg.error("数据库表不存在");
        }

        GenUtils.getTableColumns(dataSource, getDatabase(), entity).forEach(attr -> {
            if(genAttrDao.save(attr) == 0) {
                throw new ServiceException("表属性无法插入");
            }
        });

        return retMsg;
    }

    @Override
    public List<SelectInfo> getAllTables() {
        List<SelectInfo> selects = new ArrayList<>();
        dao.findAllTables().forEach(t -> selects.add(SelectInfo.builder().id(t).name(t).build()));
        return selects;
    }

    @Override
    @Transactional
    public RetMsg update(ManageGenCode entity) {
        if(entity.getAttrs() != null) {
            entity.getAttrs().forEach(attr -> genAttrDao.update(attr));
        }

        RetMsg retMsg = super.update(entity);

        if(!entity.isNoGen() && ucsmyProperties.isGenCode()) {// 代码生成
            ManageGenCode code = this.getById(entity.getId());
            code.getAttrs().sort(Comparator.comparingInt(ManageGenAttr::getWeight));
            GenUtils.genCode(code);
        }

        return retMsg;
    }

    @Override
    protected RetMsg deleteHandle(ManageGenCode databaseEntity) {
        if(databaseEntity.getAttrs() != null) {
            databaseEntity.getAttrs().forEach(attr -> genAttrDao.delete(attr.getId()));
        }
        return super.deleteHandle(databaseEntity);
    }

    private String getDatabase() {
        String driverClassName = properties.getUrl();
        int beginIndex = driverClassName.lastIndexOf('/') + 1;
        int endIndex = driverClassName.lastIndexOf('?');
        return driverClassName.substring(beginIndex, endIndex == -1 ? driverClassName.length() : endIndex);
    }

    /**
     * 重写分页查询，原来find方法使用的sql id=query 会有分页错误的问题，因主查询对象不正确
     *
     * @param entity
     * @param pageable
     * @return
     */
    @Override
    public PageInfo<ManageGenCode> get(ManageGenCode entity, Pageable pageable) {
        PageInfo<ManageGenCode> pageInfo = new AntdPageInfo<>(manageGenCodeDao.find(entity, pageable), pageable);
        List<ManageGenCode> list = pageInfo.getData();
        // 补充attr信息
        list.forEach(manageGenCode -> {
            ManageGenAttr attr = new ManageGenAttr();
            attr.setCodeId(manageGenCode.getId());
            List<ManageGenAttr> attrs = genAttrDao.find(attr);
            manageGenCode.setAttrs(attrs);
        });
        pageInfo.setData(list);
        return pageInfo;
    }
}
