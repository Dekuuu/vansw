package com.ego.manage.controller;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ego.manage.service.TbContentService;
import com.ego.pojo.TbContent;
import com.entity.EasyUIDataGrid;
import com.entity.EgoResult;
@Controller
public class TbContentController {
	@Resource
	private TbContentService tbContentServiceImpl;
	/*
	 * 分页查询所有的内容
	 */
	@RequestMapping("content/query/list")
	@ResponseBody
	public EasyUIDataGrid show(long categoryId,int page,int rows) {
		return tbContentServiceImpl.showAll(categoryId, page, rows);
	}
	
	/*
	 * 新增内容信息
	 */
	@RequestMapping("content/save")
	@ResponseBody
	public EgoResult ins(TbContent tc) {
		EgoResult er=new EgoResult();
		try {
			er= tbContentServiceImpl.insContent(tc);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return er;
	}
	
	/*
	 * 修改内容信息
	 */
	@RequestMapping("rest/content/edit")
	@ResponseBody
	public EgoResult upd(TbContent tc) {
		EgoResult er=new EgoResult();
		try {
			er=tbContentServiceImpl.updContent(tc);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return er;
	}
	
	/*
	 * 根据id删除对应的TbContent
	 */
	@RequestMapping("content/delete")
	@ResponseBody
	public EgoResult del(String ids) {
		EgoResult er=new EgoResult();
		try {
			er=tbContentServiceImpl.delById(ids);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return er;
	}
}
