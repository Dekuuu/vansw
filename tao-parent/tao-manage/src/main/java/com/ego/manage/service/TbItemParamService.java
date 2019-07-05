package com.ego.manage.service;

import com.ego.pojo.TbItemParam;
import com.entity.EasyUIDataGrid;

public interface TbItemParamService {
	EasyUIDataGrid showByPage(int page,int rows);
	
	int delByIds(String ids) throws Exception;
	
	int insParam(TbItemParam param);
	
	TbItemParam showById(long id);
}
