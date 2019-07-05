package com.ego.item.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.ego.dubbo.service.TbItemParamItemDubboService;
import com.ego.item.pojo.ParamItem;
import com.ego.item.service.TbItemParamItemService;
import com.ego.pojo.TbItemParamItem;
import com.ego.redis.service.JedisDao;
import com.utils.JsonUtils;
@Service
public class TbItemParamItemServiceImpl implements TbItemParamItemService{
	@Reference
	private TbItemParamItemDubboService tbItemParamItemDubboServiceImpl;
	@Resource
	private JedisDao jedisDaoImpl;
	@Value("${redis.item.param}")
	private String itemParamKey;
	/*
	 * 根据商品id查询商品信息
	 */
	@Override
	public String selById(long id) {
		String paramKey=itemParamKey+id;
//		先判断redis中是否存在商品参数、
		if(jedisDaoImpl.exists(paramKey)) {
			return jedisDaoImpl.get(paramKey);
		}
//		redis不存在对应的商品参数
		TbItemParamItem selById = tbItemParamItemDubboServiceImpl.selById(id);
		List<ParamItem> jsonToList = JsonUtils.jsonToList(selById.getParamData(),ParamItem.class);
		StringBuffer sb=new StringBuffer();
		for (ParamItem paramItem : jsonToList) {
			sb.append("<table width='500' style='color:gray;'>");
			for (int i = 0 ;i<paramItem.getParams().size();i++) {
				if(i==0){
					sb.append("<tr>");
					sb.append("<td align='right' width='30%'>"+paramItem.getGroup()+"</td>");
					sb.append("<td align='right' width='30%'>"+paramItem.getParams().get(i).getK()+"</td>");
					sb.append("<td>"+paramItem.getParams().get(i).getV()+"</td>");
					sb.append("<tr/>");
				}else{
					sb.append("<tr>");
					sb.append("<td> </td>");
					sb.append("<td align='right'>"+paramItem.getParams().get(i).getK()+"</td>");
					sb.append("<td>"+paramItem.getParams().get(i).getV()+"</td>");
					sb.append("</tr>");
				}
			}
			sb.append("</table>");
			sb.append("<hr style='color:gray;'/>");
		}
		jedisDaoImpl.set(paramKey, sb.toString());
		return sb.toString();
	}

}
