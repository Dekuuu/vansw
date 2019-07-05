package com.ego.manage.service;

import com.ego.pojo.TbItem;
import com.ego.pojo.TbItemParamItem;
import com.entity.EasyUIDataGrid;

public interface TbItemService {
	EasyUIDataGrid show(int page,int rows);
	
	int update(String ids,byte status);
	
	int insItem(TbItem item,String desc,String paramItem) throws Exception;
}
