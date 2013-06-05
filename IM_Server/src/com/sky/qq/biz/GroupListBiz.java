package com.sky.qq.biz;

import com.sky.qq.annotation.Autowired;
import com.sky.qq.annotation.Service;
import com.sky.qq.dao.GroupListDao;

@Service
public class GroupListBiz {
	@Autowired
	private GroupListDao groupListDao;
	
}
