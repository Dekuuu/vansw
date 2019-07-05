package com.ego.cart.service.impl;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.ego.cart.service.CartService;
import com.ego.dubbo.service.TbItemDubboService;
import com.ego.pojo.TbItem;
import com.ego.redis.service.JedisDao;
import com.entity.EgoResult;
import com.entity.TbItemChild;
import com.utils.CookieUtils;
import com.utils.HttpClientUtil;
import com.utils.JsonUtils;
@Service
public class CartServiceImpl implements CartService{

	@Reference
	private TbItemDubboService tbItemDubboServiceImpl;
	@Value("${redis.cart.key}")
	private String cartKey;
	@Value("${passport.url}")
	private String passportUrl;
	@Resource
	private JedisDao jedisDaoImpl;
	/*
	 * 购物车添加商品
	 */
	@Override
	public void addCart(long id, int num,HttpServletRequest req) {
		List<TbItemChild> list=new ArrayList<TbItemChild>();
//		设置账户购物车的key
		String token = CookieUtils.getCookieValue(req, "TT_TOKEN");
		String doPost = HttpClientUtil.doPost(passportUrl+token);
		EgoResult er = JsonUtils.jsonToPojo(doPost, EgoResult.class);
		String key=cartKey+((LinkedHashMap)er.getData()).get("username");
//		添加商品到集合
//		存在对应key的数据集合，即有购物车
		if(jedisDaoImpl.exists(key)) {
//			购物车中存在对应id的商品，则修改其数量即可
			String listJson = jedisDaoImpl.get(key);
			if(listJson!=null&&!listJson.equals("")) {
				list = JsonUtils.jsonToList(listJson, TbItemChild.class);
				for (TbItemChild tbItem : list) {
					if(tbItem.getId()==id) {
						tbItem.setNum(num+tbItem.getNum());
						jedisDaoImpl.set(key, JsonUtils.objectToJson(list));
						return ;
					}
				}
			}
			
		}
//		对应的key没有购物车或者购物车中没有该id的商品，则将该商品添加进去
		TbItem item = tbItemDubboServiceImpl.selById(id);
		TbItemChild child=new TbItemChild();
		child.setPrice(item.getPrice());
		child.setNum(num);
		child.setImages(item.getImage()==null||item.getImage().equals("")?new String[1]:item.getImage().split(","));
		child.setTitle(item.getTitle());
		child.setId(item.getId());
		
		list.add(child);
		jedisDaoImpl.set(key, JsonUtils.objectToJson(list));
	}
	
	/*
	 * 修改购物车商品数量
	 */
	@Override
	public EgoResult updCart(long id, int num, HttpServletRequest req) {
		String token = CookieUtils.getCookieValue(req, "TT_TOKEN");
		String doPost = HttpClientUtil.doPost(passportUrl+token);
		EgoResult er = JsonUtils.jsonToPojo(doPost, EgoResult.class);
		String key=cartKey+((LinkedHashMap)er.getData()).get("username");
		
		String string = jedisDaoImpl.get(key);
		List<TbItemChild> list = JsonUtils.jsonToList(string, TbItemChild.class);
		for (TbItemChild tbItemChild : list) {
			if(tbItemChild.getId()==id) {
				tbItemChild.setNum(num);
				jedisDaoImpl.set(key, JsonUtils.objectToJson(list));
			}
		}
		EgoResult egoResult=new EgoResult();
		egoResult.setStatus(200);
		egoResult.setMsg("OK");
		return egoResult;
	}

	/*
	 * 删除购物车中的商品
	 */
	@Override
	public EgoResult delCart(long id, HttpServletRequest req) {
		String token = CookieUtils.getCookieValue(req, "TT_TOKEN");
		String doPost = HttpClientUtil.doPost(passportUrl+token);
		EgoResult er = JsonUtils.jsonToPojo(doPost, EgoResult.class);
		String key=cartKey+((LinkedHashMap)er.getData()).get("username");
		
		String string = jedisDaoImpl.get(key);
		List<TbItemChild> list = JsonUtils.jsonToList(string, TbItemChild.class);
		TbItemChild child=null;
		for (TbItemChild tbItemChild : list) {
			if(tbItemChild.getId()==id) {
				child=tbItemChild;
			}
		}
//		不可以边迭代遍历集合边修改集合，否则会报抛出java.util.ConcurrentModificationException异常
		list.remove(child);
		jedisDaoImpl.set(key, JsonUtils.objectToJson(list));
		EgoResult egoResult=new EgoResult();
		egoResult.setStatus(200);
		egoResult.setMsg("OK");
		return egoResult;
	}

	/*
	 * 显示购物车
	 */
	@Override
	public List<TbItemChild> showCart(HttpServletRequest req) {
		String token = CookieUtils.getCookieValue(req, "TT_TOKEN");
		String doPost = HttpClientUtil.doPost(passportUrl+token);
		EgoResult er = JsonUtils.jsonToPojo(doPost, EgoResult.class);
		String key=cartKey+((LinkedHashMap)er.getData()).get("username");
		
		String string = jedisDaoImpl.get(key);
		return JsonUtils.jsonToList(string, TbItemChild.class);
	}

}
