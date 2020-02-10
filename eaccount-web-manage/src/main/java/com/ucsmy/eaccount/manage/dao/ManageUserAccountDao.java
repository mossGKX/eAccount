package com.ucsmy.eaccount.manage.dao;

import com.ucsmy.core.dao.BasicDao;
import com.ucsmy.eaccount.manage.entity.ManageUserAccount;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ManageUserAccountDao extends BasicDao<ManageUserAccount> {

	int updatePassword(ManageUserAccount account);

	int updateStatus(@Param("id") String id);

	ManageUserAccount findByUserName(@Param("userName") String userName);

}
