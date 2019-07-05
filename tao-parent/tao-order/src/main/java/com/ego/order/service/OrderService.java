package com.ego.order.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.ego.order.pojo.OrderParam;
import com.entity.EgoResult;
import com.entity.TbItemChild;

public interface OrderService {
	/*
	 * 查询订单列表
	 */
	List<TbItemChild> showOrderList(List<Long> ids,HttpServletRequest req);
	
	/*
	 * 新增订单
	 */
	EgoResult insOrder(OrderParam param,HttpServletRequest req);
}
