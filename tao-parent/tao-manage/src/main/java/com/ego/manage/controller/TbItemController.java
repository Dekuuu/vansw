package com.ego.manage.controller;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ego.manage.service.TbItemService;
import com.ego.pojo.TbItem;
import com.entity.EasyUIDataGrid;
import com.entity.EgoResult;

@Controller
public class TbItemController {
	@Resource
	private TbItemService tbItemServiceImpl;
	@RequestMapping("item/list")
	@ResponseBody
	public EasyUIDataGrid show(int page,int rows) {
		return tbItemServiceImpl.show(page, rows);
	}
	
	/**
	 * 显示商品修改
	 * @return
	 */
	@RequestMapping("rest/page/item-edit")
	public String showItemEdit(){
		return "item-edit";
	}
	
	/**
	 * 商品删除
	 * @param ids
	 * @return
	 */
	@RequestMapping("rest/item/delete")
	@ResponseBody
	public EgoResult delete(String ids){
		EgoResult er = new EgoResult();
		int index = tbItemServiceImpl.update(ids, (byte)3);
		if(index==1){
//			修改成功则返回200状态码
			er.setStatus(200);
		}
		return er;
	}
	/**
	 * 商品下架
	 * @param ids
	 * @return
	 */
	@RequestMapping("rest/item/instock")
	@ResponseBody
	public EgoResult instock(String ids){
		EgoResult er = new EgoResult();
		int index = tbItemServiceImpl.update(ids, (byte)2);
		if(index==1){
			er.setStatus(200);
		}
		return er;
	}
	/**
	 * 商品上架
	 * @param ids
	 * @return
	 */
	@RequestMapping("rest/item/reshelf")
	@ResponseBody
	public EgoResult reshelf(String ids){
		EgoResult er = new EgoResult();
		int index = tbItemServiceImpl.update(ids, (byte)1);
		if(index==1){
			er.setStatus(200);
		}
		return er;
	}
	
//	新增商品
	@RequestMapping("item/save")
	@ResponseBody
	public EgoResult insItem(TbItem item,String desc,String itemParams) {
		int index=0;
		EgoResult er=new EgoResult();
		try {
			index=tbItemServiceImpl.insItem(item, desc,itemParams);
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
}
