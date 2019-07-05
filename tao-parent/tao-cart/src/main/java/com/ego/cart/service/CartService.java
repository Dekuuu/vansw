package com.ego.cart.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.entity.EgoResult;
import com.entity.TbItemChild;

public interface CartService {
	/*
	 * 购物车添加商品
	 */
	public  void addCart(long id,int num,HttpServletRequest req);
	
	/*
	 * 修改购物车商品数量
	 */
	public EgoResult updCart(long id,int num,HttpServletRequest req);
	
	/*
	 * 删除购物车中的商品
	 * 
	 */
	public EgoResult delCart(long id,HttpServletRequest req);
	
	/*
	 * 显示购物车
	 */
	public List<TbItemChild> showCart(HttpServletRequest req);
}
