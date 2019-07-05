package com.ego.cart.intercepter;

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
//		对访问购物车进行拦截，先判断账户是否已经登录
		String token = CookieUtils.getCookieValue(request, "TT_TOKEN");
		String string = jedisDaoImpl.get(token);
		if(string!=null&&!string.equals("")) {

			String doPost = HttpClientUtil.doPost("http://localhost:8084/user/token/"+token);
			EgoResult er = JsonUtils.jsonToPojo(doPost, EgoResult.class);
			if(er.getStatus()==200) {
				return true;
			}
		}
		
		String num=request.getParameter("num");
		response.sendRedirect("http://localhost:8084/user/showLogin?interurl="+request.getRequestURL()+"%3Fnum="+num);
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
