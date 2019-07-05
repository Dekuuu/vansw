package com.ego.dubbo.service;

import com.ego.pojo.TbItemParam;
import com.entity.EasyUIDataGrid;

public interface TbItemParamDubboService {
	EasyUIDataGrid show(int page,int rows);
	
	int delByIds(String ids) throws Exception;
	
	int insParam(TbItemParam param);
	
	TbItemParam showByCatId(long catId);
}
