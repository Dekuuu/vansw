package com.ego.manage.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.ego.dubbo.service.TbContentDubboService;
import com.ego.manage.service.TbContentService;
import com.ego.pojo.TbContent;
import com.ego.redis.service.JedisDao;
import com.entity.EasyUIDataGrid;
import com.entity.EgoResult;
import com.github.pagehelper.PageInfo;
import com.utils.JsonUtils;
@Service
public class TbContentServiceImpl implements TbContentService{
	@Reference
	private TbContentDubboService tbContentDubboServiceImpl;
	@Resource
	@Value("${redis.bigpic.key}")
	private String key;
	@Resource
	private JedisDao jedisDaoImpl;
	/*
	 * 根据类目id查询所有的内容信息
	 */
	@Override
	public EasyUIDataGrid showAll(long catId, int page, int rows) {
		List<TbContent> showAll = tbContentDubboServiceImpl.showAll(catId, page, rows);
		PageInfo pi=new PageInfo(showAll);
		EasyUIDataGrid eu=new EasyUIDataGrid();
		eu.setRows(pi.getList());
		eu.setTotal(pi.getTotal());
		return eu;
	}
	
	/*
	 * 新增内容信息
	 */
	@Override
	public EgoResult insContent(TbContent tc) throws Exception{
		EgoResult er=new EgoResult();
		Date date=new Date();
		tc.setUpdated(date);
		tc.setCreated(date);
		int insContent = tbContentDubboServiceImpl.insContent(tc);
		if(insContent>0) {
//			实现数据同步(mysql和redis集群)
//			判断redis中是否含有该内容，如果有则获取并更新信息；如果没有。则将该数据放进redis
			if(jedisDaoImpl.exists(key)) {
				String value = jedisDaoImpl.get(key);
				List<Map> listMap = JsonUtils.jsonToList(value, Map.class);
				Map<String,Object> map=new HashMap<String, Object>();
				map.put("srcB", tc.getPic2());
				map.put("height", 240);
				map.put("alt", "对不起,加载图片失败");
				map.put("width", 670);
				map.put("src", tc.getPic());
				map.put("widthB", 550);
				map.put("href", tc.getUrl() );
				map.put("heightB", 240);
				listMap.add(0, map);
				String objectToJson = JsonUtils.objectToJson(listMap);
				jedisDaoImpl.set(key, objectToJson);
			}else {
				List<Map> listMap=new ArrayList<Map>();
				Map<String,Object> map=new HashMap<String, Object>();
				map.put("srcB", tc.getPic2());
				map.put("height", 240);
				map.put("alt", "对不起,加载图片失败");
				map.put("width", 670);
				map.put("src", tc.getPic());
				map.put("widthB", 550);
				map.put("href", tc.getUrl() );
				map.put("heightB", 240);
				listMap.add(0, map);
				String objectToJson = JsonUtils.objectToJson(listMap);
				jedisDaoImpl.set(key, objectToJson);
			}
			er.setStatus(200);
		}else{
			er.setData("添加内容失败！");
		}
		return er;
	}

	/*
	 * 修改内容信息，实现数据同步
	 */
	@Override
	public EgoResult updContent(TbContent tc) throws Exception {
		EgoResult er=new EgoResult();
//		先根据id查到之前的TbContent，完善参数的信息
		TbContent selById = tbContentDubboServiceImpl.selById(tc.getId());
		Date date=new Date();
		tc.setUpdated(date);
		tc.setCategoryId(selById.getCategoryId());
		tc.setCreated(selById.getCreated());
		int upd = tbContentDubboServiceImpl.updById(tc);
		if(upd>0) {
//			修改数据库成功，同步redis：先查出添加完后的集合，再将集合重新遍历一次，重新赋予redis集合新的元素
			List<TbContent> selByCount = tbContentDubboServiceImpl.selByCount(6, true);
			List<Map<String,Object>> listMap=new ArrayList<Map<String,Object>>();
			for (TbContent tbContent : selByCount) {
				Map<String,Object> map=new HashMap<String, Object>();
				map.put("srcB", tbContent.getPic2());
				map.put("height", 240);
				map.put("alt", "对不起,加载图片失败");
				map.put("width", 670);
				map.put("src", tbContent.getPic());
				map.put("widthB", 550);
				map.put("href", tbContent.getUrl() );
				map.put("heightB", 240);
				listMap.add(map);
			}
			String value = JsonUtils.objectToJson(listMap);
			jedisDaoImpl.set(key, value);
			er.setStatus(200);
		}else {
			er.setData("修改内容失败!");
		}
		return er;
	}

	/*
	 * 根据id删除相应的TbContent
	 */
	@Override
	public EgoResult delById(String ids) throws Exception {
		EgoResult er=new EgoResult();
		String []idArr=ids.split(",");
		int index=0;
		for (String string : idArr) {
			index+= tbContentDubboServiceImpl.delById(Long.parseLong(string));
		}
		if(index==idArr.length) {
//			删除成功，同步redis的信息，同样也是先获取删除完后的表信息，再将表信息重新输入到redis集合中（类型为Map）
			List<TbContent> selByCount = tbContentDubboServiceImpl.selByCount(6, true);
			List<Map<String,Object>> listMap=new ArrayList<Map<String,Object>>();
			for (TbContent tbContent : selByCount) {
				Map<String,Object> map=new HashMap<String, Object>();
				map.put("srcB", tbContent.getPic2());
				map.put("height", 240);
				map.put("alt", "对不起,加载图片失败");
				map.put("width", 670);
				map.put("src", tbContent.getPic());
				map.put("widthB", 550);
				map.put("href", tbContent.getUrl() );
				map.put("heightB", 240);
				listMap.add(map);
			}
			String value = JsonUtils.objectToJson(listMap);
			jedisDaoImpl.set(key, value);
			er.setStatus(200);
		}else {
			er.setData("删除失败！");
		}
		return er;
	}

}
