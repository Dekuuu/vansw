package com.ego.manage.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.ego.dubbo.service.TbContentCatDubboService;
import com.ego.manage.service.TbContentCatService;
import com.ego.pojo.TbContentCategory;
import com.entity.EasyUiTree;
import com.entity.EgoResult;
import com.utils.IDUtils;

@Service
public class TbContentCatServiceImpl implements TbContentCatService{
	@Reference
	private TbContentCatDubboService tbContentCatDubboServiceImpl;
	/*
	 * 根据id查询内容菜单
	 */
	@Override
	public  List<EasyUiTree> selById(long id) {
		List<EasyUiTree> trees=new ArrayList<EasyUiTree>();
		List<TbContentCategory> selById = tbContentCatDubboServiceImpl.selById(id);
		for (TbContentCategory tbContentCategory : selById) {
			EasyUiTree eut=new EasyUiTree();
			eut.setId(tbContentCategory.getId());
			eut.setState(tbContentCategory.getIsParent()?"closed":"open");
			eut.setText(tbContentCategory.getName());
			trees.add(eut);
		}
		return trees;
	}


	/*
	 * 新增内容分类
	 */
	@Override
	public EgoResult insContentCat(TbContentCategory tbContentCategory) throws Exception {
		EgoResult er=new EgoResult();
		//			先查询是否含有相同名字的结点
		List<TbContentCategory> selById = tbContentCatDubboServiceImpl.selById(tbContentCategory.getParentId());
		for (TbContentCategory temp : selById) {
			if(temp.getName().equals(tbContentCategory.getName())){
				er.setData("新增失败，含有相同的元素！");
				return er;
			}
		}
		//			当前目录下不含有相同名字的结点，则弥补数据并添加到数据库
		Date date=new Date();
		tbContentCategory.setCreated(date);
		tbContentCategory.setId(IDUtils.genItemId());
		tbContentCategory.setUpdated(date);
		tbContentCategory.setStatus(1);
		tbContentCategory.setIsParent(false);
		tbContentCategory.setSortOrder(1);

		int insContentCat = tbContentCatDubboServiceImpl.insContentCat(tbContentCategory);
		if(insContentCat>0) {
			//				添加成功
			//				修改父节点的状态
			TbContentCategory parent=new TbContentCategory();
			parent.setIsParent(true);
			parent.setId(tbContentCategory.getParentId());
			int updParent = tbContentCatDubboServiceImpl.updContentCat(parent);
			if(updParent>0) {
				//					父节点修改成功
				er.setStatus(200);
				er.setData(tbContentCategory);
			}else {
				er.setData("父节点状态修改失败！");
			}
		}
		return er;
	}

	/*
	 * 修改状态
	 */
	@Override
	public EgoResult updContentCat(TbContentCategory tcc) throws Exception {
		EgoResult er=new EgoResult();
		//		先根据id查询出当前结点的详细信息
		TbContentCategory sel = tbContentCatDubboServiceImpl.selTbContentCatById(tcc.getId());
		//		找出当前结点的父节点的所有结点信息
		TbContentCategory parent=tbContentCatDubboServiceImpl.selTbContentCatById(sel.getParentId());
		List<TbContentCategory> nodes = tbContentCatDubboServiceImpl.selById(parent.getId());
		//		遍历链表查找是否含有与当前结点内容相同的
		for (TbContentCategory tbContentCategory : nodes) {
			if(tbContentCategory.getName().equals(tcc.getName())) {
				er.setData("修改失败！含有相同的内容");
				return er;
			}
		}
		//		没有相同的内容
		int updContentCat = tbContentCatDubboServiceImpl.updContentCat(tcc);
		if(updContentCat>0) {
			er.setStatus(200);
		}else {
			er.setData("修改失败！！");
		}
		return er;
	}

	/*
	 * 根据id查询对应的TbContentCategory
	 */
	@Override
	public TbContentCategory selTbContentCatById(long id) {
		return tbContentCatDubboServiceImpl.selTbContentCatById(id);
	}

	/*
	 * 根据id删除对应的TbContentCategory
	 */
	@Override
	public EgoResult delTbContentById(TbContentCategory tbcc) throws Exception {
		EgoResult er=new EgoResult();
		TbContentCategory child=tbContentCatDubboServiceImpl.selTbContentCatById(tbcc.getId());
		child.setStatus(0);
		int del = tbContentCatDubboServiceImpl.delContentCat(child);
		TbContentCategory parent = tbContentCatDubboServiceImpl.selTbContentCatById(child.getParentId());
		if(del>0) {
			//			删除成功
			//			查找父节点的子节点集合
			List<TbContentCategory> nodes = tbContentCatDubboServiceImpl.selById(child.getParentId());
			if(nodes.size()==0) {
//				修改父节点的状态
				parent.setIsParent(false);
				int updParent = tbContentCatDubboServiceImpl.updContentCat(parent);
				if(updParent>0) {
					er.setStatus(200);
				}else {
					er.setData("删除失败!原因：父节点状态修改失败");
				}
			}else {
//				删除完后子节点数大于0，不用修改父节点的信息
				er.setStatus(200);
			}
		}else {
			er.setData("删除失败");
		}
		return er;

	}
}
