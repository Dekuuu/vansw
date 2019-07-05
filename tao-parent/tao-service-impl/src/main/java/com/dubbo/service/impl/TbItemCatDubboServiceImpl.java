package com.dubbo.service.impl;

import java.util.List;

import javax.annotation.Resource;

import com.ego.dubbo.service.TbItemCatDubboService;
import com.ego.mapper.TbItemCatMapper;
import com.ego.pojo.TbItemCat;
import com.ego.pojo.TbItemCatExample;

public class TbItemCatDubboServiceImpl implements TbItemCatDubboService{
	@Resource 
	TbItemCatMapper tbItemCatMapper;
	/*
	 * 根据父id查询类目
	 */
	@Override
	public TbItemCat selById(long pid) {
		// TODO Auto-generated method stub
		return tbItemCatMapper.selectByPrimaryKey(pid);
	}
	@Override
	public List<TbItemCat> selectAll(long pid) {
		TbItemCatExample example=new TbItemCatExample();
		example.createCriteria().andParentIdEqualTo(pid);
		return tbItemCatMapper.selectByExample(example);
	}

	
}
