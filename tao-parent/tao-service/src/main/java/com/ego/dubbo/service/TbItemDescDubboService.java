package com.ego.dubbo.service;

import com.ego.pojo.TbItemDesc;

public interface TbItemDescDubboService {
/*
 * 根据id查询出对应的商品描述
 */
TbItemDesc selBy(long id);
}
