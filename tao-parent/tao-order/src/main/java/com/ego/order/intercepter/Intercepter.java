package com.ego.order.intercepter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.ego.redis.service.JedisDao;
import com.entity.EgoResult;
import com.utils.CookieUtils;
import com.utils.HttpClientUtil;
import com.utils.JsonUtils;

public class Intercepter implements HandlerInterceptor{
	@Resource
	private JedisDao jedisDaoImpl;
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
//		判断用户是否已经登录
		String token = CookieUtils.getCookieValue(request, "TT_TOKEN");
		String user = jedisDaoImpl.get(token);
		if(user!=null&&!user.equals("")) {
			String doPost = HttpClientUtil.doPost("http://localhost:8084/user/token/"+token);
			EgoResult er = JsonUtils.jsonToPojo(doPost, EgoResult.class);
			er.setStatus(200);
			return true;
		}
//		未登录则重定向跳转登录页面
		response.sendRedirect("http://localhost:8084/user/showLogin");
		return false;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		// TODO Auto-generated method stub
		
	}

}
