package com.dubbo.service.impl;

import java.util.List;

import javax.annotation.Resource;

import com.ego.dubbo.service.TbContentDubboService;
import com.ego.mapper.TbContentMapper;
import com.ego.pojo.TbContent;
import com.ego.pojo.TbContentExample;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

public class TbContentDubboServiceImpl implements TbContentDubboService{
	@Resource
	private TbContentMapper tbContentMapper;
	/*
	 * 查询所有的内容信息
	 */
	@Override
	public List<TbContent> showAll(long id, int page, int rows) {
		PageHelper.startPage(page, rows);
		TbContentExample example=new TbContentExample();
		if(id!=0) {
//			id不等于0时查询对应CategoryId的内容信息
			example.createCriteria().andCategoryIdEqualTo(id);
		}
		return tbContentMapper.selectByExampleWithBLOBs(example);
	}
	
	/*
	 * 新增内容信息
	 */
	@Override
	public int insContent(TbContent tc) throws Exception {
		int ins = tbContentMapper.insertSelective(tc);
		if(ins>0) {
			return 1;
		}else {
			throw new Exception("添加失败！");
		}
	}

	/*
	 * 查询出前几个广告(mysql层面)
	 */
	@Override
	public List<TbContent> selByCount(int count, boolean ordered) {
		TbContentExample example=new TbContentExample();
		if(ordered) {
//			设置排序
			example.setOrderByClause("updated desc");
		}
		if(count>0) {
			PageHelper.startPage(1, count);
			List<TbContent> sel = tbContentMapper.selectByExampleWithBLOBs(example);
			PageInfo pi=new PageInfo(sel);
			return pi.getList();
		}else{
			return tbContentMapper.selectByExampleWithBLOBs(example);
		}
	}

	/*
	 * 修改内容信息
	 */
	@Override
	public int updById(TbContent tc) throws Exception {
		TbContentExample example=new TbContentExample();
		example.createCriteria().andIdEqualTo(tc.getId());
		int upd = tbContentMapper.updateByExampleWithBLOBs(tc, example);
		if(upd>0) {
			return 1;
		}else{
			throw new Exception("修改失败！");
		}
	}

	/*
	 * 根据id查询出对应的TbContent
	 */
	@Override
	public TbContent selById(long id) {
		return tbContentMapper.selectByPrimaryKey(id);
	}

	/*
	 * 根据id删除相应的TbContent
	 */
	@Override
	public int delById(long id) throws Exception {
		int del = tbContentMapper.deleteByPrimaryKey(id);
		if(del>0) {
			return 1;
		}else{
			throw new Exception("删除内容讯息失败！");
		}
	}

}
