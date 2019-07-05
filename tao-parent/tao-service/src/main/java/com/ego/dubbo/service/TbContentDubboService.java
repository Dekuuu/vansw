package com.ego.dubbo.service;

import java.util.List;

import com.ego.pojo.TbContent;

public interface TbContentDubboService {
	/*
	 * 查询所有的内容信息，后台管理
	 */
	List<TbContent> showAll(long id,int page,int rows);
	
	/*
	 * 新增新的内容信息，后台管理
	 */
	int insContent(TbContent tc) throws Exception;
	
	/*
	 * 查询出前几个的广告(mysql层面)，前台显示
	 */
	List<TbContent> selByCount(int count,boolean ordered);
	
	/*
	 * 修改内容信息
	 */
	int updById(TbContent tc) throws Exception;
	
	/*
	 * 根据id查询出对应的TbContent
	 */
	TbContent selById(long id);
	
	/*
	 * 根据id删除相应的TbContent
	 */
	int delById(long id) throws Exception;
}
