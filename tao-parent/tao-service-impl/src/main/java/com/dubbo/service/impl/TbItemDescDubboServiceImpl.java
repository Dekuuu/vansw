package com.dubbo.service.impl;

import javax.annotation.Resource;

import com.ego.dubbo.service.TbItemDescDubboService;
import com.ego.mapper.TbItemDescMapper;
import com.ego.pojo.TbItemDesc;

public class TbItemDescDubboServiceImpl implements TbItemDescDubboService{
	@Resource
	private TbItemDescMapper tbItemDescMapper;
	/*
	 * 根据id查询出对应的商品描述
	 */
	@Override
	public TbItemDesc selBy(long id) {
		return tbItemDescMapper.selectByPrimaryKey(id);
	}

}
