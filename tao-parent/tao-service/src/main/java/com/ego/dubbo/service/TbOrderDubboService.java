package com.ego.dubbo.service;

import java.util.List;

import com.ego.pojo.TbOrder;
import com.ego.pojo.TbOrderItem;
import com.ego.pojo.TbOrderShipping;

public interface TbOrderDubboService {
/*
 * 添加订单
 */
	int insOrder(TbOrder tbOrder,List<TbOrderItem> list, TbOrderShipping shipping) throws Exception;
}
