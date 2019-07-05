package com.ego.manage.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.ego.dubbo.service.TbItemDubboService;
import com.ego.manage.service.TbItemService;
import com.ego.pojo.TbItem;
import com.ego.pojo.TbItemDesc;
import com.ego.pojo.TbItemParamItem;
import com.entity.EasyUIDataGrid;
import com.utils.HttpClientUtil;
import com.utils.IDUtils;
import com.utils.JsonUtils;
@Service
public class TbItemServiceImpl implements TbItemService{
	@Reference
	TbItemDubboService tbItemDubboServiceImpl;
	
	/*
	 * 分页显示商品信息
	 */
	@Override
	public EasyUIDataGrid show(int page, int rows) {
		EasyUIDataGrid show = tbItemDubboServiceImpl.show(page, rows);
		return show;
	}
	
	/*
	 * 修改商品的状态
	 * 还要修改redis缓存
	 * 修改solr，若是修改商品的信息，则类似于新增；若是删除，则删除对应的id的solr数据
	 */
	@Override
	public int update(String ids, byte status) {
		int index = 0 ;
		TbItem item = new TbItem();
//		考虑到可能一次性操作多个数据的情况，所以对传进来的id进行切割分组
		String[] idsStr = ids.split(",");
		for (String id : idsStr) {
			item.setId(Long.parseLong(id));
			item.setStatus(status);
			index +=tbItemDubboServiceImpl.updItemStatus(item);
		}
//		如果全部都修改成功才返回1，否则返回0
		if(index==idsStr.length){
			return 1;
		}
		return 0;
	}
	
	/*
	 * 新增商品，包括商品的信息、商品描述、商品的参数规格
	 */
	@Override
	public int insItem(TbItem item, String desc,String itemParams) throws Exception {
		long id=IDUtils.genItemId();
		item.setId(id);
		item.setStatus((byte)1);
		Date date=new Date();
		item.setCreated(date);
		item.setUpdated(date);
		
		TbItemDesc itemDesc=new TbItemDesc();
		itemDesc.setCreated(date);
		itemDesc.setUpdated(date);
		itemDesc.setItemDesc(desc);
		itemDesc.setItemId(id);
		
		TbItemParamItem paramItem=new TbItemParamItem();
		paramItem.setCreated(date);
		paramItem.setItemId(id);
		paramItem.setUpdated(date);
		paramItem.setParamData(itemParams);
		int index=tbItemDubboServiceImpl.insItem(item, itemDesc, paramItem);
		
//		新建一个子线程负责数据的同步，提高前台的效率
		new Thread() {
			public void run() {
//				将数据同步到solr中
				Map<String,Object> map=new HashMap<String,Object>();
				map.put("item",item);
				map.put("desc", desc);			
//				以json数据方式传输到另一个项目的控制器
				String doPost = HttpClientUtil.doPostJson("http://localhost:8083/solr/add",JsonUtils.objectToJson(map));
			}
		}.start();

		return index;
	}

}
