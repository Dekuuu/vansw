package com.ego.dubbo.service;

import java.util.List;

import com.ego.pojo.TbContentCategory;

public interface TbContentCatDubboService {
	/*
	 * 根据id查询内容分类菜单
	 */
	List<TbContentCategory> selById(long id);
	
	/*
	 * 插入新的内容分类
	 */
	int insContentCat(TbContentCategory tcc) throws Exception;
	
	/*
	 * 根据id修改状态
	 * 
	 */
	int updContentCat(TbContentCategory tcc) throws Exception;
	
	/*
	 * 根据id查找对应的TbItemContentCat
	 */
	TbContentCategory selTbContentCatById(long id);
	
	/*
	 * 根据id删除对应的TbItemContentCat(只是逻辑上的删除)
	 */
	int delContentCat(TbContentCategory tcc) throws Exception;
}
