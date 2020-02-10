package com.ucsmy.core.service;

import com.ucsmy.core.bean.BaseNode;
import com.ucsmy.core.dao.BasicDao;
import com.ucsmy.core.tool.interceptor.domain.PageInfo;
import com.ucsmy.core.tool.interceptor.domain.Pageable;
import com.ucsmy.core.utils.BeanUtil;
import com.ucsmy.core.utils.StringUtils;
import com.ucsmy.core.vo.RetMsg;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.ucsmy.core.utils.HibernateValidateUtils.getError;

@Transactional(readOnly = true)
public class BasicServiceImpl<E extends BaseNode, D extends BasicDao<E>> implements BasicService<E> {
    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    protected D dao;

    @Transactional
    public RetMsg save(E entity) {
        RetMsg retMsg = getError(entity);
        if(retMsg.isError()) {
            return retMsg;
        }
        retMsg = this.saveHandle(entity);
        if(retMsg.isError()) {
            return retMsg;
        }
        int i = dao.save(entity);
        if(i == 0) {
            return RetMsg.error("保存失败");
        }
        return RetMsg.success("保存成功");
    }

    protected RetMsg saveHandle(E entity) {
        if(StringUtils.isEmpty(entity.getId())) {
            entity.setId(StringUtils.getUUID());
        }
        return RetMsg.success();
    }

    @Transactional
    public RetMsg update(E entity) {
        if (StringUtils.isEmpty(entity.getId())) {
            return RetMsg.error("记录Id不能为空");
        }
        E databaseEntity = this.getById(entity.getId());
        if (databaseEntity == null) {
            return RetMsg.error("记录不存在");
        }
        if (entity.getId() == null) {
            return RetMsg.error("记录Id不能为空");
        }
        RetMsg retMsg = getError(entity);
        if(retMsg.isError()) {
            return retMsg;
        }
        RetMsg<E> handleMsg = this.updateHandle(entity, databaseEntity);
        if(handleMsg.isError()) {
            return handleMsg;
        }
        int i = dao.update(handleMsg.getData());
        if(i == 0) {
            return RetMsg.error("修改失败");
        }
        return RetMsg.success("修改成功");
    }

    protected RetMsg updateHandle(E pageEntity, E databaseEntity) {
        BeanUtil.copyPropertiesIgnoreNull(pageEntity, databaseEntity);
        return RetMsg.success(databaseEntity);
    }

    @Transactional
    public RetMsg delete(String id) {
        if (StringUtils.isEmpty(id)) {
            return RetMsg.error("记录Id不能为空");
        }
        E databaseEntity = this.getById(id);
        if (databaseEntity == null) {
            return RetMsg.error("记录不存在");
        }
        RetMsg retMsg = this.deleteHandle(databaseEntity);
        if(retMsg.isError()) {
            return retMsg;
        }
        int i = dao.delete(id);
        if(i == 0) {
            return RetMsg.error("删除失败");
        }
        return RetMsg.success("删除成功");
    }

    protected RetMsg deleteHandle(E databaseEntity) {
        return RetMsg.success();
    }

    @Override
    public E getById(String id) {
        return dao.findById(id);
    }

    @Override
    @com.ucsmy.core.tool.log.annotation.Logger(printSQL = true)
    public List<E> get() {
        return dao.find();
    }

    @Override
    public List<E> get(E entity) {
        return dao.find(entity);
    }

    @Override
    public PageInfo<E> get(E entity, Pageable pageable) {
        return dao.find(entity, pageable);
    }
}
