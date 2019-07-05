package com.ego.item.service;

import com.entity.TbItemChild;

public interface TbItemService {

	/*
	 * 根据id查询出商品信息
	 */
	TbItemChild selById(long id);
}
