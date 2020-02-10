package com.ucsmy.eaccount;

import com.ucsmy.eaccount.manage.service.ManageResourcesService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class EAccountManageApplicationTests {

	@Autowired
	private ManageResourcesService service;

	@Test
	public void test() {
		//resourcesDao.findById("1");
		service.getById("1");
	}

}
