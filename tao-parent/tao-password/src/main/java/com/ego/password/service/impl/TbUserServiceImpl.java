package com.ego.password.service.impl;

import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.ego.dubbo.service.TbUserDubboService;
import com.ego.password.service.TbUserService;
import com.ego.pojo.TbUser;
import com.ego.redis.service.JedisDao;
import com.entity.EgoResult;
import com.utils.CookieUtils;
import com.utils.JsonUtils;

@Service
public class TbUserServiceImpl implements TbUserService{
	@Reference
	private TbUserDubboService tbUserDubboServiceImpl;
	@Resource
	private JedisDao jedisDaoImpl;
	/*
	 * 根据用户账号和密码查询用户信息
	 */
	@Override
	public EgoResult login(TbUser user,HttpServletRequest req,HttpServletResponse resp) {
		TbUser selByPass = tbUserDubboServiceImpl.selByPass(user);
		EgoResult er=new EgoResult();
		if(selByPass!=null) {
			er.setStatus(200);
//			设置redis+cookie模拟session的作用，使得用户的数据可以在多个项目间使用
			String key=UUID.randomUUID().toString();
			jedisDaoImpl.set(key, JsonUtils.objectToJson(selByPass));
			jedisDaoImpl.expire(key, 60*60*24);
			CookieUtils.setCookie(req, resp, "TT_TOKEN", key,60*60*24);
		}else{
			er.setData("登录失败");
		}
		return er;
	}

/*
 * 根据token查找用户信息
 */
	@Override
	public EgoResult selByToken(String token) {
//		token 为session的key
		String string = jedisDaoImpl.get(token);
		EgoResult er=new EgoResult();
		if(string!=null&&!string.equals("")) {
			TbUser user = JsonUtils.jsonToPojo(string, TbUser.class);
			user.setPassword(null);
			er.setData(user);
			er.setStatus(200);
			er.setMsg("OK");
		}else {
			er.setMsg("失败！！");
		}
		return er;
	}

	/*
	 * 用户退出登录
	 */
	@Override
	public EgoResult logout(String token, HttpServletRequest req, HttpServletResponse resp) {
		jedisDaoImpl.del(token);
		CookieUtils.deleteCookie(req, resp, "TT_TOKEN");
		EgoResult er=new EgoResult();
		er.setStatus(200);
		er.setMsg("OK");
		er.setData("");
		return er;
	}
}
