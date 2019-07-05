package com.dubbo.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import com.ego.dubbo.service.TbItemParamDubboService;
import com.ego.mapper.TbItemCatMapper;
import com.ego.mapper.TbItemParamMapper;
import com.ego.pojo.TbItemParam;
import com.ego.pojo.TbItemParamChild;
import com.ego.pojo.TbItemParamExample;
import com.entity.EasyUIDataGrid;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

public class TbItemParamDubboServiceImpl implements TbItemParamDubboService{
	@Resource
	private TbItemParamMapper tbItemParamMapper;
	@Resource
	private TbItemCatMapper tbItemCatMapper;
//	显示规格参数信息
	@Override
	public EasyUIDataGrid show(int page, int rows) {
		PageHelper.startPage(page, rows);
//		选择xxxWithBLOBs方法可以查询数据库表中值为text的属性列
		List<TbItemParam> selectByExampleWithBLOBs = tbItemParamMapper.selectByExampleWithBLOBs(new TbItemParamExample());
		EasyUIDataGrid eu=new EasyUIDataGrid();
		List<TbItemParamChild> list=new ArrayList<TbItemParamChild>();
		for (TbItemParam tbItemParam : selectByExampleWithBLOBs) {
			TbItemParamChild child=new TbItemParamChild();
			child.setCreated(tbItemParam.getCreated());
			child.setId(tbItemParam.getId());
			child.setItemCatId(tbItemParam.getItemCatId());
			child.setItemCatName(tbItemCatMapper.selectByPrimaryKey(tbItemParam.getItemCatId()).getName());
			child.setParamData(tbItemParam.getParamData());
			child.setUpdated(tbItemParam.getUpdated());
			list.add(child);
		}
		
		
		PageInfo<TbItemParamChild> pi=new PageInfo<TbItemParamChild>(list);
		eu.setRows(pi.getList());
		eu.setTotal(pi.getTotal());
		return eu;
	}
	
//	根据id删除规格参数信息
	@Override
	public int delByIds(String ids) throws Exception {
		String []idStr=ids.split(",");
		int index=0;
		for (String string : idStr) {
			index+=tbItemParamMapper.deleteByPrimaryKey(Long.parseLong(string));
		}
		if(index==idStr.length) {
			return 1;
		}else {
			throw new Exception("删除错误！可能原因：在你删除数据之前数据已经被删除了");
		}
	}

//	增加参数规格信息
	@Override
	public int insParam(TbItemParam param) {
		return tbItemParamMapper.insertSelective(param);
	}

	
	/*
	 * 根据类别目录id查找参数规格
	 */
	@Override
	public TbItemParam showByCatId(long catId) {
		TbItemParamExample example=new TbItemParamExample();
		example.createCriteria().andItemCatIdEqualTo(catId);
		List<TbItemParam> list = tbItemParamMapper.selectByExampleWithBLOBs(example);
		if(list!=null&&list.size()>0) {
			return list.get(0);
		}
		return null;
	}

}
