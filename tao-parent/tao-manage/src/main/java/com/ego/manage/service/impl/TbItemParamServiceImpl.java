package com.ego.manage.service.impl;

import org.springframework.stereotype.Service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.ego.dubbo.service.TbItemParamDubboService;
import com.ego.manage.service.TbItemParamService;
import com.ego.pojo.TbItemParam;
import com.entity.EasyUIDataGrid;
@Service
public class TbItemParamServiceImpl implements TbItemParamService{
@Reference
private TbItemParamDubboService tbItemParamDubboService;
//	分页显示参数规格信息
	@Override
	public EasyUIDataGrid showByPage(int page, int rows) {
		// TODO Auto-generated method stub
		return tbItemParamDubboService.show(page, rows);
	}
	
	/*
	 * 根据id删除参数规格信息
	 */
	@Override
	public int delByIds(String ids) throws Exception {
		return tbItemParamDubboService.delByIds(ids);
	}
	
//	增加参数规格信息
	@Override
	public int insParam(TbItemParam param) {
		return tbItemParamDubboService.insParam(param);
	}

	/*
	 * 根据Catid查找规格参数，从而判断改规格参数是否存在；
	 * 若已存在则前台提示已存在；
	 * 不存在则正常流程添加
	 */
	@Override
	public TbItemParam showById(long id) {
		return tbItemParamDubboService.showByCatId(id);
	}

}
