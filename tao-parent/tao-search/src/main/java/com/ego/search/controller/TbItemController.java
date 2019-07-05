package com.ego.search.controller;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.solr.client.solrj.SolrServerException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ego.pojo.TbItem;
import com.ego.search.service.TbItemService;

@Controller
public class TbItemController {
@Resource
private TbItemService tbItemServiceImpl;

@RequestMapping("item/init")
@ResponseBody
public String init() {
	try {
		tbItemServiceImpl.init();
		return "success";
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		return "fail";
	}
}

@RequestMapping("search.html")
public String search(String q,Model model,@RequestParam(defaultValue="1") int page,@RequestParam(defaultValue="10")int rows) {
	try {
//		要设置请求值的编码
		String query=new String(q.getBytes("iso-8859-1"),"utf-8");
		Map<String, Object> map = tbItemServiceImpl.selByQuery(query, page, rows);
		model.addAttribute("query", query);
		model.addAttribute("itemList", map.get("itemList"));
		model.addAttribute("totalPages", map.get("totalPages"));
		model.addAttribute("page", page);
	} catch (SolrServerException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	return "search";
}

/*
 * 新增商品到solr
 */
@RequestMapping("solr/add")
@ResponseBody
public int insItem(@RequestBody Map<String,Object> map) {
	try {
		int index = tbItemServiceImpl.insItem((LinkedHashMap)map.get("item"),map.get("desc").toString());
		return index;
	} catch (SolrServerException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	return 0;
}
}
