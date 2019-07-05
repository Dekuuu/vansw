package com.ego.item.service.impl;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.ego.dubbo.service.TbItemDubboService;
import com.ego.item.service.TbItemService;
import com.ego.pojo.TbItem;
import com.ego.redis.service.JedisDao;
import com.entity.TbItemChild;
import com.utils.JsonUtils;
@Service
public class TbItemServiceImpl implements TbItemService{

	@Reference
	private TbItemDubboService tbItemDubboServiceImpl;
	@Resource
	private JedisDao jedisDaoImpl;
	@Value("${redis.item.key}")
	private String redisItemKey;
	/*
	 * 根据id查询商品信息
	 */
	@Override
	public TbItemChild selById(long id) {
		String itemKey=redisItemKey+id;
		if(jedisDaoImpl.exists(itemKey)){
			return JsonUtils.jsonToPojo(jedisDaoImpl.get(itemKey),TbItemChild.class);
		}
		
		TbItem item=tbItemDubboServiceImpl.selById(id);
		TbItemChild child=new TbItemChild();
		child.setId(item.getId());
		child.setPrice(item.getPrice());
		child.setSellPoint(item.getSellPoint());
		child.setTitle(item.getTitle());
		String []images=item.getImage().split(",");
		child.setImages(images==null||images.length==0?new String[1]:images);
		
		jedisDaoImpl.set(itemKey, JsonUtils.objectToJson(child));
		
		return child;
	}

}
