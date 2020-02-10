package com.ucsmy.eaccount.manage.service.impl;

import com.ucsmy.eaccount.manage.dao.ManageLogInfoMapper;
import com.ucsmy.core.bean.ManageLogInfo;
import com.ucsmy.eaccount.manage.service.ManageLogInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by ucs_gaokx on 2017/5/8.
 */
@Service
public class ManageLogInfoServiceImpl implements ManageLogInfoService {

    @Autowired
    private ManageLogInfoMapper manageLogInfoMapper;

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public void add(ManageLogInfo manageLogInfo) {
        manageLogInfoMapper.addManageLogInfo(manageLogInfo);
    }
}
