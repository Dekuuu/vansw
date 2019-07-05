package com.ego.search.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.CloudSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.stereotype.Service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.ego.dubbo.service.TbItemCatDubboService;
import com.ego.dubbo.service.TbItemDescDubboService;
import com.ego.dubbo.service.TbItemDubboService;
import com.ego.pojo.TbItem;
import com.ego.search.service.TbItemService;
import com.entity.TbItemChild;
@Service
public class TbItemServiceImpl implements TbItemService{
@Reference
private TbItemDubboService tbItemDubboServiceImpl;
@Reference
private TbItemCatDubboService tbItemCatDubboService;
@Reference
private TbItemDescDubboService tbItemDescDubboServiceImpl;
@Resource 
private CloudSolrClient solrClient;
/*
 *查询出所有的正常上架商品并添加到solr中
 */
	@Override
	public void init() throws SolrServerException, IOException {
		// TODO Auto-generated method stub
		List<TbItem> showAll = tbItemDubboServiceImpl.showAll();
		for (TbItem tbItem : showAll) {
//			把数据添加到solr中
			SolrInputDocument doc=new SolrInputDocument();
			
//			一定要带上id!!!!!!!!
			doc.addField("id",tbItem.getId() );
			doc.addField("item_title", tbItem.getTitle());
			doc.addField("item_sell_point", tbItem.getSellPoint());
			doc.addField("item_price", tbItem.getPrice());
			doc.addField("item_image", tbItem.getImage());
			doc.addField("item_category_name", tbItemCatDubboService.selById(tbItem.getCid()).getName());
			doc.addField("item_desc", tbItemDescDubboServiceImpl.selBy(tbItem.getId()).getItemDesc());
			
			solrClient.add(doc);
		}
//		提交事务
		solrClient.commit();
	}
	
	/*
	 * 查询相关的商品信息(通过solr查询)
	 */
	@Override
	public Map<String, Object> selByQuery(String query, int page, int rows) throws SolrServerException, IOException {
		SolrQuery param=new SolrQuery();
//		设置查询条件
		param.setQuery("item_keywords:"+query);
//		设置分页
		param.setStart(rows*(page-1));
		param.setRows(rows);
//		对关键字设置高亮
		param.setHighlight(true);
		param.setHighlightSimplePre("<span style='color:red'>");
		param.setHighlightSimplePost("</span>");
		param.addHighlightField("item_title");
		
//		resp相当于solr查询结果中最外层的标签，包含全部东西，包含response、highlight等
		QueryResponse resp = solrClient.query(param);
//		resp.getResults()相当于是response中的内容
		SolrDocumentList results = resp.getResults();
//		resp.getHighlighting()相当于是highlight标签，内部嵌套两层：第一层是id所对应的结果，第二层是高亮属性列的结果
		Map<String, Map<String, List<String>>> hh = resp.getHighlighting();
		Map<String,Object> map=new HashMap<String, Object>();
		List<TbItemChild> list=new ArrayList<TbItemChild>();
		for (SolrDocument doc : results) {
			TbItemChild child=new TbItemChild();
			
			List<String> listHH = hh.get(doc.getFieldValue("id")).get("item_title");
			child.setId(Long.parseLong(doc.getFieldValue("id").toString()));
			if(listHH==null||listHH.size()==0) {
//				不是高亮
				child.setTitle(doc.getFieldValue("item_title").toString());	
			}else {
				child.setTitle(listHH.get(0));
			}

			
			child.setSellPoint(doc.getFieldValue("item_sell_point").toString());
			String []images=doc.getFieldValue("item_image").toString().split(",");
//			防止返回结果为空而报错
			child.setImages(images==null||images.length==0?new String[1]:images);
			child.setPrice(Long.parseLong(doc.getFieldValue("item_price").toString()));
			list.add(child);
		}
		map.put("itemList",list);
		map.put("totalPages", results.getNumFound()%rows==0?results.getNumFound()/rows:results.getNumFound()/rows+1);
		return map;
	}

	/*
	 * 新增商品信息到solr(后台同步数据)
	 * 
	 */
	@Override
	public int insItem(Map<String,Object> map,String desc) throws SolrServerException, IOException {
		SolrInputDocument doc=new SolrInputDocument();
		doc.setField("id", map.get("id"));
		doc.setField("item_title", map.get("title"));
		doc.setField("item_sell_point", map.get("sellPoint"));
		doc.setField("item_price", map.get("price"));
		doc.setField("item_image", map.get("image"));
		doc.addField("item_category_name", tbItemCatDubboService.selById((Integer)map.get("cid")).getName());
		doc.addField("item_desc", desc);
		
		UpdateResponse resp = solrClient.add(doc);
		solrClient.commit();
		return resp.getStatus();
	}
}
