package com.ego.item.controller;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ego.item.service.TbItemDescService;

@Controller
public class TbItemDescController {
	@Resource
	private TbItemDescService tbItemDescServiceImpl;
	/*
	 * 根据id查询商品描述
	 */
	@RequestMapping(value="item/desc/{id}.html",produces="text/html;charset=utf-8")
	@ResponseBody
	public String showDesc(@PathVariable long id) {
		return tbItemDescServiceImpl.selDescById(id);
	}
}
