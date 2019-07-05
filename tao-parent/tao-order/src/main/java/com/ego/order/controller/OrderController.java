package com.ego.order.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ego.order.pojo.OrderParam;
import com.ego.order.service.OrderService;
import com.entity.EgoResult;

@Controller
public class OrderController {
	@Resource
	private OrderService orderServiceImpl;
	@RequestMapping("order/order-cart.html")
	public String orderList(Model model,@RequestParam("id") List<Long> ids,HttpServletRequest req) {
		model.addAttribute("cartList", orderServiceImpl.showOrderList(ids,req));
		return "order-cart";
	}
	
	@RequestMapping("order/create.html")
	public String insOrder(HttpServletRequest req,OrderParam param) {
		EgoResult er = orderServiceImpl.insOrder(param, req);
		if(er.getStatus()==200) {
			return "my-orders";
		}else {
			return "error/exception";
		}
	}
}
