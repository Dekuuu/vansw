package com.ego.dubbo.service;

import java.util.List;

import com.ego.pojo.TbItem;
import com.ego.pojo.TbItemDesc;
import com.ego.pojo.TbItemParamItem;
import com.entity.EasyUIDataGrid;

public interface TbItemDubboService {
//	分页查询商品信息
	EasyUIDataGrid show(int page,int rows);
	
//	根据ID修改状态
	int updItemStatus(TbItem tbItem);
	
//新增商品信息
	int insItem(TbItem tbItem,TbItemDesc desc,TbItemParamItem tbItemParamItem) throws Exception;
	
	/*
	 * 查询出所有的商品
	 */
	List<TbItem> showAll();
	
	/*
	 * 根据id查询出商品信息
	 */
	TbItem selById(long id);
}
