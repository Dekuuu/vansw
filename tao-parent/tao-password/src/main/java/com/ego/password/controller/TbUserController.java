package com.ego.password.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ego.password.service.TbUserService;
import com.ego.pojo.TbUser;
import com.entity.EgoResult;

@Controller
public class TbUserController {
	@Resource
	private TbUserService tbUserServiceImpl;
	/*
	 * 登录
	 */
	@RequestMapping("user/login")
	@ResponseBody
	public EgoResult login(TbUser user,HttpServletRequest req,HttpServletResponse resp) {
		return tbUserServiceImpl.login(user,req,resp);
	}
	
	/*
	 * 跳转登录页面
	 */
	@RequestMapping("user/showLogin")
	public String showLogin(@RequestHeader(value="Referer",defaultValue = "") String url,Model model,String interurl) {
//		两种请求到该控制器：第一种是正常的网页跳转进来；
//		另一种是拦截器的重定向，interurl为拦截器的目标地址
		if(interurl!=null&&!interurl.equals("")) {
			model.addAttribute("redirect", interurl);
		}else {
//			设置返回跳转地址属性，方便登录成功后返回登录前的界面
			model.addAttribute("redirect", url);
		}		
		return "login";
	}
	
	/*
	 * 通过token获取用户信息
	 */
	@RequestMapping("user/token/{token}")
	@ResponseBody
	public Object showTokenInfo(@PathVariable String token,String callback) {
		EgoResult selByToken = tbUserServiceImpl.selByToken(token);
//		有回调函数则返回的是jsonp数据类型
		if(callback!=null&&!callback.equals("")) {
			MappingJacksonValue mjv=new MappingJacksonValue(selByToken);
			mjv.setJsonpFunction(callback);
			return mjv;
		}
//		没有回调函数则直接返回EgoResult
		return selByToken;
	}
	
	/*
	 * 用户退出登录
	 */
	@RequestMapping("user/logout/{token}")
	@ResponseBody
	public Object logout(@PathVariable String token,HttpServletRequest req, HttpServletResponse resp,String callback) {
		EgoResult logout = tbUserServiceImpl.logout(token, req, resp);
		if(callback!=null&&!callback.equals("")) {
			MappingJacksonValue mjv=new MappingJacksonValue(logout);
			mjv.setJsonpFunction(callback);
			return mjv;
		}else {
			return logout;
		}
	}
}
