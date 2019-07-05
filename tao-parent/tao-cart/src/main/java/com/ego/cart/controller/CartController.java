package com.ego.cart.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ego.cart.service.CartService;
import com.entity.EgoResult;
import com.entity.TbItemChild;

@Controller
public class CartController {
	@Resource
	private CartService cartServiceImpl;

	/*
	 * 添加商品进购物车
	 */
	@RequestMapping("cart/add/{id}.html")
	public String addcart(@PathVariable long id,int num,HttpServletRequest req) {
		cartServiceImpl.addCart(id, num, req);
		return "cartSuccess";
	}
	
	
	/*
	 * 显示购物车
	 */
	@RequestMapping("cart/cart.html")
	public String showCart(Model model,HttpServletRequest req) {
		List<TbItemChild> showCart = cartServiceImpl.showCart(req);
		model.addAttribute("cartList", showCart);
		return "cart";
	}
	
	/*
	 * 修改购物车
	 */
	@RequestMapping("cart/update/num/{id}/{num}.action")
	public EgoResult updCart(@PathVariable long id,@PathVariable int num,HttpServletRequest req) {
		return cartServiceImpl.updCart(id, num, req);
	}
	
	/*
	 * 删除购物车
	 */
	@RequestMapping("cart/delete/{id}.action")
	public EgoResult delCart(@PathVariable long id,HttpServletRequest req) {
		return cartServiceImpl.delCart(id, req);
	}
}
