package com.ego.manage.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ego.manage.service.TbContentCatService;
import com.ego.pojo.TbContentCategory;
import com.entity.EasyUiTree;
import com.entity.EgoResult;

@Controller
public class TbContentCatController {
	@Resource
	private TbContentCatService tbContentCatServiceImpl;
	
	/*
	 * 查询内容
	 */
	@RequestMapping("content/category/list")
	@ResponseBody
	public List<EasyUiTree> showAllCat(@RequestParam(defaultValue = "0") long id) {
		return tbContentCatServiceImpl.selById(id);
	}
	
	/*
	 * 插入
	 */
	@RequestMapping("content/category/create")
	@ResponseBody
	EgoResult insContentCat(TbContentCategory cate) {
		EgoResult er=new EgoResult();
		try {
			er=tbContentCatServiceImpl.insContentCat(cate);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return er;
	}
	
	
	/*
	 * 修改
	 */
	@RequestMapping("content/category/update")
	@ResponseBody
	EgoResult updContentCat(TbContentCategory cate) {
		EgoResult er=new EgoResult();
		try {
			er=tbContentCatServiceImpl.updContentCat(cate);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return er;
	}
	
	/*
	 * 删除内容
	 */
	@RequestMapping("content/category/delete")
	@ResponseBody
	EgoResult delContentCat(TbContentCategory cate) {
		EgoResult er=new EgoResult();
		try {
			er=tbContentCatServiceImpl.delTbContentById(cate);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return er;
	}
}
