package com.ego.manage.service;

import com.ego.pojo.TbContent;
import com.entity.EasyUIDataGrid;
import com.entity.EgoResult;

public interface TbContentService {
/*
 * 查询所有的内容信息
 */
	EasyUIDataGrid showAll(long catId,int page,int rows);
	
	/*
	 * 新增内容信息
	 */
	EgoResult insContent(TbContent tc) throws Exception;	
	
	/*
	 * 修改内容信息
	 */
	EgoResult updContent(TbContent tc) throws Exception;
	
	/*
	 * 根据id删除内容信息
	 */
	EgoResult delById(String ids) throws Exception;
}
