package com.ego.item.service.impl;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.ego.dubbo.service.TbItemDescDubboService;
import com.ego.item.service.TbItemDescService;
import com.ego.pojo.TbItemDesc;
import com.ego.redis.service.JedisDao;
@Service
public class TbItemDescServiceImpl implements TbItemDescService{
	@Reference
	private TbItemDescDubboService tbItemDescDubboServiceImpl;
	@Value("${redis.item.desc}")
	private String redisItemDescKey;
	@Resource
	private JedisDao jedisDaoImpl;
	/*
	 * 根据id查询商品描述
	 */
	@Override
	public String selDescById(long id) {
		String descKey=redisItemDescKey+id;
//		先从redis中查询
		if(jedisDaoImpl.exists(descKey)){
			return jedisDaoImpl.get(descKey);
		}
		TbItemDesc desc=tbItemDescDubboServiceImpl.selBy(id);
		jedisDaoImpl.set(descKey, desc.getItemDesc());
		return desc.getItemDesc();
	}

}
