package com.dubbo.service.impl;

import java.util.List;

import javax.annotation.Resource;

import com.ego.dubbo.service.TbContentCatDubboService;
import com.ego.mapper.TbContentCategoryMapper;
import com.ego.mapper.TbContentMapper;
import com.ego.pojo.TbContentCategory;
import com.ego.pojo.TbContentCategoryExample;

public class TbContentCategoryDubboServiceImpl implements TbContentCatDubboService{
	@Resource
	private TbContentCategoryMapper tbContentCategoryMapper;
	/*
	 * 根据父id查询子内容类别
	 */
	@Override
	public List<TbContentCategory> selById(long id) {
		TbContentCategoryExample example=new TbContentCategoryExample();
		example.createCriteria().andParentIdEqualTo(id).andStatusEqualTo(1);
		List<TbContentCategory> list = tbContentCategoryMapper.selectByExample(example);
		return list;
	}
	
	/*
	 * 添加新的内容分类
	 */
	@Override
	public int insContentCat(TbContentCategory tcc) throws Exception{
		int insertSelective = tbContentCategoryMapper.insertSelective(tcc);
		if(insertSelective>0) {
			return 1;
		}else {
			throw new Exception("添加新的内容分类失败！");	
		}
	}

	/*
	 * 修改相关的状态
	 */
	@Override
	public int updContentCat(TbContentCategory tcc) throws Exception {
		int upd = tbContentCategoryMapper.updateByPrimaryKeySelective(tcc);
		if(upd>0) {
			return 1;
		}else {
			throw new Exception("修改节点状态失败！");	
		}
	}

	/*
	 * 根据id查找对应 的TbContentCategory
	 */
	@Override
	public TbContentCategory selTbContentCatById(long id) {
		return tbContentCategoryMapper.selectByPrimaryKey(id);
	}

	
	/*
	 * 根据id删除对应的TbContentCategory
	 */
	@Override
	public int delContentCat(TbContentCategory tcc) throws Exception {
		int del = tbContentCategoryMapper.updateByPrimaryKeySelective(tcc);
		if(del>0) {
			return 1;
		}else {
			throw new Exception("删除失败嗷！");
		}
	}

}
