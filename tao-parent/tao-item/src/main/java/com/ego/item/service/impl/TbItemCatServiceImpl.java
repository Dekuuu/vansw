package com.ego.item.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.ego.dubbo.service.TbItemCatDubboService;
import com.ego.item.pojo.PortalMenu;
import com.ego.item.pojo.PortalMenuNode;
import com.ego.item.service.TbItemCatService;
import com.ego.pojo.TbItemCat;
@Service
public class TbItemCatServiceImpl implements TbItemCatService{
	@Reference
	private TbItemCatDubboService tbItemCatDubboServiceImpl;
	/*
	 * 查询一级菜单的类型
	 */
	@Override
	public PortalMenu showCatMenu() {
		List<TbItemCat> show = tbItemCatDubboServiceImpl.selectAll(0);
		PortalMenu pw=new PortalMenu();
		pw.setData(showAllMenu(show));
		return pw;
	}
	
	/*
	 * 查询所有的菜单
	 */
	public List<Object> showAllMenu(List<TbItemCat> list){
		List<Object> listAll=new ArrayList<Object>();
		for (TbItemCat tbItemCat : list) {
			if(tbItemCat.getIsParent()) {
//				是父菜单则设置前台接受数据的uni
				PortalMenuNode pmn=new PortalMenuNode();
				pmn.setU("/products/"+tbItemCat.getId()+".html");
				pmn.setN("<a href='/products/"+tbItemCat.getId()+".html'>"+tbItemCat.getName()+"</a>");
//				由于是父项目，所以i要继续往下查询
				pmn.setI(showAllMenu(tbItemCatDubboServiceImpl.selectAll(tbItemCat.getId())));
				listAll.add(pmn);
			}else {
//				是子菜单
				listAll.add("/products/"+tbItemCat.getId()+".html|"+tbItemCat.getName());
			}
		}
		return listAll;
	}
}
