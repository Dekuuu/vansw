package com.ego.manage.service;

import java.util.List;

import com.ego.pojo.TbContentCategory;
import com.entity.EasyUiTree;
import com.entity.EgoResult;

public interface TbContentCatService {
	/*
	 * 根据id查询内容分类菜单
	 */
	List<EasyUiTree> selById(long id);
	
	/*
	 * 新增内容分类
	 */
	EgoResult insContentCat(TbContentCategory tbContentCategory) throws Exception;
	
	/*
	 * 根据id修改状态
	 * 
	 */
	EgoResult updContentCat(TbContentCategory tcc) throws Exception;
	
	/*
	 * 根据id查找对应的TbContentCategory
	 */
	TbContentCategory selTbContentCatById(long id);
	
	/*
	 * 根据id删除对应的TbContentCategory
	 */
	EgoResult delTbContentById(TbContentCategory tbcc) throws Exception;
}
