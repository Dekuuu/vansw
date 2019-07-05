package com.ego.manage.controller;

import java.util.Date;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ego.manage.service.TbItemParamService;
import com.ego.pojo.TbItemParam;
import com.entity.EasyUIDataGrid;
import com.entity.EgoResult;

@Controller
public class TbItemParamController {
	@Resource
	private TbItemParamService tbItemParamServiceImpl;
	
//	分页展示参数规格
	@RequestMapping("item/param/list")
	@ResponseBody
	public EasyUIDataGrid showByPage(int page,int rows) {
		return tbItemParamServiceImpl.showByPage(page, rows);
	}
	
//	根据id删除参数规格
	@RequestMapping("item/param/delete")
	@ResponseBody
	public EgoResult delByIds(String ids) {
		int index=0;
		EgoResult er=new EgoResult();
		try {
			index=tbItemParamServiceImpl.delByIds(ids);
			if(index==1) {
				er.setStatus(200);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			er.setData(e.getMessage());
		}
		return er;
	}
	
//	根据类目id判断是否存在参数规格
	@RequestMapping("item/param/query/itemcatid/{catId}")
	@ResponseBody
	public EgoResult showByCatId(@PathVariable long catId) {
		TbItemParam showById = tbItemParamServiceImpl.showById(catId);
		EgoResult er=new EgoResult();
		if(showById!=null) {
			er.setData(showById);
			er.setStatus(200);
		}
		return er;
	}
	
	@RequestMapping("item/param/save/{catId}")
	@ResponseBody
	public EgoResult insParam(@PathVariable long catId,TbItemParam param) {
		Date date=new Date();
		param.setCreated(date);
		param.setUpdated(date);
		param.setItemCatId(catId);
		int insParam = tbItemParamServiceImpl.insParam(param);
		EgoResult er=new EgoResult();
		if(insParam==1) {
			er.setStatus(200);
		}
		return er;
	}
}
