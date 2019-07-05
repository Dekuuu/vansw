package com.ego.order.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.ego.dubbo.service.TbItemDubboService;
import com.ego.dubbo.service.TbOrderDubboService;
import com.ego.order.pojo.OrderParam;
import com.ego.order.service.OrderService;
import com.ego.pojo.TbItem;
import com.ego.pojo.TbOrder;
import com.ego.pojo.TbOrderItem;
import com.ego.pojo.TbOrderShipping;
import com.ego.redis.service.JedisDao;
import com.entity.EgoResult;
import com.entity.TbItemChild;
import com.utils.CookieUtils;
import com.utils.HttpClientUtil;
import com.utils.IDUtils;
import com.utils.JsonUtils;
@Service
public class OrderServiceImpl implements OrderService{
	@Resource
	private JedisDao jedisDaoImpl;
	@Value("${passport.url}")
	private String passportUrl;
	@Value("${redis.cart.key}")
	private String cartKey;
	@Reference
	private TbItemDubboService tbItemDubboServiceImpl;
	@Reference
	private TbOrderDubboService tbOrderDubboServiceImpl;
	/*
	 * 查询订单列表
	 */
	@Override
	public List<TbItemChild> showOrderList(List<Long> ids,HttpServletRequest req) {
//		先获取购物车信息，要获取购物车信息需要key，key由cart:和用户名组合，而cookie中的UUID对应用户的key
		String token = CookieUtils.getCookieValue(req, "TT_TOKEN");
		String doPost = HttpClientUtil.doPost(passportUrl+token);
		EgoResult er = JsonUtils.jsonToPojo(doPost, EgoResult.class);
		String key=cartKey+((LinkedHashMap)er.getData()).get("username");
		
		List<TbItemChild> newList=new ArrayList<TbItemChild>();
		String str = jedisDaoImpl.get(key);
		List<TbItemChild> list = JsonUtils.jsonToList(str, TbItemChild.class);
		for (TbItemChild tbItemChild : list) {
//			这里需要对商品进行选择，即订单中显示的是购物车勾选的商品，所以传参进来的是商品id集合，同时需要遍历id集合进行筛选
			for (Long id : ids) {
				if((long)tbItemChild.getId()==(long)id) {
//					校验库存量是否足够
					if(tbItemDubboServiceImpl.selById(id).getNum()>=tbItemChild.getNum()) {
						tbItemChild.setEnough(true);
					}else {
						tbItemChild.setEnough(false);
					}
					newList.add(tbItemChild);
				}
			}
		}
		return newList;
	}
	
	/*
	 * 新增订单
	 */
	@Override
	public EgoResult insOrder(OrderParam param,HttpServletRequest req) {
		TbOrder tbOrder=new TbOrder();
		tbOrder.setPayment(param.getPayment());
		tbOrder.setPaymentType(param.getPaymentType());
		String orderId=IDUtils.genItemId()+"";
		tbOrder.setOrderId(orderId);
		Date date=new Date();
		tbOrder.setCreateTime(date);
		tbOrder.setUpdateTime(date);
		
		String token = CookieUtils.getCookieValue(req, "TT_TOKEN");
		String doPost = HttpClientUtil.doPost(passportUrl+token);
		EgoResult er = JsonUtils.jsonToPojo(doPost, EgoResult.class);
		Map user=(LinkedHashMap)er.getData();
		
		tbOrder.setUserId(Long.parseLong(user.get("id")+""));
		tbOrder.setBuyerNick(user.get("username").toString());					//设置用户昵称
		tbOrder.setBuyerRate(0);				//未评价的订单
//		设置订单商品
		List<TbOrderItem> list = param.getOrderItems();
		for (TbOrderItem tbOrderItem : list) {
			tbOrderItem.setOrderId(orderId);
			tbOrderItem.setId(IDUtils.genItemId()+"");
		}
//		设置订单用户信息
		TbOrderShipping shipping = param.getOrderShipping();
		shipping.setOrderId(orderId);
		shipping.setUpdated(date);
		shipping.setCreated(date);
		EgoResult egoResult=new EgoResult();
		try {
			int ins = tbOrderDubboServiceImpl.insOrder(tbOrder, list, shipping);
			if(ins>0) {
//				删除购物车中的数据
				String string = jedisDaoImpl.get(cartKey+user.get("username").toString());
				List<TbItemChild> jsonToList = JsonUtils.jsonToList(string, TbItemChild.class);
				List<TbItemChild> newList=new ArrayList<TbItemChild>();
				for (TbItemChild tbItemChild : jsonToList) {
					for (TbOrderItem l : list) {
						if(Long.parseLong(l.getItemId())==tbItemChild.getId().longValue()) {
//							将购物车中id与订单中id相同的商品删除
							newList.add(tbItemChild);
						}
					}
					
//					还要修改对应商品表的数据
				
					
				}
//				遍历存放待删除商品的集合，逐个删除数据
				for (TbItemChild tbItemChild : newList) {
					jsonToList.remove(tbItemChild);
				}
				jedisDaoImpl.set(cartKey+user.get("username").toString(),JsonUtils.objectToJson(jsonToList));
				
				egoResult.setStatus(200);
			}else {
				egoResult.setMsg("新增账单有误!!!");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return egoResult;
	}
	

}
