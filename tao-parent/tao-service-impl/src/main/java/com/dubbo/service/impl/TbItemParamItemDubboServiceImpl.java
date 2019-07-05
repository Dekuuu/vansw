package com.dubbo.service.impl;

import javax.annotation.Resource;

import com.ego.dubbo.service.TbItemParamItemDubboService;
import com.ego.mapper.TbItemParamItemMapper;
import com.ego.pojo.TbItemParamItem;
import com.ego.pojo.TbItemParamItemExample;

public class TbItemParamItemDubboServiceImpl implements TbItemParamItemDubboService{
	@Resource
	private TbItemParamItemMapper tbItemParamItemMapperImpl;
	/*
	 * 根据商品id查询商品信息
	 */
	@Override
	public TbItemParamItem selById(long id) {
		TbItemParamItemExample example=new TbItemParamItemExample();
		example.createCriteria().andItemIdEqualTo(id);
		return tbItemParamItemMapperImpl.selectByExampleWithBLOBs(example).get(0);
	}

}
