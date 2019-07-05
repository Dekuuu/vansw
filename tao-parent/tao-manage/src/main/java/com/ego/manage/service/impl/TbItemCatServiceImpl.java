package com.ego.manage.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.ego.dubbo.service.TbItemCatDubboService;
import com.ego.manage.service.TbItemCatService;
import com.ego.pojo.TbItemCat;
import com.entity.EasyUiTree;
@Service
public class TbItemCatServiceImpl implements TbItemCatService{
@Reference
private TbItemCatDubboService tbItemCatDubboService;
	@Override
	public List<EasyUiTree> show(long pid) {
		List<TbItemCat> show = tbItemCatDubboService.selectAll(pid);
		List<EasyUiTree> easyUiTree = new ArrayList<EasyUiTree>();
		for (TbItemCat tbItemCat : show) {
			EasyUiTree et=new EasyUiTree();
			et.setId(tbItemCat.getId());
			et.setText(tbItemCat.getName());
			et.setState(tbItemCat.getIsParent()?"closed":"open");
			easyUiTree.add(et);
		}
		return easyUiTree;
	}

}
