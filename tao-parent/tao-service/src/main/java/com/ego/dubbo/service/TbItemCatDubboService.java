package com.ego.dubbo.service;

import java.util.List;

import com.ego.pojo.TbItemCat;

public interface TbItemCatDubboService {
//	根据父类目id查找类型
	public TbItemCat selById(long pid);
	
	
	public List<TbItemCat> selectAll(long pid);
}
