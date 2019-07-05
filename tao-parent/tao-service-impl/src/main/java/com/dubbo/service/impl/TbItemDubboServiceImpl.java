package com.dubbo.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.ego.dubbo.service.TbItemDubboService;
import com.ego.mapper.TbItemDescMapper;
import com.ego.mapper.TbItemMapper;
import com.ego.mapper.TbItemParamItemMapper;
import com.ego.pojo.TbItem;
import com.ego.pojo.TbItemDesc;
import com.ego.pojo.TbItemExample;
import com.ego.pojo.TbItemParamItem;
import com.entity.EasyUIDataGrid;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
@Service
public class TbItemDubboServiceImpl implements TbItemDubboService{
	@Resource
	private TbItemMapper tbItemMapper;
	@Resource
	private TbItemDescMapper tbItemDescMapper;
	@Resource
	private TbItemParamItemMapper tbItemParamItemMapper;
	/*
	 * 分页显示
	 */
	@Override
	public EasyUIDataGrid show(int page, int rows) {
//		设置分页属性，要在查询之前设置，实现的原理是在sql语句中加入limit(page,rows)
		PageHelper.startPage(page, rows);
		List<TbItem> selectByExample = tbItemMapper.selectByExample(new TbItemExample());
		PageInfo<TbItem> pi = new PageInfo<>(selectByExample);
//		放入到实体类
		EasyUIDataGrid datagrid = new EasyUIDataGrid();
		datagrid.setRows(pi.getList());
		datagrid.setTotal(pi.getTotal());
		return datagrid;
	}
	
	/*
	 * 修改商品状态
	 */
	@Override
	public int updItemStatus(TbItem tbItem) {
		return tbItemMapper.updateByPrimaryKeySelective(tbItem);
	}

	/*
	 * 新增商品信息
	 */
	@Override
	public int insItem(TbItem tbItem, TbItemDesc desc,TbItemParamItem paramItem) throws Exception {
		int index=0;
		try {
			index+=tbItemMapper.insertSelective(tbItem);
			index+=tbItemDescMapper.insertSelective(desc);
			index+=tbItemParamItemMapper.insertSelective(paramItem);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(index==3) {
			return 1;
		}else {
			throw new Exception("新增数据错误!");
		}
	}

	/*
	 * 查询出所有的上线商品
	 */
	@Override
	public List<TbItem> showAll() {
		TbItemExample example=new TbItemExample();
		example.createCriteria().andStatusEqualTo((byte)1);
		return tbItemMapper.selectByExample(example);
	}

	/*
	 * 根据id查询出商品信息
	 */
	@Override
	public TbItem selById(long id) {
		return tbItemMapper.selectByPrimaryKey(id);
	}

}
