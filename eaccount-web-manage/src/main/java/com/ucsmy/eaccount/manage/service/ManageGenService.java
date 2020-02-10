package com.ucsmy.eaccount.manage.service;

import com.ucsmy.core.service.BasicService;
import com.ucsmy.core.bean.ManageGenCode;
import com.ucsmy.eaccount.manage.ext.SelectInfo;

import java.util.List;

public interface ManageGenService extends BasicService<ManageGenCode> {
    List<SelectInfo> getAllTables();
}
