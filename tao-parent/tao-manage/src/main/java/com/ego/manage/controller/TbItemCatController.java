package com.ego.manage.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ego.manage.service.TbItemCatService;
import com.entity.EasyUiTree;

@Controller
public class TbItemCatController {
	@Resource
	private TbItemCatService tbItemCatServiceImpl;
	@RequestMapping("item/cat/list")
	@ResponseBody
	public List<EasyUiTree> show(@RequestParam(defaultValue="0") long id){
		return tbItemCatServiceImpl.show(id);
	}
}
