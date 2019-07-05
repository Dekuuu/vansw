package com.ego.search.service;

import java.io.IOException;
import java.util.Map;

import org.apache.solr.client.solrj.SolrServerException;

import com.ego.pojo.TbItem;

public interface TbItemService {
	/*
	 * 初始化solr
	 */
	void init() throws SolrServerException, IOException ;
	
	/*
	 * 商品查询
	 */
	Map<String,Object> selByQuery(String query,int page,int rows) throws SolrServerException, IOException ;
	
	/*
	 * 新增商品信息
	 */
	int insItem(Map<String,Object> map,String desc) throws SolrServerException, IOException ;
}
