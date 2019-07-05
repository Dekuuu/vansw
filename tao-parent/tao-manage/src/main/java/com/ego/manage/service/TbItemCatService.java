package com.ego.manage.service;

import java.util.List;

import com.entity.EasyUiTree;

public interface TbItemCatService {
//	根据父类目id查找类型
	public List<EasyUiTree> show(long pid);
}
