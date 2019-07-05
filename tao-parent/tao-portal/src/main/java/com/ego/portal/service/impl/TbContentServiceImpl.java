package com.ego.portal.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.ego.dubbo.service.TbContentDubboService;
import com.ego.pojo.TbContent;
import com.ego.portal.service.TbContentService;
import com.ego.redis.service.JedisDao;
import com.utils.JsonUtils;

@Service
public class TbContentServiceImpl implements TbContentService{
	@Reference
	private TbContentDubboService  tbContentDubboServiceImpl;

	@Resource
	private JedisDao jedisDaoImpl;
	
	@Resource
	@Value("${redis.bigpic.key}")
	private String key;
	/*
	 * 展示前几个广告
	 */
	@Override
	public String showBigPic() {
//		先查看redis中是否含有缓存
		if(jedisDaoImpl.exists(key)) {
//			redis存在缓存
			String value=jedisDaoImpl.get(key);
			if(value!=null&&!value.equals("")) {
				return value;
			}
		}
//		redis中不存在缓存，则先查询mysql数据库，再把数据返回的同时存放到redis
		List<TbContent> selByCount = tbContentDubboServiceImpl.selByCount(6, true);
		List<Map<String,Object>> list=new ArrayList<Map<String,Object>>();
		for (TbContent tb : selByCount) {
			Map<String,Object> map=new HashMap<String, Object>();
			map.put("srcB", tb.getPic2());
			map.put("height", 240);
			map.put("alt", "对不起,加载图片失败");
			map.put("width", 670);
			map.put("src", tb.getPic());
			map.put("widthB", 550);
			map.put("href", tb.getUrl() );
			map.put("heightB", 240);
			list.add(map);
		}
		String str = JsonUtils.objectToJson(list);
		jedisDaoImpl.set(key, str);
		return str;
	}
	
}
